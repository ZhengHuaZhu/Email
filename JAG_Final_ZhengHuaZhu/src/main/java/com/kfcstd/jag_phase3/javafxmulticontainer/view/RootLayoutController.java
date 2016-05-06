package com.kfcstd.jag_phase3.javafxmulticontainer.view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.MainAppFX;
import com.kfcstd.jag_phase3.controllers.PropertyController;
import com.kfcstd.jag_phase3.javafxHTMLEditor.view.HTMLController;
import com.kfcstd.jag_phase3.javafxMailTable.view.MailTableController;
import com.kfcstd.jag_phase3.javafxTree.view.FolderController;
import com.kfcstd.jag_phase3.javafxTree.view.TreeLayoutController;
import com.kfcstd.jag_phase3.persistence.EmailDAO;
import com.kfcstd.jag_phase3.persistence.EmailDAOImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This is the root layout. All of the other layouts are added in code here.
 * This allows us to use the standalone containers with minimal changes.
 * 
 * i18n added
 * 
 * @author Zheng Hua Zhu
 * @version 1.1
 *
 */
public class RootLayoutController {
	
    @FXML
    private AnchorPane EditorAnchorPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private AnchorPane TreeAnchorPane;

    @FXML
    private AnchorPane MailTableAnchorPane;
    
    @FXML 
    private ResourceBundle resources;
    
    @FXML
    private TextField newfoldername;

	private EmailDAO emailDAO;
	private TreeLayoutController treeController;
	private MailTableController tableController;
	private HTMLController htmlController;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	public RootLayoutController() {
		super();
		emailDAO = new EmailDAOImpl();
	}

	/**
	 * Here we call upon the methods that load the other containers and then
	 * send the appropriate action command to each container
	 */
	@FXML
	private void initialize() {

		initLeftLayout();
		initUpperRightLayout();
		initLowerRightLayout();

		// Tell the tree about the table
		setTableControllerToTree();

		try {
			treeController.displayTree();
			tableController.displayTheTable(-1);
			//htmlController.displayOtherHTML();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send the reference to the FishFXTableController to the FishFXTreeController
	 */
	private void setTableControllerToTree() {
		treeController.setTableController(tableController);
	}

	/**
	 * The TreeView Layout
	 */
	private void initLeftLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);
			
			loader.setLocation(MainAppFX.class
					.getResource("/fxml/TreeLayout.fxml"));
			AnchorPane treeView = (AnchorPane) loader.load();

			// Give the controller the data object.
			treeController = loader.getController();
			treeController.setEmailDAO(emailDAO);

			TreeAnchorPane.getChildren().add(treeView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The TableView Layout
	 */
	private void initUpperRightLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class
					.getResource("/fxml/MailTableLayout.fxml"));
			AnchorPane tableView = (AnchorPane) loader.load();

			// Give the controller the data object.
			tableController = loader.getController();
			tableController.setEmailDAO(emailDAO);

			MailTableAnchorPane.getChildren().add(tableView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The HTMLEditor Layout
	 */
	private void initLowerRightLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);
			loader.setLocation(MainAppFX.class
					.getResource("/fxml/HTMLEditorLayout.fxml"));
			AnchorPane ap = (AnchorPane) loader.load();
			
			// Give the controller the data object.
			htmlController = loader.getController();
			htmlController.setEmailDAO(emailDAO);
			EditorAnchorPane.getChildren().add(ap);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   
   /**
    * This method is to add a new folder
    * 
    * @param event
    */
   @FXML
   private void addNewFolder(ActionEvent event) {
	   try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);
			loader.setLocation(MainAppFX.class
					.getResource("/fxml/NewFolderLayout.fxml"));
			AnchorPane FolderPane = (AnchorPane) loader.load();				
	    	Stage addfolderstage = new Stage();
			addfolderstage.setTitle("Add new folder");
			addfolderstage.initModality(Modality.WINDOW_MODAL);
			// get the primary stage
			addfolderstage.initOwner(rootAnchorPane.getScene().getWindow());
			Scene scene = new Scene(FolderPane);
			addfolderstage.setScene(scene);
			
			// Give the controller the data object.
			FolderController fc = loader.getController();
			fc.setEmailDAO(emailDAO);
			fc.setTreeController(treeController);
			addfolderstage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
   
   /**
    * This method is to configure the Setting.properties file
    * 
    * @param event
    */
   @FXML
   void configureProperties(ActionEvent event) {
		try{
			 FXMLLoader loader=new FXMLLoader();

		    // point to the layout for properties checking
			loader.setLocation(MainAppFX.class
					.getResource("/fxml/Scene.fxml"));
		
			Stage configstage = new Stage();
			configstage.setTitle(ResourceBundle.getBundle("SettingBundle").getString("title"));
			configstage.initModality(Modality.WINDOW_MODAL);
			// get the primary stage
			configstage.initOwner(rootAnchorPane.getScene().getWindow());
			AnchorPane configPane = (AnchorPane) loader.load();
			Scene scene = new Scene(configPane);
			configstage.setScene(scene);
			configstage.show();
			// Give the controller the data object.
			PropertyController pc = loader.getController();
			pc.setStage(configstage);
			pc.updateConfiguration(true);
			log.info("Property checking launched.");			
		}catch(Exception e){
			log.error("The causing problem is: "+e.getMessage());
		}
   }
   
   /**
	 * Opens an about dialog.
	 */
	@FXML
	void handleAbout() {
		// Modal dialog box
		// JavaFX dialog coming in 8u40
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle(resources.getString("HTMLDemo"));
		dialog.setHeaderText(resources.getString("About"));
		dialog.setContentText(resources.getString("Demo"));
		dialog.show();
	}
  
}

