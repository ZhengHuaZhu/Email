/**
 * 
 */
package com.kfcstd.jag_phase3.beans;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Zheng Hua Zhu
 *
 */
public class RecipientBean {

	private int recipientID;	
	private StringProperty to;
	private StringProperty cc;
	private StringProperty bcc;
	
	public RecipientBean(){
		this(-1, "", "", "");
	}
	
	/**
	 * @param recipientID
	 * @param to
	 * @param cc
	 * @param bcc
	 */
	public RecipientBean(final int recipientID, final String to, final String cc, final String bcc){
		super();
		this.recipientID=recipientID;
		this.to=new SimpleStringProperty(to);
		this.cc=new SimpleStringProperty(cc);
		this.bcc=new SimpleStringProperty(bcc);
	}

	/**
	 * @return the recipientID
	 */
	public int getRecipientID() {
		return recipientID;
	}

	/**
	 * @param recipientID the recipientID to set
	 */
	public void setRecipientID(int recipientID) {
		this.recipientID = recipientID;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return this.to.get();
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(final String to) {
		this.to.set(to);
	}
	
	public StringProperty toProperty(){
		return this.to;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return this.cc.get();
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(final String cc) {
		this.cc.set(cc);
	}
	
	public StringProperty ccProperty(){
		return this.cc;
	}

	/**
	 * @return the bcc
	 */
	public String getBcc() {
		return this.bcc.get();
	}

	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(final String bcc) {
		this.bcc.set(bcc);
	}
	
	public StringProperty bccProperty(){
		return this.bcc;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bcc.get() == null) ? 0 : bcc.get().hashCode());
		result = prime * result + ((cc.get() == null) ? 0 : cc.get().hashCode());
		result = prime * result + recipientID;
		result = prime * result + ((to.get() == null) ? 0 : to.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecipientBean other = (RecipientBean) obj;
		if (bcc.get() == null) {
			if (other.bcc.get() != null)
				return false;
		} else if (!bcc.get().equals(other.bcc.get()))
			return false;
		if (cc.get() == null) {
			if (other.cc.get() != null)
				return false;
		} else if (!cc.get().equals(other.cc.get()))
			return false;
		if (recipientID != other.recipientID)
			return false;
		if (to.get() == null) {
			if (other.to.get() != null)
				return false;
		} else if (!to.get().equals(other.to.get()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RecipientBean [recipientID=" + recipientID + ", tofield=" + to.get() + ", cc=" + cc.get() + ", bcc=" + bcc.get() + "]";
	}
	
}
