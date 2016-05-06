/**
 * 
 */
package com.kfcstd.jag_phase3.persistence;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.kfcstd.jag_phase3.beans.AttachmentBean;
import com.kfcstd.jag_phase3.beans.FolderBean;
import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.beans.Mail_Recipient_Bean;
import com.kfcstd.jag_phase3.beans.RecipientBean;

import javafx.collections.ObservableList;


/**
 * @author Zheng Hua Zhu
 *
 */
public interface EmailDAO {
	
	// Create 
	// create RecipientBean, AttachmentBean & Mail_Recipient_Bean as well
    public int create(MailBean mail) throws SQLException; 
    
    public int create(FolderBean folder) throws SQLException;
    
    // Read
	public ObservableList<MailBean> findAllMails() throws SQLException;
	public ObservableList<FolderBean> findAllFolders() throws SQLException;
	public ArrayList<MailBean> findByFrom(String from) throws SQLException;
	public ArrayList<MailBean> findByTo(String to) throws SQLException;
	public ArrayList<MailBean> findByCc(String cc) throws SQLException;
	public ObservableList<MailBean> findByFolderID(int id) throws SQLException;
	public ArrayList<MailBean> findByBcc(String bcc) throws SQLException;
	public ArrayList<MailBean> findBySubject(String subject) throws SQLException;
	public ArrayList<MailBean> findByDateSent(LocalDateTime date) throws SQLException;
	public ArrayList<MailBean> findByDateReceived(LocalDateTime date) throws SQLException;
	public MailBean findByID(int id) throws SQLException;
	
	// methods established to check mail-related contents 
	public RecipientBean findRecipientByID(int id) throws SQLException;
	public Mail_Recipient_Bean findBridgeByID(int id) throws SQLException;
	public FolderBean findFolderByID(int id) throws SQLException;
	public AttachmentBean findAttachmentByID(int id) throws SQLException;
	
	
	// Update
	// update RecipientBean, AttachmentBean & Mail_Recipient_Bean as well
	public int update(MailBean mail) throws SQLException;
	
	public int update(FolderBean folder) throws SQLException;
	
    // Delete a mail record, which is related to the deletion in Mail table, Recipient table, 
	// Attachment table & Mail_Recipient_Bean table 
    public int delete(int ID) throws SQLException;
    
    public int deleteFolder(int ID) throws SQLException;
  
}
