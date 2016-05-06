package com.kfcstd.jag_phase3.javafxMailTable.view;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.MainAppFX;
import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.beans.RecipientBean;
//import com.kfcstd.jag_phase3.beans.RecipientBean;
import com.kfcstd.jag_phase3.persistence.EmailDAO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This controller is to render MailBean objects into a table view
 *  
 * @author Zheng Hua Zhu
 *
 */
public class MailTableController {
	
	@FXML
	private AnchorPane tablePane;
    
    private AnchorPane mailPane;
	
	@FXML
	private TableView<MailBean> tableView;
    
	@FXML
    private TableColumn<MailBean, String> subject;

    @FXML
    private TableColumn<MailBean, Number> mailid;

    @FXML
    private TableColumn<MailBean, String> from;

    @FXML
    private TableColumn<MailBean, String> to;

    @FXML
    private TableColumn<MailBean, LocalDateTime> time;

    @FXML
    private TableColumn<MailBean, String> content;

    private final Logger log = LoggerFactory.getLogger(this.getClass()
    			.getName());

    private EmailDAO emailDAO;
    
    // resources that are from the FXMLLoader
    @FXML
    private ResourceBundle resources;
    
  
	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public MailTableController() {
		super();
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Connects the property in the MailBean object to the column in the table
		mailid.setCellValueFactory(cellData -> cellData.getValue()
				.mailIDProperty());
		from.setCellValueFactory(cellData -> cellData.getValue()
				.fromFieldProperty());
		
//		to.setCellValueFactory(cellData -> cellData.getValue()
//				.getToField().get(0));
		subject.setCellValueFactory(cellData -> cellData.getValue()
				.subjectFieldProperty());
		time.setCellValueFactory(cellData -> cellData.getValue()
				.receivedTimeProperty());
		content.setCellValueFactory(cellData -> cellData.getValue()
				.textFieldProperty());
		
		log.info("Data are bound.");

		adjustColumnWidths();

		// Listen for selection changes and show the mail details when changed.
		tableView
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showMailDetails(newValue));
		
		// We are going to drag and drop
		tableView.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				/* drag was detected, start drag-and-drop gesture */
				log.debug("onDragDetected");

				/* allow any transfer mode */
				Dragboard db = tableView.startDragAndDrop(TransferMode.ANY);

				/* put a string on dragboard */
				ClipboardContent content = new ClipboardContent();
				content.putString(tableView.getSelectionModel().getSelectedItem().toString());

				db.setContent(content);

				event.consume();
			}
		});
	}

	/**
	 * Sets a reference to the EmailDAO object that retrieves data from the
	 * database
	 * 
	 * @param emailDAO
	 * @throws SQLException
	 */
	public void setEmailDAO(EmailDAO emailDAO) throws SQLException {
		this.emailDAO = emailDAO;
	}
	
	public void displayTheTable(int folderid) throws SQLException {
		if(folderid == -1){
			// show all mails if no specific folder is selected
			tableView.setItems(this.emailDAO.findAllMails());
		}else{
			// show mails according to folder id
			tableView.setItems(this.emailDAO.findByFolderID(folderid));
		}		
	}
	
	/**
	 * The TreeLayoutController needs a reference to the this controller. With
	 * that reference it can call this method to retrieve a reference to the
	 * TableView and change its selection
	 * 
	 * @return
	 */
	public TableView<MailBean> getMailTable() {
		return tableView;
	}
 

	/**
	 * Sets the width of the columns based on a percentage of the overall width
	 */
	private void adjustColumnWidths() {
		// Get the current width of the table
		double width = tableView.getPrefWidth();
		// Set width of each column
		mailid.setPrefWidth(width * .05);
		subject.setPrefWidth(width * .1);
		content.setPrefWidth(width * .1);
		to.setPrefWidth(width * .1);
		from.setPrefWidth(width * .1);
		time.setPrefWidth(width * .1);
	}

	/**
	 * This method displays the MailBean object that corresponds to the selected row.
	 * 
	 * @param MailBean object
	 */
	private void showMailDetails(MailBean mb) {
		if(mb!=null){
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainAppFX.class
						.getResource("/fxml/MailRendererLayout.fxml"));
				mailPane = (AnchorPane) loader.load();
						
		    	Stage mailrenderingstage = new Stage();
				mailrenderingstage.setTitle("Mail Details");
				mailrenderingstage.initModality(Modality.WINDOW_MODAL);
				// get the primary stage
				mailrenderingstage.initOwner((Stage) tablePane.getScene().getWindow());
				Scene scene = new Scene(mailPane);
				mailrenderingstage.setScene(scene);

				// Give the controller the data object.
				MailRenderingController  mrc = loader.getController();
				mrc.setRenderingSatge(mailrenderingstage);
				mrc.setMail(mb);
				mailrenderingstage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * When a drag is detected the control at the start of the drag is accessed
	 * to determine what will be dragged.
	 * 
	 * SceneBuilder writes the event as ActionEvent that you must change to the
	 * proper event type that in this case is DragEvent
	 * 
	 * @param event
	 */
	@FXML
	private void dragDetected(MouseEvent event) {
		/* drag was detected, start drag-and-drop gesture */
		log.debug("onDragDetected");

		/* allow any transfer mode */
		Dragboard db = tableView.startDragAndDrop(TransferMode.ANY);

		/* put a string on dragboard */
		ClipboardContent content = new ClipboardContent();
		content.putString(tableView.getSelectionModel().getSelectedItem().toString());

		db.setContent(content);

		event.consume();

	}

}

