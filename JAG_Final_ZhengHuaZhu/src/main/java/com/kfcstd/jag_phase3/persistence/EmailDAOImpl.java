/**
 * 
 */
package com.kfcstd.jag_phase3.persistence;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.kfcstd.jag_phase3.beans.AttachmentBean;
import com.kfcstd.jag_phase3.beans.FolderBean;
import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.beans.Mail_Recipient_Bean;
import com.kfcstd.jag_phase3.beans.RecipientBean;
import com.kfcstd.jag_phase3.properties_manager.PropertiesManager;
import com.kfcstd.jag_phase3.beans.PropertiesBean;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * This class implements the EMailDAO interface
 * 
 * Exceptions are possible whenever a JDBC object is used. When these exceptions
 * occur it should result in some message appearing to the user and possibly
 * some corrective action. To simplify this, all exceptions are thrown and not
 * caught here. The methods that you write to call any of these methods must
 * either use a try/catch or continue throwing the exception.
 * 
 * by: Zheng Hua Zhu
 * 
 */
public class EmailDAOImpl implements EmailDAO{
	
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());
	
	private PropertiesManager pm = new PropertiesManager();
	private PropertiesBean pb = new PropertiesBean();
	
	//private String filename="jarDbConnection.properties"; // properties file for connecting to the DB
	private String filename="Setting.properties"; // properties file for connecting to the DB
	
	public EmailDAOImpl() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ObservableList<MailBean> findByFolderID(int id) throws SQLException {
		ObservableList<MailBean> rows = FXCollections.observableArrayList();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL WHERE FOLDERID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setInt(1, id);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found in FOLDERID: " + rows.size());
		return rows;
	}
	
	/**
	 * @param mail a mail message to be saved to the mail table
	 * 
	 * @return 0 indicates failure, 1 indicates successful saving
	 */
	@Override
	public int create(MailBean mail) throws SQLException {
		int result = 0;
		int result2= 0;
		String query = "INSERT INTO MAIL (FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS) VALUES (?,?,?,?,?,?,?,?)";
        
		try{
        	pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, mail.getFromField());
			ps.setString(2, mail.getSubjectField());
			ps.setString(3, mail.getTextField());
			ps.setString(4, mail.getHtmlField());
			ps.setInt(5, mail.getFolderID());
			ps.setTimestamp(6, Timestamp.valueOf(mail.getSentTime()));
			ps.setTimestamp(7, Timestamp.valueOf(mail.getReceivedTime()));
			ps.setInt(8, mail.getMailStatus());
					
			result = ps.executeUpdate();
			try(ResultSet rs=ps.getGeneratedKeys();){
				if(rs.next()){
					//log.info("MailID is: "+rs.getInt(1));	
					createRecipient(mail, rs.getInt(1));
					createAttachment(mail, rs.getInt(1));
					result2=rs.getInt(1); // the auto-generated mailid
				}
			}
		}
		// info showing both how many row has been created and the maildID of the last record
		log.info("# of records created : " + result + ", its MAILID is " + result2);  
		return result;
	}

	/**
	 * @param folder a folder to be saved to the folder table
	 * 
	 * @return 0 indicates failure, 1 indicates successful saving
	 */
	@Override
	public int create(FolderBean folder) throws SQLException {
		int result = 0;
		String query = "INSERT INTO FOLDER (NAME) VALUES (?)";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query);) {
			
			ps.setString(1, folder.getName());
					
			result = ps.executeUpdate();
		}
		log.info("Mail # of records created in CREATEFOLDER: " + result);
		return result;
	}

	/**
	 * 
	 * @return a list of all mails in the DB
	 */
	@Override
	public ObservableList<MailBean> findAllMails() throws SQLException {
		ObservableList<MailBean> rows = FXCollections
                .observableArrayList();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query);	
				ResultSet resultSet = ps.executeQuery()) {		
			while (resultSet.next()) {
				MailBean mail = new MailBean();
				mail.setMailID(resultSet.getInt("MAILID"));
				mail.setFromField(resultSet.getString("FROMFIELD"));
				mail.setSubjectField(resultSet.getString("SUBJECT"));
				mail.setTextField(resultSet.getString("TEXT"));
				mail.setHtmlField(resultSet.getString("HTML"));
				mail.setFolderID(resultSet.getInt("FOLDERID"));
				mail.setMailStatus(resultSet.getInt("MAILSTATUS"));
				mail.setSentTime(resultSet.getTimestamp("SENTTIME").toLocalDateTime());
				mail.setReceivedTime(resultSet.getTimestamp("RECEIVEDTIME").toLocalDateTime());
				findAllRecipients(mail);
				findAllAttachments(mail);
				rows.add(mail);
			}
		}
		log.info("Mail # of records found by ALL: " + rows.size());
		return rows;
	}
	
	

	/**
	 * @param from find all mails according to the from
	 * 
	 * @return a list of matching mails
	 */
	@Override
	public ArrayList<MailBean> findByFrom(String from) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL WHERE FROMFIELD = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setString(1, from);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found by FROM: " + rows.size());
		return rows;
	}

	/**
	 * @param to find all mails according to the to
	 * 
	 * @return a list of matching mails
	 */
	@Override
	public ArrayList<MailBean> findByTo(String to) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL JOIN MAIL_RECIPIENT USING (MAILID)"
				+ " JOIN RECIPIENT USING (RECIPIENTID) WHERE TOFIELD = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setString(1, to);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found in TO: " + rows.size());
		return rows;
	}

	/**
	 * @param cc find all mails according to the cc
	 * 
	 * @return a list of matching mails
	 */
	@Override
	public ArrayList<MailBean> findByCc(String cc) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL JOIN MAIL_RECIPIENT USING (MAILID)"
				+ " JOIN RECIPIENT USING (RECIPIENTID) WHERE CC = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setString(1, cc);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found in CC: " + rows.size());
		return rows;
	}

	/**
	 * @param bcc find all mails according to the bcc
	 * 
	 * @return a list of matching mails
	 */
	@Override
	public ArrayList<MailBean> findByBcc(String bcc) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL JOIN MAIL_RECIPIENT USING (MAILID)"
				+ " JOIN RECIPIENT USING (RECIPIENTID) WHERE BCC = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setString(1, bcc);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found in BCC: " + rows.size());
		return rows;
	}

	/**
	 * @param subject find all mails according to the subject
	 * 
	 * @return a list of matching mails
	 */
	@Override
	public ArrayList<MailBean> findBySubject(String subject) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL WHERE SUBJECT = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setString(1, subject);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found by SUBJECT: " + rows.size());
		return rows;
	}

	/**
	 * @param date find all mails according to the sent time
	 * 
	 * @return a list of matching mails
	 */
	@Override
	public ArrayList<MailBean> findByDateSent(LocalDateTime date) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL WHERE SENTTIME = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setTimestamp(1, Timestamp.valueOf(date));
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found by SENTTIME: " + rows.size());
		return rows;
	}

	/**
	 * @param date find all mails according to the received time
	 * 
	 * @return a list of matching mails
	 */
	@Override
	public ArrayList<MailBean> findByDateReceived(LocalDateTime date) throws SQLException {
		ArrayList<MailBean> rows = new ArrayList<>();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL WHERE RECEIVEDTIME = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setTimestamp(1, Timestamp.valueOf(date));
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					rows.add(createMail(resultSet));
				}
			}
		}
		log.info("Mail # of records found by RECEIVEDTIME: " + rows.size());
		return rows;
	}

	/**
	 * @param id find the mail according to the id
	 * 
	 * @return a matching mail
	 */
	@Override
	public MailBean findByID(int id) throws SQLException {
		MailBean mail = new MailBean();

		String query = "SELECT MAILID, FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS FROM MAIL WHERE MAILID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setInt(1, id);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					mail=createMail(resultSet);
				}else{
					mail=null;
				}
			}
		}
		log.info("One mail record found by ID is (true/false): " + (mail != null));
		return mail;
	}
	
	/**
	 * @param id find the recipient according to the id
	 * 
	 * @return a matching recipient record
	 *
	 */
	@Override
	public RecipientBean findRecipientByID(int id) throws SQLException {
		RecipientBean r = new RecipientBean();

		String query = "SELECT RECIPIENTID, TOFIELD, CC, BCC FROM RECIPIENT WHERE RECIPIENTID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setInt(1, id);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					r.setRecipientID(resultSet.getInt("RECIPIENTID"));
					r.setTo(resultSet.getString("TOFIELD"));
					r.setCc(resultSet.getString("CC"));
					r.setBcc(resultSet.getString("BCC"));	
				}else{
					r=null;
				}
			}
		}
		log.info("One recipient record found by ID is (true/false): " + (r != null));
		return r;
	}

	/**
	 * @param id find the record in the bridge table according to the id
	 * 
	 * @return a matching bridge record
	 * 
	 */
	@Override
	public Mail_Recipient_Bean findBridgeByID(int id) throws SQLException {
		Mail_Recipient_Bean mr = new Mail_Recipient_Bean();

		String query = "SELECT ID, MAILID, RECIPIENTID FROM MAIL_RECIPIENT WHERE ID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setInt(1, id);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					mr.setId(resultSet.getInt("ID"));
					mr.setMailID(resultSet.getInt("MAILID"));
					mr.setRecipientID(resultSet.getInt("RECIPIENTID"));	
				}else{
					mr=null;
				}
			}
		}
		log.info("One mail_recipient record found by ID is (true/false): " + (mr != null));
		return mr;
	}
	
    /**
     * @param id find the record in the folder table according to the id
	 * 
	 * @return a matching folder record
     * 
     */
	@Override
	public FolderBean findFolderByID(int id) throws SQLException {
		FolderBean fb = new FolderBean();

		String query = "SELECT FOLDERID, NAME FROM FOLDER WHERE FOLDERID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setInt(1, id);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					fb.setFolderID(resultSet.getInt("FOLDERID"));
					fb.setName(resultSet.getString("NAME"));
				}else{
					fb=null;
				}
			}
		}
		log.info("One folder record found by ID is (true/false): " + (fb != null));
		return fb;
	}

	/**
	 * @param id find the record in the attachment table according to the id
	 * 
	 * @return a matching attachment record
	 * 
	 */
	@Override
	public AttachmentBean findAttachmentByID(int id) throws SQLException {
		AttachmentBean ab = new AttachmentBean();

		String query = "SELECT ATTACHMENTID, NAME, CID, SIZE, CONTENT FROM ATTACHMENT WHERE ATTACHMENTID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
				//ResultSet resultSet = ps.executeQuery()) {
			
			ps.setInt(1, id);
			// A new try-with-resources block begins for creating the ResultSet
			// object
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					ab.setAttachmentID(resultSet.getInt("ATTACHMENTID"));
					ab.setName(resultSet.getString("NAME"));
					ab.setCID(resultSet.getString("CID"));
					ab.setSize(resultSet.getInt("SIZE"));
					ab.setImage(resultSet.getBytes("CONTENT"));
				}else{
					ab=null;
				}
			}
		}
		log.info("One attachment record found by ID is (true/false): " + (ab != null));
		return ab;
	}


	/**
	 * This method will update the folderID of a mail record. 
	 * 
	 * @param mail
	 *            An object with an existing ID and new data in the fields
	 * @return The number of records updated, should be 0 or 1
	 * @throws SQLException
	 * 
	 */
	@Override
	public int update(MailBean mail) throws SQLException {
		int result = 0;

		String query = "UPDATE MAIL SET FOLDERID=? WHERE MAILID = ?";

		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
		
			ps.setInt(1, mail.getFolderID());
			ps.setInt(2, mail.getMailID());
			result = ps.executeUpdate();
		}
		log.info("# of records updated by UPDATEMAIL: " + result);
		return result;
	}

	/**
	 * This method will update the name field of a folder record. 
	 * 
	 * @param mail
	 *            An object with an existing ID and new data in the fields
	 * @return The number of records updated, should be 0 or 1
	 * @throws SQLException
	 * 
	 */
	@Override
	public int update(FolderBean folder) throws SQLException {
		int result = 0;

		String query = "UPDATE FOLDER SET NAME=? WHERE FOLDERID = ?";

		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query)){
		
			ps.setString(1, folder.getName());	
			ps.setInt(2, folder.getFolderID());
			result = ps.executeUpdate();
		}
		log.info("# of records updated by PUBLIC UPDATEFOLDER: " + result);
		return result;
	}

	/**
	 * This method deletes a single record based on the primary key field ID value. 
	 * It should return either 0 meaning that there is no
	 * record with that ID or 1 meaning a single record has been deleted. If the
	 * value is greater than 1 then something unexpected has happened. A
	 * criteria other than ID may delete more than one record.
	 * 
	 * @param ID
	 *            The primary key to use to identify the record that must be
	 *            deleted
	 * @return The number of records deleted, should be 0 or 1
	 * @throws SQLException
	 */
	@Override
	public int delete(int ID) throws SQLException {
		int result = 0;
		
		MailBean mail= findByID(ID);
		ArrayList<Integer> IDs=findAllRecipients(mail);	

		String query = "DELETE FROM MAIL WHERE MAILID = ?";

		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query);) {
			
			ps.setInt(1, ID);
			result = ps.executeUpdate();
			deleteRecipients(IDs);
		}
		log.info("# of records deleted : " + result);
		return result;
	}
	
	private void deleteRecipients(ArrayList<Integer> iDs) throws SQLException {
		
		int result=0;
		
		String query = "DELETE FROM RECIPIENT WHERE RECIPIENTID = ?";

		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query);) {
			for(int each : iDs){
				ps.setInt(1, each);
				result += ps.executeUpdate();
			}
		}
		log.info("# of records deleted in the recipient table: " + result);
		
	}

	/**
	 * This method deletes a folder record based on its primary key field ID value. 
	 * It should return either 0 meaning that there is no
	 * record with that ID or 1 meaning a single record has been deleted. If the
	 * value is greater than 1 then something unexpected has happened. A
	 * criteria other than ID may delete more than one record.
	 * 
	 * @param ID
	 *            The primary key to use to identify the record that must be
	 *            deleted
	 * @return The number of records deleted, should be 0 or 1
	 * @throws SQLException
	 */
	@Override
	public int deleteFolder(int ID) throws SQLException {
		int result = 0;

		String query = "DELETE FROM FOLDER WHERE FOLDERID = ?";

		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query);) {
			
			ps.setInt(1, ID);
			result = ps.executeUpdate();
		}
		log.info("# of records deleted in DELETEFOLDER: " + result);
		return result;
	}
	
	private ArrayList<Integer> findAllRecipients(MailBean mail)throws SQLException {
		ArrayList<Integer> IDs=new ArrayList<Integer>();
		String query = "SELECT RECIPIENTID, TOFIELD, CC, BCC FROM RECIPIENT "
				+ "JOIN MAIL_RECIPIENT USING (RECIPIENTID) WHERE MAILID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query) ){
					ps.setInt(1, mail.getMailID());	

			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					IDs.add(resultSet.getInt("RECIPIENTID"));
					if(resultSet.getString("TOFIELD")!=null)
						mail.getToField().add(resultSet.getString("TOFIELD"));					
					if(resultSet.getString("CC")!=null)
						mail.getCcField().add(resultSet.getString("CC"));
					if(resultSet.getString("BCC")!=null)
						mail.getbCCField().add(resultSet.getString("BCC"));
				}
			}
		}
		log.info("Recipients # of records found : " + " [TOFIELD: "+ mail.getToField().size() + 
		"] " + " [CC: " + mail.getCcField().size() + "]" + " [BCC: " + mail.getbCCField().size() + "]");
		return IDs;
	}
	
	
	private void findAllAttachments(MailBean mail) throws SQLException {
		
		String query = "SELECT ATTACHMENTID, MAILID, NAME, CID, SIZE, CONTENT FROM ATTACHMENT WHERE MAILID = ?";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query) ){
					ps.setInt(1, mail.getMailID());	

			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					//EmailAttachmentBuilder eab = EmailAttachment.attachment().file(resultSet.getString("NAME"));
					//EmailAttachment ea=eab.create();
					if(resultSet.getString("CID")!=null){
						log.info("CID is: "+resultSet.getString("CID"));
						log.info("ENAME is: "+resultSet.getString("NAME"));
						EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File(resultSet.getString("NAME")));
						eab.setInline(resultSet.getString("CID"));
						EmailAttachment ea=eab.create();
						mail.getEmbeddedAttField().add(ea);	
					}else{
						log.info("RNAME is: "+resultSet.getString("NAME"));
						EmailAttachmentBuilder eab = EmailAttachment.attachment().file(resultSet.getString("NAME"));
						EmailAttachment ea=eab.create();
						mail.getAttachmentField().add(ea);	
					}
				}
			}
		}
		log.info("Attachments # of records found : " + " [Attachments: "+ mail.getAttachmentField().size() + "]" + 
		" [Embedded Attachments: " + mail.getEmbeddedAttField().size() + "]");	
	}
	
	private int createRecipient(MailBean mail, int mailid) throws SQLException {
		int result = 0;
		String query = "INSERT INTO RECIPIENT (TOFIELD, CC, BCC) VALUES (?, ?, ?)";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			
			int lt=mail.getToField().size();
			int lc=mail.getCcField().size();
			int lbc=mail.getbCCField().size();
			
			for(int i=0; i<lt || i<lc || i<lbc; i++){
				if(i<lt){
					ps.setString(1, mail.getToField().get(i));
				}else{
					ps.setString(1, "");
				}

				if(i<lc){
					ps.setString(2, mail.getCcField().get(i));
				}else{
					ps.setString(2, "");
				}

				if(i<lbc){
					ps.setString(3, mail.getbCCField().get(i));		
				}else{
					ps.setString(3, "");
				}
				
				result+=ps.executeUpdate();
				
				ResultSet rs=ps.getGeneratedKeys();
				
				if(rs.next())
					createMail_Recipient(mailid, rs.getInt(1));
			}		
			//result = ps.executeUpdate();						
		}
		log.info("# of records in [table Recipient] created : " + result);
		return result;
	}
	
	private int createMail_Recipient(int mailid, int recipientid) throws SQLException {
		int result = 0;
		String query = "INSERT INTO MAIL_RECIPIENT (MAILID, RECIPIENTID) VALUES (?, ?)";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query);) {
			ps.setInt(1, mailid);
			ps.setInt(2, recipientid);
			
			result = ps.executeUpdate();
		}
		log.info("# of records in [table Mail_Recipient] created : " + result);
		return result;
	}
	
	private int createAttachment(MailBean mail, int mailid) throws SQLException {
		int result = 0;
		String query = "INSERT INTO ATTACHMENT (MAILID, NAME, CID, SIZE, CONTENT) VALUES (?, ?, ?, ?, ?)";
		
		try{
			pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		// Connection is only open for the operation and then immediately closed
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
				// Using a prepared statement to handle the conversion
				// of special characters in the SQL statement and guard against SQL Injection
				PreparedStatement ps = connection.prepareStatement(query);) {
			
			int la=mail.getAttachmentField().size();
			int le=mail.getEmbeddedAttField().size();
			
			if(la>0){
				for(EmailAttachment each : mail.getAttachmentField()){
					ps.setInt(1, mailid);
					ps.setString(2, each.getName());
					ps.setString(3, null);
					ps.setInt(4, each.getSize());
					ps.setBytes(5, each.toByteArray());
					
					result += ps.executeUpdate();
				}
			}
			
			if(le>0){
				for(EmailAttachment each : mail.getEmbeddedAttField()){
					ps.setInt(1, mailid);
					ps.setString(2, each.getName());
					ps.setString(3, each.getContentId());
					ps.setInt(4, each.getSize());
					ps.setBytes(5, each.toByteArray());
					
					result += ps.executeUpdate();
				}
			}
			
		}
		log.info("# of records in [table Attachmnet] created : " + result);
		return result;
	}
	
	/**
	 * Private method that creates a MailBean object from the current
	 * record in the ResultSet
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private MailBean createMail(ResultSet resultSet) throws SQLException {
		MailBean mail = new MailBean();
		mail.setMailID(resultSet.getInt("MAILID"));
		mail.setFromField(resultSet.getString("FROMFIELD"));
		mail.setSubjectField(resultSet.getString("SUBJECT"));
		mail.setTextField(resultSet.getString("TEXT"));
		mail.setHtmlField(resultSet.getString("HTML"));
		mail.setFolderID(resultSet.getInt("FOLDERID"));
		mail.setSentTime(resultSet.getTimestamp("SENTTIME").toLocalDateTime());
		mail.setReceivedTime(resultSet.getTimestamp("RECEIVEDTIME").toLocalDateTime());
		mail.setMailStatus(resultSet.getInt("MAILSTATUS"));
		findAllRecipients(mail);
		findAllAttachments(mail);
		return mail;
	}

	/**
	 * 
	 * @return a list of all folders in the DB
	 * @throws SQLException
	 */
	@Override
	public ObservableList<FolderBean> findAllFolders() throws SQLException {

		 ObservableList<FolderBean> rows = FXCollections
	                .observableArrayList();

			String query = "SELECT FOLDERID, NAME FROM FOLDER";
			
			try{
				pb = pm.loadTextProperties("", filename);
	        }catch(IOException ioe){
				log.error("Error: " + ioe.getMessage());
			}catch(NullPointerException npe){
				log.error("Error: " + npe.getMessage());
			}
			
			// Connection is only open for the operation and then immediately closed
			try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+pb.getDatabasename(), pb.getUser(), pb.getPassword());
					// Using a prepared statement to handle the conversion
					// of special characters in the SQL statement and guard against SQL Injection
					PreparedStatement ps = connection.prepareStatement(query);	
					ResultSet resultSet = ps.executeQuery()) {		
				while (resultSet.next()) {
					FolderBean fb = new FolderBean();
					fb.setFolderID(resultSet.getInt("FOLDERID"));
					fb.setName(resultSet.getString("NAME"));
					rows.add(fb);
				}
			}
			log.info("Folder # of records found by ALL: " + rows.size());
			return rows;
		}

}
