package com.kfcstd.jag_phase3;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.controllers.PropertyController;
import com.kfcstd.jag_phase3.javafxHTMLEditor.view.HTMLController;
import com.kfcstd.jag_phase3.javafxMailTable.view.MailTableController;
import com.kfcstd.jag_phase3.persistence.EmailDAO;
import com.kfcstd.jag_phase3.persistence.EmailDAOImpl;

/**
 * In this program we are creating a layout that will contain four other
 * containers. 
 * 
 * i18n added
 * 
 * Thanks to Marco Jakob's excellent tutorial at
 * http://code.makery.ch/java/javafx-8-tutorial-intro/ for much of the
 * inspiration.
 * 
 * @author 
 * @version 1.1
 *
 */
public class MainAppFX extends Application {

	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private Stage primaryStage;
	private AnchorPane ap;
	private EmailDAO emailDAO;
	
	public MainAppFX() {
		super();
		emailDAO=new EmailDAOImpl();
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		FXMLLoader loader=new FXMLLoader();
		try{
		// load default properties
		loader.setResources(ResourceBundle.getBundle("SettingBundle"));	
		// point to the layout for properties checking
		loader.setLocation(MainAppFX.class
				.getResource("/fxml/Scene.fxml"));
		
		this.primaryStage.setTitle(ResourceBundle.getBundle("SettingBundle").getString("title"));
		ap = (AnchorPane) loader.load();

		Scene scene = new Scene(ap);
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
		log.info("Property checking launched.");
		}catch(Exception e){
			log.error("The causing problem is: "+e.getMessage());
		}
	}

	/**
	 * Where it all begins
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
