/**
 * 
 */
package com.kfcstd.jag_phase3.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.beans.PropertiesBean;
import com.kfcstd.jag_phase3.properties_manager.PropertiesManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.kfcstd.jag_phase3.beans.FolderBean;
import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.persistence.EmailDAO;
import com.kfcstd.jag_phase3.persistence.EmailDAOImpl;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * This class tests all the public methods that create/read/update/delete tables of maildb database
 * 
 * Exceptions are possible whenever a JDBC object is used. When these exceptions
 * occur it should result in some message appearing to the user and possibly
 * some corrective action. To simplify this, all exceptions are thrown and not
 * caught here. The methods that you write to call any of these methods must
 * either use a try/catch or continue throwing the exception.
 * 
 * @author Zheng Hua Zhu
 *
 */
public class DAOActionTest {
	
	//private String filename="jarClientConnection.properties"; // DB connection credential from a properties file
	private String filename="Setting.properties"; // DB connection credential from a properties file
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());
	
	private PropertiesManager pm = new PropertiesManager();
	private PropertiesBean pb = new PropertiesBean();
/*	
	private String url="jdbc:mysql://localhost:3306/";
	private String user="root";
	private String password="";
*/
	
	/**
	 * test1 how many rows in a known database
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindAllMails() throws SQLException {
		EmailDAO ed = new EmailDAOImpl();
		ObservableList<MailBean> mails = FXCollections
                .observableArrayList();
		mails = ed.findAllMails();
		log.info("Test case 01: ");
		assertEquals("TEST_01__testFindAllMails() failed. ", 5, mails.size());
	}

	/**
	 * test2 retrieve a mail record by its id and comparing it with the original data
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByID_1() throws SQLException {

		LocalDateTime ldts=LocalDateTime.of(2014, 7, 14, 9, 0, 0);
		LocalDateTime ldtr=LocalDateTime.of(2014, 7, 15, 13, 0, 0);
		
		MailBean mail = new MailBean(6,"zzh.517.00001@gmail.com", "Tony", "Appendicitis", null, 2, ldts, ldtr, 0);
		mail.getToField().add("zzh.517.00002@gmail.com");
		mail.getToField().add("zzh.517.00002@gmail.com");
		mail.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mail.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mail.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		mail.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		eab.setInline("CID"); // designate CID to an embedded attachment
		EmailAttachment ea1 = eab.create();
		mail.getEmbeddedAttField().add(ea1); 
		eab = EmailAttachment.attachment().file("p1.JPG");
		EmailAttachment ea2 = eab.create();
		mail.getAttachmentField().add(ea2);
		
		EmailDAO ed = new EmailDAOImpl();
		ed.create(mail); // test the create()
		
		
		log.info("Test case 02_1: ");
		MailBean mail2 = ed.findByID(6);
		assertEquals("TEST_02__01_testfindByID failed. ", mail, mail2); // check if the retrieved record is the same with the one which is saved to the DB
	}
	
	/**
	 * test3 check if the create method is successful
	 * test3 check if it has a default mailID(-1) if findByID is given a nonexisting mailID 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByID_2() throws SQLException {
		LocalDateTime ldts=LocalDateTime.of(2014, 07, 14, 9, 00, 00);
		LocalDateTime ldtr=LocalDateTime.of(2014, 07, 15, 13, 00, 00);
		
		MailBean mail = new MailBean(6,"zzh.517.00001@gmail.com", "Tony", "Appendicitis", null, 2, ldts, ldtr, 0);
		mail.getToField().add("zzh.517.00002@gmail.com");
		mail.getToField().add("zzh.517.00002@gmail.com");
		mail.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mail.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mail.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		mail.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		eab.setInline("CID"); // designate CID to an embedded attachment
		EmailAttachment ea1 = eab.create();
		mail.getEmbeddedAttField().add(ea1); 
		eab = EmailAttachment.attachment().file("p1.JPG");
		EmailAttachment ea2 = eab.create();
		mail.getAttachmentField().add(ea2);
		
		EmailDAO ed = new EmailDAOImpl();
		int result=ed.create(mail); // test the create()
		log.info("Test case 02_2: ");
		assertEquals("TEST_02__02_testCreateMail failed. ", 1, result); // check if create(mail) is successful
	}
	
	/**
	 * test4 check if it has a default mailID(-1) if findByID is given a nonexisting mailID 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByID_3() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();

		MailBean mail3 = ed.findByID(10);
		log.info("Test case 02_3: ");
		assertEquals("TEST_02__03_testfindByID() failed. ", null, mail3); // test a wrong id will return null from findByID()
	}
	
	/**
	 * test5 to create a folder and test its return value
	 * 0 means wrong, 1 means successful
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testCreateFolder() throws SQLException {
	
		FolderBean fb=new FolderBean(3, "draft");

		EmailDAO ed = new EmailDAOImpl();
		int result=ed.create(fb);	
	
		log.info("Test case 03_1: ");
		assertEquals("TEST_03__1 testCreateFolder() failed. ", 1, result); 
	}
	
	/**
	 * test6 to create a folder and retrieve it to compare with the original data
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testCreateFolder_2() throws SQLException {
	
		FolderBean fb=new FolderBean(3, "draft");

		EmailDAO ed = new EmailDAOImpl();
		ed.create(fb);	
		FolderBean fb2=ed.findFolderByID(3);
	
		log.info("Test case 03_2: ");
		assertEquals("TEST_03__2 testCreateFolder() failed. ", fb2, fb); 
	}
	
	/**
	 * test7 find records by its fromField
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByFrom() throws SQLException {
	
		String fromField="zzh.517.00001@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByFrom(fromField);	
	
		log.info("Test case 04_1: ");
		assertEquals("TEST_04__01_testfindByFrom() failed. ", 5, mails.size());	
	}
	
	/**
	 * test8 check if returned mails each has a matching from 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByFrom_2() throws SQLException {
	
		String fromField="zzh.517.00001@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByFrom(fromField);	
	
		log.info("Test case 04_2: ");
		
		// check if returned mails each has a from field matches the query
		for(MailBean mail : mails){
			assertEquals("TEST_04__02_testfindByFrom() failed. ", mail.getFromField(), "zzh.517.00001@gmail.com");
		}		
	}
	
	/**
	 * test9 test the return with a nonexisting from
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByFrom_3() throws SQLException {
	
		String fromField="zzh.517@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();		
		EmailDAO ed = new EmailDAOImpl();
	
		log.info("Test case 04_3: ");
		mails=ed.findByFrom(fromField);
		assertEquals("TEST_04__03_testfindByFrom() failed. ", 0, mails.size()); // test by giving a nonexisting from		
	}
	
	/**
	 * test10 find records by its subjectField
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindBySubject() throws SQLException {
	
		String subjectField="Barry";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findBySubject(subjectField);	
		log.info("Test case 05_1: ");
		assertEquals("TEST_05__01_testfindBySubject() failed. ", 1, mails.size());	
	}
	
	/**
	 * test11 check if returned mails each has a matching subject 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindBySubject_2() throws SQLException {
	
		String subjectField="Barry";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findBySubject(subjectField);	
		
		log.info("Test case 05_2: ");
		
		for(MailBean mail : mails){
			assertEquals("TEST_05__02_testfindBySubject() failed. ", mail.getSubjectField(), "Barry");
		}			
	}
	
	/**
	 * test12 test the return with a nonexisting subject
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindBySubject_3() throws SQLException {
	
		String subjectField="go shopping";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findBySubject(subjectField);	
		
		log.info("Test case 05_3: ");

		assertEquals("TEST_05__03_testfindBySubject() failed. ", 0, mails.size()); // test by giving a nonexisting subject		
	}
	
	/**
	 * test13 find records by its toField
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByTo() throws SQLException {
		
		String toField="zzh.517.00002@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByTo(toField);	
		log.info("Test case 06_1: ");
		assertEquals("TEST_06__01_testfindByTo() failed. ", 3, mails.size());			
	}
	
	/**
	 * test14 check if returned mails each has a matching to 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByTo_2() throws SQLException {
	
		boolean foundTo=false;
		
		String toField="zzh.517.00002@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByTo(toField);	
		
		log.info("Test case 06_2: ");
		
		for(MailBean mail : mails){
			foundTo=false;
			for(String to : mail.getToField())
				if(to.equals(toField))
					foundTo=true;
		}
		
		assertEquals("TEST_06__02_testfindByTo() failed. ", true, foundTo);				
	}
	
	/**
	 * test15 test the return with a nonexisting to
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByTo_3() throws SQLException {
		
		String toField="zzh@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByTo(toField);	
		
		log.info("Test case 06_3: ");

		assertEquals("TEST_06__03_testfindByTo() failed. ", 0, mails.size()); // test by giving a nonexisting to				
	}
	
	/**
	 * test16 find records by its ccField
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByCC() throws SQLException {
	
		String ccField="zzh.517.cctestacct@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByCc(ccField);	
		log.info("Test case 07_1: ");
		assertEquals("TEST_07__01_testfindByCC() failed. ", 2, mails.size());			
	}
	
	/**
	 * test17 check if returned mails each has a matching cc 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByCC_2() throws SQLException {
	    boolean foundCC=false;
		String ccField="zzh.517.cctestacct@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByCc(ccField);	
		log.info("Test case 07_2: ");
		
		for(MailBean mail : mails){
			foundCC=false;
			for(String cc : mail.getCcField())
				if(cc.equals(ccField))
					foundCC=true;
		}
		
		assertEquals("TEST_07__02_testfindByTo() failed. ", true, foundCC);
	}
	
	/**
	 * test18 test the return with a nonexisting cc
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByCC_3() throws SQLException {
	   
		String ccField="zzh.715@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByCc(ccField);	
		
		log.info("Test case 07_3: ");		
		assertEquals("TEST_07__03_testfindByCC() failed. ", 0, mails.size()); // test by giving a nonexisting cc			
	}
	
	/**
	 * test19 find records by its bccField
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByBCC() throws SQLException {

		String bccField="zzh.517.bcctestacct2@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByBcc(bccField);	
		log.info("Test case 08_1: ");
		assertEquals("TEST_08__01_testfindByBCC() failed. ", 2, mails.size());	
	}
	
	/**
	 * test20 check if returned mails each has a matching bcc 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByBCC_2() throws SQLException {
	
		boolean foundBCC=false;
		String bccField="zzh.517.bcctestacct2@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByBcc(bccField);	
		
		log.info("Test case 08_2: ");
		
		for(MailBean mail : mails){
			foundBCC=false;
			for(String bcc : mail.getbCCField())
				if(bcc.equals(bccField))
					foundBCC=true;
		}
		
		assertEquals("TEST_08__02_testfindByBCC() failed. ", true, foundBCC);
	}
	
	/**
	 * test21 test the return with a nonexisting bcc
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByBCC_3() throws SQLException {
		String bccField="zzh.51799@gmail.com";
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByBcc(bccField);	
		
		log.info("Test case 08_3: ");
		assertEquals("TEST_08__03_testfindByFrom() failed. ", 0, mails.size()); // test by giving a nonexisting bcc		
	}
	
	/**
	 * test22 find records by its sent time
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByDateSent() throws SQLException {
	
		LocalDateTime sent=LocalDateTime.of(2014, 01, 23, 9, 00, 00);
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByDateSent(sent);	
		log.info("Test case 09_1: ");
		assertEquals("TEST_09__01_testfindByDateSent() failed. ", 1, mails.size());
	}
	
	/**
	 * test23 check if returned mails each has a matching sent time 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByDateSent_2() throws SQLException {
	
		LocalDateTime sent=LocalDateTime.of(2014, 01, 23, 9, 00, 00);
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByDateSent(sent);	
		log.info("Test case 09_2: ");
				
		for(MailBean mail : mails){
			assertEquals("TEST_09__02_testfindByDateSent() failed. ", mail.getSentTime(), sent);
		}
	}
	
	/**
	 * test24 test the return with a nonexisting sent time
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByDateSent_3() throws SQLException {
	
		LocalDateTime sent=LocalDateTime.of(2012, 01, 20, 19, 10, 00);
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByDateSent(sent);	
		log.info("Test case 09_3: ");
		assertEquals("TEST_09__03_testfindByDateSent() failed. ", 0, mails.size()); // test by giving a nonexisting sent time
	}
	
	/**
	 * test25 find records by its received time
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByDateReceived() throws SQLException {
	
		LocalDateTime rev=LocalDateTime.of(2014, 02, 21, 18, 00, 00);
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByDateReceived(rev);	
		log.info("Test case 10_1: ");
		assertEquals("TEST_10__01_testfindByDateReceived() failed. ", 1, mails.size());	
	}
	
	/**
	 * test26 check if returned mails each has a matching received time 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByDateReceived_2() throws SQLException {
	
		LocalDateTime rev=LocalDateTime.of(2014, 02, 21, 13, 00, 00);
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByDateReceived(rev);	
		log.info("Test case 10_2: ");
		
		for(MailBean mail : mails){
			assertEquals("TEST_10__02_testfindByDateReceived() failed. ", mail.getReceivedTime(), rev);
		}	
	}
	
	/** 
	 * test27 test the return with a nonexisting received time
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testFindByDateReceived_3() throws SQLException {
	
		LocalDateTime rev=LocalDateTime.of(2011, 01, 20, 12, 40, 00);
		ArrayList<MailBean> mails = new ArrayList<MailBean>();
		
		EmailDAO ed = new EmailDAOImpl();
		mails=ed.findByDateReceived(rev);	
		log.info("Test case 10_3: ");
		assertEquals("TEST_10__03_testfindByDateReceived() failed. ", 0, mails.size()); // test by giving a nonexisting received time		
	}
	
	/**
	 * test28 test if the update was successful
	 * 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testUpdateMail() throws SQLException {
		LocalDateTime ldts=LocalDateTime.of(2014, 9, 14, 9, 00, 00);
		LocalDateTime ldtr=LocalDateTime.of(2014, 9, 15, 11, 00, 00);
		
		MailBean mail = new MailBean(5,"zzh.517.00001@gmail.com", "Tony", "Appendicitis", null, 1, ldts, ldtr, 0);
		mail.getToField().add("zzh.517.00002@gmail.com");
		
		EmailDAO ed = new EmailDAOImpl();
		int result=ed.update(mail);
		log.info("Test case 11_1: ");
		assertEquals("TEST_11__01_testUpdateMail() failed. ", 1, result);
	}
	
	/**
	 * test29 updating a mail's folder field according to its id and comparing it with the original data's folder field
	 * 
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testUpdateMail_2() throws SQLException {
		LocalDateTime ldts=LocalDateTime.of(2014, 9, 14, 9, 00, 00);
		LocalDateTime ldtr=LocalDateTime.of(2014, 9, 15, 11, 00, 00);
		
		MailBean mail = new MailBean(5,"zzh.517.00001@gmail.com", "Tony", "Appendicitis", null, 1, ldts, ldtr, 0);
		mail.getToField().add("zzh.517.00002@gmail.com");
		
		EmailDAO ed = new EmailDAOImpl();
		ed.update(mail);
		
		MailBean mail2 = ed.findByID(5);
		log.info("Test case 11_2: ");
		assertEquals("TEST_11__02_testUpdateMail() failed. ", mail.getFolderID(), mail2.getFolderID());
	}
	
	/**
	 * test30 updating a folder's name according to its id and comparing it with the original data's folder name field
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testUpdateFolder() throws SQLException {
		FolderBean fd=new FolderBean(1, "sent");
		
		EmailDAO ed = new EmailDAOImpl();
		int result=ed.update(fd);
		log.info("Test case 12_1: ");
		assertEquals("TEST_12__testUpdateFolder() failed. ", 1, result);
	}
	
	/**
	 * test31 updating a folder's name according to its id and comparing it with the original data's folder name field
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testUpdateFolder_2() throws SQLException {
		FolderBean fd=new FolderBean(1, "sent");
		
		EmailDAO ed = new EmailDAOImpl();
		ed.update(fd);
		log.info("Test case 12_2: ");
		assertEquals("TEST_12__testUpdateFolder() failed. ", fd, ed.findFolderByID(1));
	}
	
	/**
	 * test32 test if the deleting a mail from mail table is successful
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDelete() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		int result=ed.delete(4);
		log.info("Test case 13_1: ");
		assertEquals("TEST_13__01_testDelete() failed. ", 1, result);
	}
	
	/**
	 * test33 check if the remaining number of mails in mail table reflects the deletion
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDelete_2() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		ed.delete(4);
		log.info("Test case 13_2: ");
		assertEquals("TEST_13__02_testDelete() failed. ", 4, ed.findAllMails().size()); // check mail table
	}
	
	/**
	 * test34 check if the attachment table has cascadingly deleted related records
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDelete_3() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		ed.delete(4);
		log.info("Test case 13_3: ");
		// check the deleted mail's attachments have also been deleted from the attachment table
		for(int i=1; i<3; i++)
			assertEquals("TEST_13__03_testDelete() failed. ", null, ed.findAttachmentByID(i)); // check the deleted mail's recipient table
	}
	
	/**
	 * test35 check if the recipient table has cascadingly deleted related records
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDelete_4() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		ed.delete(4);
		log.info("Test case 13_4: ");
		// check the deleted mail's recipients have also been deleted from the recipient table
		for(int i=4; i<6; i++)
			assertEquals("TEST_13__04_testDelete() failed. ", null, ed.findRecipientByID(i)); // check the deleted mail's recipient table
	}
	
	/**
	 * test36 check if the mail_recipient table has cascadingly deleted related records
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDelete_5() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		ed.delete(4);
		log.info("Test case 13_5: ");
		// check the deleted mail's recipients have also been deleted from the recipient table
		for(int i=4; i<6; i++)
			assertEquals("TEST_13__05_testDelete() failed. ", null, ed.findBridgeByID(i)); // check the deleted mail's recipient table
	}
	
	/**
	 * test37 deleting a folder from folder table
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDeleteFolder() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		int result=ed.deleteFolder(1);
		log.info("Test case 14_1: ");
		assertEquals("TEST_14__testDeleteFolder() failed. ", 1, result);
	}
	
	/**
	 * test38 prove a folder has been deleted from the folder table
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDeleteFolder_2() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		ed.deleteFolder(2);
		log.info("Test case 14_2: ");
		
		for(int i=4; i<6; i++)
			assertEquals("TEST_14__testDeleteFolder() failed. ", null, ed.findByID(i));
	}
	
	/**
	 * test39 prove a folder has been deleted from the folder table
	 * 
	 * @throws SQLException
	 */
	//@Ignore
	@Test
	public void testDeleteFolder_3() throws SQLException {
		
		EmailDAO ed = new EmailDAOImpl();
		ed.deleteFolder(2);
		log.info("Test case 14_3: ");
	
		assertEquals("TEST_14__testDeleteFolder() failed. ", null, ed.findFolderByID(2));
	}
	
	/**
	 * This routine recreates the database for every test. This makes sure that
	 * a destructive test will not interfere with any other test.
	 * 
	 * This routine is courtesy of Bartosz Majsak, an Arquillian developer at
	 * JBoss who helped me out last winter with an issue with Arquillian. Look
	 * up Arquillian to learn what it is.
	 */
	@Before
	public void seedDatabase() {
		
		try{
        	pb = pm.loadTextProperties("", filename);
        }catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		final String seedDataScript = loadAsString("maildb.sql");
		try (Connection connection = DriverManager.getConnection(pb.getHosturl()+pb.getPortnumber()+"/", pb.getUser(), pb.getPassword());) {
			for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
				connection.prepareStatement(statement).execute();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed seeding database", e);
		}
	}

	/**
	 * The following methods support the seedDatabse method
	 */
	private String loadAsString(final String path) {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			Scanner scanner = new Scanner(inputStream)) {
			return scanner.useDelimiter("\\A").next();
		} catch (IOException e) {
			throw new RuntimeException("Unable to close input stream.", e);
		}
	}

	private List<String> splitStatements(Reader reader, String statementDelimiter) {
		final BufferedReader bufferedReader = new BufferedReader(reader);
		final StringBuilder sqlStatement = new StringBuilder();
		final List<String> statements = new LinkedList<String>();
		try {
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || isComment(line)) {
					continue;
				}
				sqlStatement.append(line);
				if (line.endsWith(statementDelimiter)) {
					statements.add(sqlStatement.toString());
					sqlStatement.setLength(0);
				}
			}
			return statements;
		} catch (IOException e) {
			throw new RuntimeException("Failed parsing sql", e);
		}
	}

	private boolean isComment(final String line) {
		return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
	}
}
