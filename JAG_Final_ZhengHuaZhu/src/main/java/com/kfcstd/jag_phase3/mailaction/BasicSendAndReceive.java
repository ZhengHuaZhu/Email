package com.kfcstd.jag_phase3.mailaction;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Flags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kfcstd.jag_phase3.beans.MailBean;
import com.kfcstd.jag_phase3.beans.MailConfigBean;
import com.kfcstd.jag_phase3.beans.PropertiesBean;
import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailFilter;
import jodd.mail.EmailMessage;
import jodd.mail.ImapServer;
import jodd.mail.ImapSslServer;
import jodd.mail.MailAddress;
import jodd.mail.ReceiveMailSession;
import jodd.mail.ReceivedEmail;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;

/**
 * JAG project Phase 1
 * 
 * The class sends and receives emails that
 * consist of to, from, cc, Bcc, subject, 
 * text, and may or may not with html text or/and attachment.
 * 
 * @author Zheng Hua Zhu
 *
 */
public class BasicSendAndReceive {
	
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	/**
	 * Send routine that handles the components mentioned above.
	 */
	public final String sendEmail(final MailBean mailBean, final MailConfigBean sendConfigBean) {

		// Create am SMTP server object
		SmtpServer<?> smtpServer = SmtpSslServer.create(sendConfigBean.getUrl())
				.authenticateWith(sendConfigBean.getEmailaddress(), sendConfigBean.getPassword());

		// Do not use the fluent type because ArrayLists need to be processed
		// from the bean
		Email email = Email.create();

		// sender info
		email.from(sendConfigBean.getEmailaddress());

		// send to receivers
		for (String emailAddress : mailBean.getToField()) {
			email.to(emailAddress);
		}
		
		// send copies
		for (String emailAddress : mailBean.getCcField()) {
			email.cc(emailAddress);
		}
		
		// send blind copies
		for (String emailAddress : mailBean.getbCCField()) {
			email.bcc(emailAddress);
		}

		// send subject
		email.subject(mailBean.getSubjectField());		
		
		// send plain text
		email.addText(mailBean.getTextField());
		
		// send html text
		if(mailBean.getHtmlField()!=null)
			email.addHtml(mailBean.getHtmlField());
		
		// send regular attachments
		if(mailBean.getAttachmentField()!=null)
			for(EmailAttachment att: mailBean.getAttachmentField()){
				email.attach(att);
			}
		
		// send embedded attachments
		if(mailBean.getEmbeddedAttField()!=null)
			for(EmailAttachment att: mailBean.getEmbeddedAttField()){
				email.embed(att);
			}

		// A session is the object responsible for communicating with the server
		SendMailSession session = smtpServer.createSession();

		// Like a file we open the session, send the message and close the
		// session
		session.open();
		String messageId = session.sendMail(email);
		session.close();

		return messageId;

	}

	/**
	 * Basic receive that only takes the values that match the basic mail bean.
	 * Returns an array list because there could be more than one message. This
	 * could be a problem during testing so use addresses that do not receive
	 * any other messages and you can assume that subscript 0 in the array is
	 * the message you just sent.
	 */
	public final ArrayList<MailBean> receiveEmail(final MailConfigBean receiveConfigBean) {

		ArrayList<MailBean> mailBeans = null;

		// Create an IMAP server that does not display debug info
		ImapServer imapServer = new ImapSslServer(receiveConfigBean.getUrl(), receiveConfigBean.getEmailaddress(),
				receiveConfigBean.getPassword());

		// A session is the object responsible for communicating with the server
		ReceiveMailSession session = imapServer.createSession();
		session.open();

		// We only want messages that have not been read yet.
		// Messages that are delivered are then marked as read on the server
		ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(EmailFilter.filter().flag(Flags.Flag.SEEN, false));

		// If there is any email then loop through them adding their contents to
		// a new MailBean that is then added to the array list.
		if (emails != null) {

			// Instantiate the array list of messages
			mailBeans = new ArrayList<MailBean>();

			for (ReceivedEmail email : emails) {

				MailBean mailBean = new MailBean();

				mailBean.setFromField(email.getFrom().getEmail());
				mailBean.setSubjectField(email.getSubject());
				mailBean.setSentTime(email.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
				mailBean.setReceivedTime(email.getReceiveDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
				
				// populate the toField
				for (MailAddress mailAddress : email.getTo()) {
					mailBean.getToField().add(mailAddress.getEmail());
				}
				// populate the ccField
				for (MailAddress mailAddress : email.getCc()) {
					mailBean.getCcField().add(mailAddress.getEmail());
				}
				// populate the bCCField
				for (MailAddress mailAddress : email.getBcc()) {
					mailBean.getbCCField().add(mailAddress.getEmail());
				}
				
				// Messages may be multi-part so they are stored in an array
				List<EmailMessage> messages = email.getAllMessages();
				
				// Categorize messages from plain text to html
				for(EmailMessage em : messages){
					if(em.getMimeType().equals("TEXT/PLAIN"))
						mailBean.setTextField(em.getContent());
					if(em.getMimeType().equals("TEXT/HTML"))
						mailBean.setHtmlField(em.getContent());					
				}
				
				List<EmailAttachment> attachments = email.getAttachments();
				if (attachments != null) {
					//System.out.println("+++++");
					for (EmailAttachment attachment : attachments) {
						// It is an embedded attachment if there is a content id, otherwise
						// it is a regular attachment
						if(attachment.getContentId()==null)
							mailBean.getAttachmentField().add(attachment);
						else
							mailBean.getEmbeddedAttField().add(attachment);
					}
				}

				// Add the mailBean to the array list
				mailBeans.add(mailBean);
			}
		}
		session.close();

		return mailBeans;
	}
}
