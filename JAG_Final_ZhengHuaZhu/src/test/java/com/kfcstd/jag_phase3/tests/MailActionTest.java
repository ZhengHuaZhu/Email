package com.kfcstd.jag_phase3.tests;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Rule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.beans.MailConfigBean;
import com.kfcstd.jag_phase3.properties_manager.PropertiesManager;
import com.kfcstd.jag_phase3.mailaction.BasicSendAndReceive;
import com.kfcstd.jag_phase3.beans.PropertiesBean;
import com.kfcstd.jag_phase3.tests.MethodLogger;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * A set of test methods that determine if an email can be saved, retrieved, updated
 * and deleted through a database access object which has a bunch of methods within
 * to deal with the CRUD functionality.
 * 
 * @author Zheng Hua Zhu
 *
 */
public class MailActionTest {
	@Rule
	public MethodLogger methodLogger = new MethodLogger();

	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	
	// Declare fields 
	private MailConfigBean sendConfigBean;
	private MailConfigBean receiveConfigBean;
	private BasicSendAndReceive basicSendAndReceive;
	private MailBean mailBeanSend;
	private ArrayList<MailBean> mailBeansReceive;
	private String messageId="-1000";
	private PropertiesManager mp; 
	
	/**
	 * Initialize necessary fields for each test case
	 */
	@Ignore
	@Before
	public void init(){
		mp = new PropertiesManager();
		
		try{
			PropertiesBean pb=mp.loadJarTextProperties("SettingBundle.properties");
			sendConfigBean = new MailConfigBean(pb.getSmtpurl(), pb.getEmailaddress(), pb.getEmailpassword());
			receiveConfigBean = new MailConfigBean(pb.getImapurl(), pb.getReceiveremailaddress(), pb.getReceiverpassword());
		}catch(IOException ioe){
			log.error("Error: " + ioe.getMessage());
		}catch(NullPointerException npe){
			log.error("Error: " + npe.getMessage());
		}
		
		basicSendAndReceive = new BasicSendAndReceive();
		mailBeanSend = new MailBean();		
	}
	
	/**
	 * Test1 test only the to, from and subject fields. The 3 fields have to be filled for any test case!
	 */
	@Ignore
	@Test
	public void testToFromSubjectFields() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
			
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   To: " + mailBeanSend.getToField() + " : " + mailBeansReceive.get(0).getToField());
		log.info("   From: " + mailBeanSend.getFromField() + " : " + mailBeansReceive.get(0).getFromField());
		log.info("   Subject: " + mailBeanSend.getSubjectField() + " : " + mailBeansReceive.get(0).getSubjectField());
		assertEquals("To_From_Subject fields are not equal between send and receive", mailBeanSend, mailBeansReceive.get(0));	
	}
	
	/**
	 * Test2 Add bcc field and test the cc field
	 */
	@Ignore
	@Test
	public void testCCField() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.getCcField().add("zzh.517.cctestacct@gmail.com");
		mailBeanSend.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct1@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
	
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   CC: " + mailBeanSend.getCcField() + " : " + mailBeansReceive.get(0).getCcField());
		assertEquals("CC fields are not equal between send and receive", mailBeanSend, mailBeansReceive.get(0));			
	}
	
	/**
	 * Test3 Test the text field
	 */
	@Ignore
	@Test
	public void testTextField() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.setTextField("This is the text of the message - 03.");
		
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
				
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
		
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   Text message: " + mailBeanSend.getTextField() + " : " + mailBeansReceive.get(0).getTextField());
		assertEquals("Text messages are not equal between send and receive", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test4 Test the html field
	 */
	@Ignore
	@Test
	public void testHtmlField() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.setHtmlField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1>"
				+ "<img src='cid:FreeFall.jpg'><img src='cid:WindsorKen180.jpg'><h2>I'm flying!</h2></body></html>");
		
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
	
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   HTML: " + mailBeanSend.getHtmlField() + " : " + mailBeansReceive.get(0).getHtmlField());
		assertEquals("HTML messages are not equal between send and receive", mailBeanSend, mailBeansReceive.get(0));			
	}
	
	/**
	 * Test5 Test the attachment field
	 */
	@Ignore
	@Test
	public void testAttachmentField() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		// Create regular attachments
		EmailAttachmentBuilder ra = EmailAttachment.attachment().file("ferrari1.jpg");
		EmailAttachment ra1 = ra.create();
		mailBeanSend.getAttachmentField().add(ra1);
		ra = EmailAttachment.attachment().file("ferrari2.jpg");
		EmailAttachment ra2 = ra.create();
		mailBeanSend.getAttachmentField().add(ra2);
		
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
	
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   Attachment: " + mailBeanSend.getAttachmentField() + " : " + mailBeansReceive.get(0).getAttachmentField());
		assertEquals("Attachments are not equal between send and receive", mailBeanSend, mailBeansReceive.get(0));			
	}
	
	/**
	 * Test6 Test the embedded attachment field
	 */
	@Ignore
	@Test
	public void testEmbeddedAttachmentField() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		// Create embedded attachments
		EmailAttachmentBuilder ea = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		ea.setInline("FreeFall.jpg"); // designate CID to an embedded attachment
		EmailAttachment ea1 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea1); 
		ea = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea.setInline("WindsorKen180.jpg");
		EmailAttachment ea2 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea2);
	
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
	
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   Embedded Attachment: " + mailBeanSend.getEmbeddedAttField() + " : " + mailBeansReceive.get(0).getEmbeddedAttField());
		assertEquals("Embedded attachments are not equal between send and receive", mailBeanSend, mailBeansReceive.get(0));					
	}
	
	
	/**
	 * Test7 Test the html + text fields
	 */
	@Ignore
	@Test
	public void testHtmlTextFields() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.getCcField().add("zzh.517.cctestacct@gmail.com");
		mailBeanSend.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct1@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		mailBeanSend.setTextField("This is the text of the message - 03.");
		mailBeanSend.setHtmlField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1>"
				+ "<img src='cid:FreeFall.jpg'><img src='cid:WindsorKen180.jpg'><h2>I'm flying!</h2></body></html>");
		
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
	
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   HTML: " + mailBeanSend.getHtmlField() + " : " + mailBeansReceive.get(0).getHtmlField());
		log.info("   Text: " + mailBeanSend.getTextField() + " : " + mailBeansReceive.get(0).getTextField());
		assertEquals("Html+Text messages are not the same between send and receive", mailBeanSend, mailBeansReceive.get(0));			
	}
	
	/**
	 * Test8 Test the embedded + attachment fields
	 */
	@Ignore
	@Test
	public void testEmbeddedAndRegularAttachmentFields() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		// Create embedded attachments
		EmailAttachmentBuilder ea = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		ea.setInline("FreeFall.jpg"); // designate CID to an embedded attachment
		EmailAttachment ea1 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea1); 
		ea = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea.setInline("WindsorKen180.jpg");
		EmailAttachment ea2 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea2);
		
		// Create regular attachments
		EmailAttachmentBuilder ra = EmailAttachment.attachment().file("ferrari1.jpg");
		EmailAttachment ra1 = ra.create();
		mailBeanSend.getAttachmentField().add(ra1);
		ra = EmailAttachment.attachment().file("ferrari2.jpg");
		EmailAttachment ra2 = ra.create();
		mailBeanSend.getAttachmentField().add(ra2);

		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
	
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   Embedded Attachment: " + mailBeanSend.getEmbeddedAttField() + " : " + mailBeansReceive.get(0).getEmbeddedAttField());
		log.info("   Regular Attachment: " + mailBeanSend.getAttachmentField() + " : " + mailBeansReceive.get(0).getAttachmentField());
		assertEquals("Embedded+Regular Attachments are not the same between send and receive", mailBeanSend, mailBeansReceive.get(0));					
	}
	
	/**
	 * Test9 Test Text + Embedded attachment
	 */
	@Ignore
	@Test
	public void testTextAndEmbedded() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.getCcField().add("zzh.517.cctestacct@gmail.com");
		mailBeanSend.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct1@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		
		mailBeanSend.setTextField("This is the text of the message - 03.");
		
		// Create embedded attachments
		EmailAttachmentBuilder ea = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		ea.setInline("FreeFall.jpg"); // designate CID to an embedded attachment
		EmailAttachment ea1 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea1); 
		ea = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea.setInline("WindsorKen180.jpg");
		EmailAttachment ea2 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea2);
		
		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		

		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   Text: " + mailBeanSend.getTextField() + " : " + mailBeansReceive.get(0).getTextField());
		log.info("   Embedded Attachment: " + mailBeanSend.getEmbeddedAttField() + " : " + mailBeansReceive.get(0).getEmbeddedAttField());
		assertEquals("Data are not the same between send and receive", mailBeanSend, mailBeansReceive.get(0));					
	}
	
	/**
	 * Test10 Test all the fields together
	 */
	@Ignore
	@Test
	public void testAllFields() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.getCcField().add("zzh.517.cctestacct@gmail.com");
		mailBeanSend.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct1@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		
		mailBeanSend.setTextField("This is the text of the message - 03.");
		mailBeanSend.setHtmlField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1>"
				+ "<img src='cid:FreeFall.jpg'><img src='cid:WindsorKen180.jpg'><h2>I'm flying!</h2></body></html>");
		
		// Create embedded attachments
		EmailAttachmentBuilder ea = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		ea.setInline("FreeFall.jpg"); // designate CID to an embedded attachment
		EmailAttachment ea1 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea1); 
		ea = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea.setInline("WindsorKen180.jpg");
		EmailAttachment ea2 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea2);
		
		// Create regular attachments
		EmailAttachmentBuilder ra = EmailAttachment.attachment().file("ferrari1.jpg");
		EmailAttachment ra1 = ra.create();
		mailBeanSend.getAttachmentField().add(ra1);
		ra = EmailAttachment.attachment().file("ferrari2.jpg");
		EmailAttachment ra2 = ra.create();
		mailBeanSend.getAttachmentField().add(ra2);

		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);	
		
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   To: " + mailBeanSend.getToField() + " : " + mailBeansReceive.get(0).getToField());
		log.info("   From: " + mailBeanSend.getFromField() + " : " + mailBeansReceive.get(0).getFromField());
		log.info("   Subject: " + mailBeanSend.getSubjectField() + " : " + mailBeansReceive.get(0).getSubjectField());
		log.info("   CC: " + mailBeanSend.getCcField() + " : " + mailBeansReceive.get(0).getCcField());
		log.info("   HTML: " + mailBeanSend.getHtmlField() + " : " + mailBeansReceive.get(0).getHtmlField());
		log.info("   Text: " + mailBeanSend.getTextField() + " : " + mailBeansReceive.get(0).getTextField());
		log.info("   Embedded Attachment: " + mailBeanSend.getEmbeddedAttField() + " : " + mailBeansReceive.get(0).getEmbeddedAttField());
		log.info("   Regular Attachment: " + mailBeanSend.getAttachmentField() + " : " + mailBeansReceive.get(0).getAttachmentField());
		assertEquals("Data are not the same between send and receive", mailBeanSend, mailBeansReceive.get(0));					
	}
	
	/**
	 * Test11 Test regular attachment + HTML message
	 */
	@Ignore
	@Test
	public void testHtmlAndAttachmentFields() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.getToField().add("zzh.montreal.qc@gmail.com");
		mailBeanSend.getToField().add("zhuzhenghua@live.cn");
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.getCcField().add("zzh.517.cctestacct@gmail.com");
		mailBeanSend.getCcField().add("zzh.517.cctestacct2@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct1@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct2@gmail.com");
		
		mailBeanSend.setHtmlField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1>"
				+ "<img src='cid:FreeFall.jpg'><img src='cid:WindsorKen180.jpg'><h2>I'm flying!</h2></body></html>");
		
		// Create regular attachments
		EmailAttachmentBuilder ra = EmailAttachment.attachment().file("ferrari1.jpg");
		EmailAttachment ra1 = ra.create();
		mailBeanSend.getAttachmentField().add(ra1);
		ra = EmailAttachment.attachment().file("ferrari2.jpg");
		EmailAttachment ra2 = ra.create();
		mailBeanSend.getAttachmentField().add(ra2);

		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);		
		
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   HTML: " + mailBeanSend.getHtmlField() + " : " + mailBeansReceive.get(0).getHtmlField());
		log.info("   Regular Attachment: " + mailBeanSend.getAttachmentField() + " : " + mailBeansReceive.get(0).getAttachmentField());
		assertEquals("Data are not the same between send and receive", mailBeanSend, mailBeansReceive.get(0));					
	}
	
	/**
	 * Test12 Test all the fields together with only one element respectively
	 */
	@Ignore
	@Test
	public void testAllFieldsWithOneElementEach() {
		mailBeanSend.getToField().add("zzh.517.00002@gmail.com"); 
		mailBeanSend.setFromField(sendConfigBean.getEmailaddress());
		mailBeanSend.setSubjectField("A Test Message - 03");
		mailBeanSend.getCcField().add("zzh.517.cctestacct@gmail.com");
		mailBeanSend.getbCCField().add("zzh.517.bcctestacct1@gmail.com");
		
		mailBeanSend.setTextField("This is the text of the message - 03.");
		mailBeanSend.setHtmlField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1>"
				+ "<img src='cid:FreeFall.jpg'><img src='cid:WindsorKen180.jpg'><h2>I'm flying!</h2></body></html>");
		
		// Create embedded attachments
		EmailAttachmentBuilder ea = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		ea.setInline("FreeFall.jpg"); // designate CID to an embedded attachment
		EmailAttachment ea1 = ea.create();
		mailBeanSend.getEmbeddedAttField().add(ea1); 
	
		// Create regular attachments
		EmailAttachmentBuilder ra = EmailAttachment.attachment().file("ferrari1.jpg");
		EmailAttachment ra1 = ra.create();
		mailBeanSend.getAttachmentField().add(ra1);

		messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
	
		// Add a five second pause to allow the Gmail server to receive what has been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);
		
		log.info("   MessageID: " + messageId + " :  -1000 indicates the MessageID is wrong.");
		log.info("   To: " + mailBeanSend.getToField() + " : " + mailBeansReceive.get(0).getToField());
		log.info("   From: " + mailBeanSend.getFromField() + " : " + mailBeansReceive.get(0).getFromField());
		log.info("   Subject: " + mailBeanSend.getSubjectField() + " : " + mailBeansReceive.get(0).getSubjectField());
		log.info("   CC: " + mailBeanSend.getCcField() + " : " + mailBeansReceive.get(0).getCcField());
		log.info("   HTML: " + mailBeanSend.getHtmlField() + " : " + mailBeansReceive.get(0).getHtmlField());
		log.info("   Text: " + mailBeanSend.getTextField() + " : " + mailBeansReceive.get(0).getTextField());
		log.info("   Embedded Attachment: " + mailBeanSend.getEmbeddedAttField() + " : " + mailBeansReceive.get(0).getEmbeddedAttField());
		log.info("   Regular Attachment: " + mailBeanSend.getAttachmentField() + " : " + mailBeansReceive.get(0).getAttachmentField());
		assertEquals("Data are not the same between send and receive", mailBeanSend, mailBeansReceive.get(0));					
	}
}
