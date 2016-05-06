/**
 * 
 */
package com.kfcstd.jag_phase3.javafxMailTable.view;

import java.io.IOException;

import com.kfcstd.jag_phase3.MainAppFX;
import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.javafxWebView.view.WebViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import jodd.mail.EmailAttachment;

/**
 * @author Zheng Hua Zhu
 *
 */
public class MailRenderingController {
	
	@FXML
	private HTMLEditor editor;
	
	@FXML
	private TextField to_or_fromField;
	
	@FXML
	private ListView<String> attachList;
	
	@FXML
	private TextField subjectField;
	
	@FXML
	private TextField ccField;
	
	@FXML
	private TextField bccField;
	
    @FXML
    private Label to_or_fromLabel;
	
	private Stage mailrenderingstage;	
	private MailBean mb;	

	public void setRenderingSatge(Stage mailrenderingstage) {
		this.mailrenderingstage = mailrenderingstage;	
	}

	public void setMail(MailBean mb) {
		this.mb = mb;
		String str = "";
		// inbox mail detail (taking the fromField)
		if(mb.getFolderID() == 1){
			to_or_fromLabel.setText("From");
			to_or_fromField.setText(mb.getFromField());
		}
		// sent mail detail (taking the toField)
		if(mb.getFolderID() == 2){
			to_or_fromLabel.setText("To");
			if(mb.getToField()!=null && mb.getToField().size()>0){
				for(String each : mb.getToField())
					str+=each+", ";	
				to_or_fromField.setText(str.substring(0, str.length()-2));
			}
		}		
		str="";
		
		subjectField.setText(mb.getSubjectField());
		
		if(mb.getCcField()!=null && mb.getCcField().size()>0){
			for(String each : mb.getCcField())
				str+=each+", ";
			ccField.setText(str.substring(0, str.length()-2));
		}
		str="";
		
		if(mb.getbCCField()!=null && mb.getbCCField().size()>0){
			for(String each : mb.getbCCField())
				str+=each+", ";	
			bccField.setText(str.substring(0, str.length()-2));
		}
		str="";
	
		if(mb.getHtmlField()!=null){
			editor.setHtmlText(mb.getTextField() + mb.getHtmlField());
		}else{
			editor.setHtmlText(mb.getTextField());
		}
		
		ObservableList<String> datastr = FXCollections.observableArrayList();
		
		if(mb.getAttachmentField()!=null && mb.getAttachmentField().size()>0)
			for(EmailAttachment ea : mb.getAttachmentField()){
				datastr.add(ea.getName());
			}
		
		if(mb.getEmbeddedAttField()!=null && mb.getEmbeddedAttField().size()>0)
			for(EmailAttachment ea : mb.getEmbeddedAttField()){
				datastr.add(ea.getName());
			}
		
		attachList.setItems(datastr);

		// Listen for selection changes and show the selected attachment.
		attachList
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showAttachment(newValue));				
	}

	private void showAttachment(String filename) {
		if(filename!=null){
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainAppFX.class
						.getResource("/fxml/WebViewLayout.fxml"));
				AnchorPane attachmentPane = (AnchorPane) loader.load();
						
		    	Stage attachmentstage = new Stage();
				attachmentstage.setTitle("Attachment rendering");
				attachmentstage.initModality(Modality.WINDOW_MODAL);
				// get the primary stage
				attachmentstage.initOwner(mailrenderingstage);
				Scene scene = new Scene(attachmentPane);
				attachmentstage.setScene(scene);

				// Give the controller the data object.
				WebViewController  wvc = loader.getController();
				wvc.initialize(filename);
				attachmentstage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
