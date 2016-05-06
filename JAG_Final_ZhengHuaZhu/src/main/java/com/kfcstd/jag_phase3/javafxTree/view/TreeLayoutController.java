package com.kfcstd.jag_phase3.javafxTree.view;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.beans.FolderBean;
import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.javafxMailTable.view.MailTableController;
import com.kfcstd.jag_phase3.persistence.EmailDAO;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * This controller is a part of the root container.
 * 
 * A method is added to allow the RootLayoutController to pass in a reference
 * to the MailTableController
 * 
 * i18n added
 * 
 * @author 
 * @version 
 *
 */
public class TreeLayoutController {
	private final Logger log = LoggerFactory.getLogger(this.getClass()
            .getName());

	private EmailDAO emailDAO;
	
	private MailTableController tableController;
	
	@FXML
    private TreeView<FolderBean> treeview;

    @FXML
    private AnchorPane TreeAnchorPane;
    
    @FXML
    private Button add;

    @FXML
    private TextField newfoldername;
    
    private final ContextMenu cm = new ContextMenu();
    private MenuItem mi;
    private TreeItem<FolderBean> item;



	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// We need a root node for the tree and it must be the same type as all
		// nodes
		FolderBean fb = new FolderBean();

		// The tree will display common name so we set this for the root
		// Because we are using i18n the root name comes from the resource
		// bundle
		fb.setName("Folders");

		treeview.setRoot(new TreeItem<FolderBean>(fb));

		// This cell factory is used to choose which field in the FolderBean object
		// is used for the node name
		treeview.setCellFactory((e) -> new TreeCell<FolderBean>() {
			@Override
			protected void updateItem(FolderBean item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getName());
					setGraphic(getTreeItem().getGraphic());
				} else {
					setText("");
					setGraphic(null);
				}
			}
		});	
		
		mi = new MenuItem("Delete this folder");
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
	 * The RootLayoutController calls this method to provide a reference to the
	 * MailTableController from which it can request a reference to the
	 * TreeView. With theTreeView reference it can change the selection in the
	 * TableView.
	 * 
	 * @param tableController
	 */
	public void setTableController(MailTableController tableController) {
		this.tableController = tableController;
	}

	/**
	 * Build the tree from the database
	 * 
	 * @throws SQLException
	 */
	public void displayTree() throws SQLException {
		// refresh the treeview by flushing the previous tree items
		if(treeview.getRoot().getChildren().size()>0){
			treeview.getRoot().getChildren().clear();
		}
		
		// Retrieve the list of folder
		ObservableList<FolderBean> folders = emailDAO.findAllFolders();
		// Build an item for each fish and add it to the root
		if (folders != null) {
			for (FolderBean fb : folders) {
				item = new TreeItem<>(fb);
				item.setGraphic(new ImageView(getClass().getResource(
						"/images/folder1.png").toExternalForm()));
				
				treeview.getRoot().getChildren().add(item);
			}
		}
	
		mi.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		    	try {
					emailDAO.deleteFolder(treeview.getSelectionModel().getSelectedItem().getValue().getFolderID());
				    TreeItem ti = (TreeItem)treeview.getSelectionModel().getSelectedItem();
		            boolean removed = ti.getParent().getChildren().remove(ti);
		            log.info("Tree item "+ti+" has been removed: " + removed);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		    }
		});
        
		cm.getItems().add(mi);
         
		item.addEventHandler(MouseEvent.MOUSE_CLICKED,
		    new EventHandler<MouseEvent>() {
		        @Override public void handle(MouseEvent e) {
		            if (e.getButton() == MouseButton.SECONDARY) 
		            	cm.show(treeview, e.getScreenX(), e.getScreenY());	            	
		        }
		});
		
		treeview.setContextMenu(cm);

		// Open the tree
		treeview.getRoot().setExpanded(true);

		// Listen for selection changes and show details when
		// changed.
		treeview.getSelectionModel()
				.selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showFolderDetailsTree(newValue));
	}

	/**
	 * Using the reference to the MailTableController it can change the
	 * selected row in the TableView. It also displays the MailBean object that
	 * corresponds to the selected node.
	 * 
	 * @param folders
	 */
	private void showFolderDetailsTree(TreeItem<FolderBean> fb) {
		try{
			// Select the folder that contains the mailbean objects from the Tree
			tableController.displayTheTable(fb.getValue().getFolderID());
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
}

