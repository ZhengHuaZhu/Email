package com.kfcstd.jag_phase3.javafxHTMLEditor.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.beans.MailConfigBean;
import com.kfcstd.jag_phase3.beans.PropertiesBean;
import com.kfcstd.jag_phase3.mailaction.BasicSendAndReceive;
import com.kfcstd.jag_phase3.persistence.EmailDAO;
import com.kfcstd.jag_phase3.properties_manager.PropertiesManager;


/**
 * This controller is in charge of rendering the HTML editor.
 * 
 * i18n added
 * 
 * @author Zheng Hua Zhu
 * @version 1.1
 *
 */
public class HTMLController {

	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	// We will need this to read from the database
	private EmailDAO emailDAO;

	@FXML
	private HTMLEditor editor;

    @FXML
    private Button sendbtn;
    
    // Resource bundle is injected when controller is loaded
    @FXML 
    private ResourceBundle resources;
    MailBean mb;

    @FXML
    private Button addAttachments;

    @FXML
    private TextField toField;

    @FXML
    private TextField attachmentField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextField ccField;
    
    @FXML
    private AnchorPane HTMLPane;
    
    @FXML
    private TextField embedded;

    @FXML
    private TextField bccField;   
    private EmailValidator ev;
    private MailConfigBean sendConfigBean, recConfigBean;
    private PropertiesManager pm;
    private PropertiesBean pb;
    private BasicSendAndReceive basicSendAndReceive;
    private File file;
    private Path path;
    private String str = "", str2 = "";
    private FileChooser chooser;
    private EmailAttachmentBuilder eab;
    private EmailAttachment ea;
    

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. Not much to do here.
	 */
	@FXML
	private void initialize() {
		pm = new PropertiesManager();
		basicSendAndReceive = new BasicSendAndReceive();
		mb = new MailBean();
    	try{
			pb=pm.loadTextProperties("","Setting.properties");
			sendConfigBean = new MailConfigBean(pb.getSmtpurl(), pb.getEmailaddress(), pb.getEmailpassword());
			recConfigBean = new MailConfigBean(pb.getImapurl(), pb.getEmailaddress(), pb.getEmailpassword());
		}catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}   	
	}
	
	/**
	 * This just displays the contents of the HTMLEditor
	 */
    @FXML
    public void onClickSend() {
    
    	String[] tolist = null;
    	ev = new EmailValidator();
    	// get user input
    	if(toField.getText()!=null){
    		tolist = toField.getText().split(",|;|\\s");
	    	// add to addresses
	    	for(String each : tolist)
	    		// validate email pattern
	    		if(ev.validate(each))
	    			mb.getToField().add(each.trim()); 
	    	toField.setText(null);
    	}
    	
    	String[] cclist = null;
    	// get user input
    	if(ccField.getText()!=null){
    		cclist = ccField.getText().split(",|;|\\s");
	    	// add to addresses
	    	for(String each : cclist)
	    		// validate email pattern
	    		if(ev.validate(each))
	    			mb.getCcField().add(each.trim());
	    	ccField.setText(null);
    	}
    	
    	String[] bcclist = null;
    	// get user input
    	if(bccField.getText()!=null){
    		bcclist = bccField.getText().split(",|;|\\s");
	    	// add to addresses
	    	for(String each : bcclist)
	    		// validate email pattern
	    		if(ev.validate(each))
	    			mb.getbCCField().add(each.trim());
	    	bccField.setText(null);
    	}
    	
    	if(editor.getHtmlText()!=null){
    		mb.setHtmlField(editor.getHtmlText());
    		editor.setHtmlText(null);
    	}
		
		if(subjectField.getText()!=null){
			mb.setSubjectField(subjectField.getText());
			subjectField.setText(null);
		}
		
		String messageID=null;
		if(mb.getToField().size()>0 && mb.getSubjectField()!="" && (mb.getTextField()!="" || mb.getHtmlField()!="")){
			try{
				mb.setFromField(sendConfigBean.getEmailaddress());
		    	mb.setFolderID(2);
				emailDAO.create(mb);
				messageID = basicSendAndReceive.sendEmail(mb, sendConfigBean);
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		ArrayList<MailBean> mbs= basicSendAndReceive.receiveEmail(recConfigBean);
	
		if(mbs!=null && mbs.size()>0){
			for(MailBean each : mbs){
				try{
					each.setFolderID(1);
					emailDAO.create(each);
					if(each.getAttachmentField().size()>0){
						for(EmailAttachment ea : each.getAttachmentField()){
							byte data[] = ea.toByteArray();
							Path path = Paths.get(ea.getName());
							Files.write(path, data);
						}
					}
					if(each.getEmbeddedAttField().size()>0){
						for(EmailAttachment ea : each.getEmbeddedAttField()){
							byte data[] = ea.toByteArray();
							Path path = Paths.get(ea.getName());
							Files.write(path, data);
						}
					}
				}catch (SQLException se) {
					se.printStackTrace();
				}catch (IOException ie) {
					ie.printStackTrace();
				}
			}		
    	    log.info("The amount of mail received: " + mbs.size());
		}
		log.debug("Send is clicked.");
		str="";
		str2="";
		attachmentField.setText(null);
		embedded.setText(null);
    }
    
    /**
     * add regular attachment selected from local
     * @param event
     */
    @FXML
    public void addAttachments(ActionEvent event) {
    	chooser = new FileChooser();
        chooser.setTitle("Open File");
        file = chooser.showOpenDialog(editor.getScene().getWindow());
        if(file!=null){
	        str += file.getName() +"  ";
	        attachmentField.setText(str);
	        
			eab = EmailAttachment.attachment().file(file.getAbsolutePath());
			ea = eab.create();
			mb.getAttachmentField().add(ea);
        }
    }
    
    /**
     * add embedded attachment selected from local
     * @param event
     */
    @FXML
    public void addEmbedded(ActionEvent event) {
    	chooser = new FileChooser();
        chooser.setTitle("Open File");
        file = chooser.showOpenDialog(editor.getScene().getWindow());
        if(file!=null){
	        str2 += file.getName() +"  ";
	        embedded.setText(str2);
	        
	        eab = EmailAttachment.attachment().bytes(new File(file.getAbsolutePath()));
			eab.setInline(file.getName()); // designate CID to an embedded attachment
			ea = eab.create();
			mb.getEmbeddedAttField().add(ea); 
        }
    }
 
	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}

	/**
	 * The RootLayoutController calls this method to provide a reference to the
	 * EmailDAO object.
	 * 
	 * @param emailDAO
	 * @throws SQLException
	 */
	public void setEmailDAO(EmailDAO emailDAO) {
		this.emailDAO = emailDAO;
	}
	
    /**
	 * When the mouse is released over the FXHTMLEditor the value is written to
	 * the editor.
	 * 
	 * SceneBuilder writes the event as ActionEvent that you must change to the
	 * proper event type that in this case is DragEvent
	 * 
	 * @param event
	 */
	@FXML
	private void dragDropped(DragEvent event) {
		log.debug("onDragDropped");
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasString()) {
			editor.setHtmlText(db.getString());
			success = true;
		}
		/*
		 * let the source know whether the string was successfully transferred
		 * and used
		 */
		event.setDropCompleted(success);

		event.consume();
	}
    
    /**
	 * This method prevents dropping the value on anything but the
	 * FXHTMLEditor,
	 * 
	 * SceneBuilder writes the event as ActionEvent that you must change to the
	 * proper event type that in this case is DragEvent
	 * 
	 * @param event
	 */
	@FXML
	private void dragOver(DragEvent event) {
		/* data is dragged over the target */
		log.debug("onDragOver");

		/*
		 * Accept it only if it is not dragged from the same control and if it
		 * has a string data
		 */
		if (event.getGestureSource() != editor && event.getDragboard().hasString()) {
			/*
			 * allow for both copying and moving, whatever user chooses
			 */
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}

		event.consume();
	}

}
