/**
 * 
 */
package com.kfcstd.jag_phase3.javafxTree.view;

import java.sql.SQLException;

import com.kfcstd.jag_phase3.beans.FolderBean;
import com.kfcstd.jag_phase3.persistence.EmailDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Zheng Hua Zhu
 *
 */
public class FolderController {
    
	@FXML
    private AnchorPane NewFolderPane;

    @FXML
    private Button add;

    @FXML
    private TextField newfoldername;

    private EmailDAO emailDAO;
    private TreeLayoutController treeController;
    
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
	 * This method is to add a new folder
	 * @param event
	 */
    @FXML
    public void addFolder(ActionEvent event) {
    	FolderBean fb = new FolderBean(-1, newfoldername.getText());
		try{
			emailDAO.create(fb);
			treeController.displayTree();
		}catch (SQLException e){
			e.printStackTrace();
		}	

    }

    /**
     * pass in the reference to the controller
     * @param treeController
     */
	public void setTreeController(TreeLayoutController treeController) {
		this.treeController=treeController;
	}

}
