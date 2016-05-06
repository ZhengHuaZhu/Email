package com.kfcstd.jag_phase3.controllers;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.MainAppFX;
import com.kfcstd.jag_phase3.beans.PropertiesBean;
import com.kfcstd.jag_phase3.javafxHTMLEditor.view.EmailValidator;
import com.kfcstd.jag_phase3.properties_manager.PropertiesManager;

/**
 * Property controller to handle user information and DB access information
 *
 * #KFCStandard and JavaFX8
 *
 * @author 
 */
public class PropertyController {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());    
    private PropertiesManager pm;   
    private PropertiesBean pb;
    private File file;
    
    @FXML
    private Label labelemailpassword;

    @FXML
    private Label labelport;

    @FXML
    private Label labelemail;

    @FXML
    private Label labeldb;

    @FXML
    private Label labeluser;

    @FXML
    private Label labelpassword;

    @FXML
    private Label labelname;

    @FXML
    private Label labelusername;

    @FXML
    private Label labelhost;

    @FXML
    private Button btnconfirm;

    @FXML
    private Label labelimap;

    @FXML
    private Label labelsmtp;
    
    @FXML
    private TextField password;

    @FXML
    private TextField emailpassword;

    @FXML
    private TextField portnumber;

    @FXML
    private TextField imapurl;

    @FXML
    private TextField databasename;

    @FXML
    private TextField name;

    @FXML
    private TextField emailaddress;

    @FXML
    private TextField hosturl;

    @FXML
    private TextField smtpurl;

    @FXML
    private TextField user;

    @FXML
    private TextField username;
    
    // resources were from the FXMLLoader
    @FXML
    private ResourceBundle resources;
    
    @FXML
    private AnchorPane PropertyPane;
    
    private AnchorPane ap;
    
    private boolean hasinvalidinput = true;
    private boolean isConfiguration = false;
    private Stage configstage;
    
    @FXML
    private void onClickConfirm(ActionEvent event) throws IOException{
    	EmailValidator ev = new EmailValidator();
    	
    	labelusername.setText(null);
    	labelemail.setText(null);
    	labelname.setText(null);
    	labelemailpassword.setText(null);
    	labelsmtp.setText(null);
        labelimap.setText(null);
        labelhost.setText(null);
        labelport.setText(null);
        labeldb.setText(null);
        labeluser.setText(null);
        labelpassword.setText(null);
        
    	// properties validation	
    	if(username.getText()==null || username.getLength()==0 || username.getLength()>50){
    		labelusername.setText("User Name should be within 1 to 50 characters");
    	}
    	else if(!ev.validate(emailaddress.getText())){
    		labelemail.setText("Invalid email address");
    	}
    	else if(name.getText()==null || name.getLength()==0 || name.getLength()>50){
    		labelname.setText("Name should be within 1 to 50 characters");
    	}
    	else if(emailpassword.getText()==null || emailpassword.getLength()<6 || emailpassword.getLength()>16){
    		labelemailpassword.setText("Email password should be between 6 to 16 characters");
    	}
    	else if(smtpurl.getText()==null || smtpurl.getLength()==0 || smtpurl.getLength()>20){
    		labelsmtp.setText("You may have a wrong SMTP address");
    	}
    	else if(imapurl.getText()==null || imapurl.getLength()==0 || imapurl.getLength()>20){
    		labelimap.setText("You may have a wrong IMAP address");
    	}
    	else if(hosturl.getText()==null || hosturl.getLength()==0 || hosturl.getLength()>255){
    		labelhost.setText("You may have a wrong host url");
    	}
    	else if(portnumber.getText()==null || !portnumber.getText().matches("^\\d+$")){
    		labelport.setText("Port number must consist of digits only");
    	}
    	else if(databasename.getText()==null || databasename.getLength()==0 || databasename.getLength()>50){
    		labeldb.setText("Database name should be within 50 characters");
    	}
    	else if(user.getText()==null || user.getLength()==0 || user.getLength()>50){
    		labeluser.setText("User should be within 50 characters");
    	}
    	else if(password.getText()==null || password.getLength()>16){
    		labelpassword.setText("Password should be within 16 characters");
    	}else{
    		hasinvalidinput=false;
    	}	
    	
    	if(!hasinvalidinput){
	    	try{
	    		pm.writeTextProperties("", file.getName(), pb);
	    		if(isConfiguration)
	    			configstage.close();
	    	}catch (IOException ioe){
	    		log.error("Writing SettingBundle.properties failed.");
	    	}
	    	
	    	if(!isConfiguration){
		        // get the primary stage
		    	Stage stage2 = (Stage) PropertyPane.getScene().getWindow();
		    	FXMLLoader loader=new FXMLLoader();
		    	loader.setResources(ResourceBundle.getBundle("SettingBundle"));
		    	// point to the App's root layout
				loader.setLocation(MainAppFX.class
						.getResource("/fxml/RootLayout.fxml"));
				stage2.setTitle(ResourceBundle.getBundle("SettingBundle").getString("AppTitle"));
				stage2.getIcons().add(new Image(MainAppFX.class.getResourceAsStream("/images/mail.png")));
				ap = (AnchorPane) loader.load();
		
				Scene scene = new Scene(ap);
				stage2.setScene(scene);
				stage2.show();
				log.info("Program started.");
	    	}
    	}
    }

    @FXML
    private void onClickQuit(ActionEvent event) {
    	Platform.exit();
    }


    /**
     * This method is automatically called after the fxml file has been loaded.
     * Useful if a control must be dynamically configured such as loading data
     * into a table. In this code all it does is binding accessing data to a property file.
     */
    @FXML
    private void initialize() {
        pm=new PropertiesManager();
        pb=new PropertiesBean();
        file=new File("Setting.properties");
        
        if(file.exists()){
        	try{
        		pb=pm.loadTextProperties("", file.getName());
        		Bindings.bindBidirectional(username.textProperty(), pb.usernameProperty());
                Bindings.bindBidirectional(emailaddress.textProperty(), pb.emailaddressProperty());
                Bindings.bindBidirectional(name.textProperty(), pb.nameProperty());
                Bindings.bindBidirectional(emailpassword.textProperty(), pb.emailpasswordProperty());
                Bindings.bindBidirectional(smtpurl.textProperty(), pb.smtpurlProperty());
                Bindings.bindBidirectional(imapurl.textProperty(), pb.imapurlProperty());
                Bindings.bindBidirectional(hosturl.textProperty(), pb.hosturlProperty());
                Bindings.bindBidirectional(portnumber.textProperty(), pb.portnumberProperty());
                Bindings.bindBidirectional(databasename.textProperty(), pb.databasenameProperty());
                Bindings.bindBidirectional(user.textProperty(), pb.userProperty());
                Bindings.bindBidirectional(emailpassword.textProperty(), pb.emailpasswordProperty());
                
        	}catch (IOException ioe){
        		log.error("Error: " + ioe.getMessage());
        	}catch (NullPointerException npe){
        		log.error("Error: " + npe.getMessage());
        	}
        }else{
        	log.error("Property file is missing");
        }               
    }
    
    /**
     * check if the controller is called from root menu
     * 
     * @param bool
     */
    public void updateConfiguration(boolean bool){
    	isConfiguration=bool;
    }
    
    /**
     * set the configstage
     * 
     * @param stage
     */
    public void setStage(Stage stage){
    	configstage=stage;
    }
}
