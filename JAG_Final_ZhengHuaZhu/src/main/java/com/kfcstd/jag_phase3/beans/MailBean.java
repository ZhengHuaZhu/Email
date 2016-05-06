/**
 * 
 */
package com.kfcstd.jag_phase3.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jodd.mail.EmailAttachment;

/**
 * Here is an email data bean. It holds fields that represent an email like
 * to, from, cc, bCC, subject, text, html text, attachment, sent time, 
 * received time, send/receive status, belonging folder, etc.
 * 
 * @author Zheng Hua Zhu
 *
 */
public class MailBean {
	
	private IntegerProperty mailID;

	// The sender
	private StringProperty fromField;
	
	// The receiver address(es)
	private ArrayList<String> toField;
	
	// Copy receiver(s) and blind copy receiver(s)
	private ArrayList<String> ccField, bCCField;

	// The subject line of the email
	private StringProperty subjectField;

	// Plain text part of the email
	private StringProperty textField;
	
	// html text part of the email
	private StringProperty htmlField;
	
	// attachment part of the email
	private ArrayList<EmailAttachment> attachmentField;
	
	// embedded attachment part of the email
	private ArrayList<EmailAttachment> embeddedAttField;

	// Id of the folder
	private IntegerProperty folderID;

	// Status 0 = New Email for Sending
	// Status 1 = Received Email
	private int mailStatus;
	
	// Date object to track down sent time
	//private LocalDateTime sentTime;
	private ObjectProperty<LocalDateTime> sentTime;
	
	// Data object to track down received time
	//private LocalDateTime receivedTime;
	private ObjectProperty<LocalDateTime> receivedTime;
	
	// Create Logger object to log essential information
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
		

	/**
	 * Default constructor for a new mail message waiting to be sent
	 */
	public MailBean() {
		this(-1, "", "", "", null, -1, LocalDateTime.now(), LocalDateTime.now(), 0 );
	}

	/**
	 * Constructor for creating an Email from either a form or a database record
	 * 
	 * @param mailID
	 * @param fromField
	 * @param subjectField
	 * @param textField
	 * @param htmlField
	 * @param sentTime
	 * @param receivedTime
	 * @param folder
	 * @param mailStatus
	 */
	public MailBean(final int mailID, final String fromField, final String subjectField, final String textField, 
					final String htmlField, final int folderID, final LocalDateTime sentTime,
					final LocalDateTime receivedTime, final int mailStatus) {
		super();
		this.mailID=new SimpleIntegerProperty(mailID);
		this.fromField = new SimpleStringProperty(fromField);
		this.toField = new ArrayList<>();
		this.ccField= new ArrayList<>();
		this.bCCField= new ArrayList<>();
		this.subjectField = new SimpleStringProperty(subjectField);
		this.textField = new SimpleStringProperty(textField);
		this.htmlField=new SimpleStringProperty(htmlField);
		this.attachmentField=new ArrayList<>();
		this.embeddedAttField=new ArrayList<>();
		this.sentTime=new SimpleObjectProperty<LocalDateTime>(sentTime);
		this.receivedTime=new SimpleObjectProperty<LocalDateTime>(receivedTime);
		this.folderID = new SimpleIntegerProperty(folderID);
		this.mailStatus = mailStatus;
	}

	/**
	 * 
	 * @return the mailID
	 */
	public int getMailID() {
		return this.mailID.get();
	}

	/**
	 * 
	 * @param mailID
	 */
	public void setMailID(final int mailID) {
		this.mailID.set(mailID);
	}
	
	public final IntegerProperty mailIDProperty(){
		return this.mailID;
	}

	/**
	 * @return the fromField
	 */
	public final String getFromField() {
		return this.fromField.get();
	}
	
	/**
	 * @return the fromField property
	 */
	public final StringProperty fromFieldProperty(){
		return this.fromField;
	}

	/**
	 * When passing a reference to a setter it is best practice to declare it
	 * final so that the setter cannot change the reference
	 * 
	 * @param fromField
	 *            the fromField to set
	 */
	public final void setFromField(final String fromField) {
		this.fromField.set(fromField);
	}

	/**
	 * @return the subjectField
	 */
	public final String getSubjectField() {
		return this.subjectField.get();
	}
	
	/**
	 * @return the subjectField property
	 */
	public final StringProperty subjectFieldProperty(){
		return this.subjectField;
	}

	/**
	 * @param subjectField
	 *            the subjectField to set
	 */
	public final void setSubjectField(final String subjectField) {
		this.subjectField.set(subjectField);
	}

	/**
	 * @return the textMessageField
	 */
	public final String getTextField() {
		return this.textField.get();
	}
	
	public final StringProperty textFieldProperty(){
		return this.textField;
	}

	/**
	 * @param textMessageField
	 *            the textMessageField to set
	 */
	public final void setTextField(final String textField) {
		this.textField.set(textField);
	}

	/**
	 * @return the folder
	 */
	public final int getFolderID() {
		return this.folderID.get();
	}

	/**
	 * @param folder
	 *            the folder to set
	 */
	public final void setFolderID(final int folderID) {
		this.folderID.set(folderID);
	}
	
	public final IntegerProperty folderIDProperty(){
		return this.folderID;
	}

	/**
	 * @return the mailStatus
	 */
	public final int getMailStatus() {
		return mailStatus;
	}

	/**
	 * @param mailStatus
	 *            the mailStatus to set
	 */
	public final void setMailStatus(final int mailStatus) {
		this.mailStatus = mailStatus;
	}

	/**
	 * There is no set when working with collections. When you get the ArrayList
	 * you can add elements to it. A set method implies changing the current
	 * ArrayList for another ArrayList and this is something we rarely do with
	 * collections.
	 * 
	 * @return the toField
	 */
//	public final ArrayList<String> getToField() {
//		return this.getToField(); //?
//	}
	
	public final ArrayList<String> getToField(){
		return toField;
	}

	/**
	 * @return the attachmentField
	 */
	public ArrayList<EmailAttachment> getAttachmentField() {
		return attachmentField;
	}

	/**
	 * @return the ccField
	 */
	public ArrayList<String> getCcField() {
		return ccField;
	}

	/**
	 * @return the bCCField
	 */
	public ArrayList<String> getbCCField() {
		return bCCField;
	}

	/**
	 * @return the htmlField
	 */
	public String getHtmlField() {
		return this.htmlField.get();
	}
	
	/**
	 * @return the htmlField
	 */
	public final StringProperty htmlFieldProperty(){
		return this.htmlField;
	}

	/**
	 * @param htmlField the htmlField to set
	 */
	public void setHtmlField(String htmlField) {
		this.htmlField.set(htmlField);
	}

	/**
	 * @return the sentTime
	 */
	public final LocalDateTime getSentTime() {
		return this.sentTime.get();
	}
	
	/**
	 * @return sentTime
	 */
	public final ObjectProperty<LocalDateTime> sentTimeProperty(){
		return this.sentTime;
	}

	/**
	 * @param sentTime the sentTime to set
	 */
	public void setSentTime(LocalDateTime sentTime) {
		this.sentTime.set(sentTime);
	}

	/**
	 * @return the receiveTime
	 */
	public LocalDateTime getReceivedTime() {
		return this.receivedTime.get();
	}
	
	/**
	 * @return receivedTime
	 */
	public final ObjectProperty<LocalDateTime> receivedTimeProperty(){
		return this.receivedTime;
	}

	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceivedTime(LocalDateTime receivedTime) {
		this.receivedTime.set(receivedTime);
	}

	/**
	 * @return the embeddedAttField
	 */
	public ArrayList<EmailAttachment> getEmbeddedAttField() {
		return embeddedAttField;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * The main fields needed to generate hashCode.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachmentField == null) ? 0 : attachmentField.hashCode());
		result = prime * result + ((ccField == null) ? 0 : ccField.hashCode());
		result = prime * result + ((embeddedAttField == null) ? 0 : embeddedAttField.hashCode());
		result = prime * result + ((fromField.get() == null) ? 0 : fromField.get().hashCode());
		result = prime * result + ((htmlField.get() == null) ? 0 : htmlField.get().hashCode());
		result = prime * result + ((subjectField.get() == null) ? 0 : subjectField.get().hashCode());
		result = prime * result + ((textField.get() == null) ? 0 : textField.get().hashCode());
		result = prime * result + ((toField == null) ? 0 : toField.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		MailBean other = (MailBean) obj;
		if (attachmentField == null) {
			if (other.attachmentField != null)
				return false;
		} else if (getAttachmentField().size()!=other.attachmentField.size()){
					return false;
				}else{
					for (int i = 0; i < getAttachmentField().size(); i++) {
						if (!Arrays.equals(getAttachmentField().get(i).toByteArray(),
								other.getAttachmentField().get(i).toByteArray())) {
							log.info("The regular attachments at both sides are not equal.");
							return false;
						}
					}
		        }// end attachment equals
		
		if(mailID.get()!=other.mailID.get()) // check mailID
			return false;
		
		if(folderID.get()!=other.folderID.get()) // check folderID
			return false;
			
		if (ccField == null) {
			if (other.ccField != null)
				return false;
		} else if (getCcField().size()!=other.getCcField().size()){
			return false;
		}else{
			for (int i = 0; i < getCcField().size(); i++) {
				if (!getCcField().get(i).equals(other.getCcField().get(i))) {
					log.info("The cc field at both sides are not equal.");
					return false;
				}
			}
        }// end cc equals
		
		if (embeddedAttField == null) {
			if (other.embeddedAttField != null)
				return false;
		} else if (getEmbeddedAttField().size()!=other.getEmbeddedAttField().size()){
			return false;
		}else{
			for (int i = 0; i < getEmbeddedAttField().size(); i++) {
				if (!Arrays.equals(getEmbeddedAttField().get(i).toByteArray(),
						other.getEmbeddedAttField().get(i).toByteArray())) {
					log.info("The embedded attachments at both sides are not equal.");
					return false;
				}
			}
        }// end embeddedAtt equals
		
		if (fromField.get() == null) {
			if (other.fromField.get() != null)
				return false;
		} else if (!fromField.get().equals(other.fromField.get())){
			log.info("The from field at both sides are not equal.");
			return false;
		}// end from equals
		
		if (htmlField.get() == null) {
			if (other.htmlField.get() != null)
				return false;
		} else if (!getHtmlField().trim().equals(other.getHtmlField().trim())){
			log.info("The html message at both sides are not equal.");
			return false;
		}// end html equals
		
		if (subjectField.get() == null) {
			if (other.subjectField.get() != null)
				return false;
		} else if (!subjectField.get().equals(other.subjectField.get())){
			log.info("The subject field at both sides are not equal.");
			return false;
		}// end subject equals
		
		if (textField.get() == null) {
			if (other.textField.get() != null)
				return false;
		} else if (!getTextField().trim().equals(other.getTextField().trim())){
			log.info("The text messages at both sides are not equal.");
			return false;
		}// end text equals
		
		if (toField == null) {
			if (other.toField != null){
				return false;
			}
		} else if (getToField().size()!=other.getToField().size()){
			return false;
			}else{
				for (int i = 0; i < getToField().size(); i++) {
					if (!getToField().get(i).equals(other.getToField().get(i))) {
						log.info("The to_field at both sides are not equal.");
						return false;
					}
				}
			}// end to equals
		return true;
	}

	@Override
	public String toString() {
		return "MailBean [mailID=" + mailID.get() + ", fromField=" + fromField.get() + ", toField=" + toField + ", ccField="
				+ ccField + ", bCCField=" + bCCField + ", subjectField=" + subjectField.get() + ", textField=" + textField.get()
				+ ", htmlField=" + htmlField.get() + ", attachmentField=" + attachmentField + ", embeddedAttField="
				+ embeddedAttField + ", folderID=" + folderID.get() + ", mailStatus=" + mailStatus + ", sentTime=" + sentTime.get()
				+ ", receivedTime=" + receivedTime.get() + "]";
	}

}
