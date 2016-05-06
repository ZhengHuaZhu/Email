/**
 * 
 */
package com.kfcstd.jag_phase3.beans;

/**
 * @author Zheng Hua Zhu
 *
 */
public class Mail_Recipient_Bean {
	
	private int id;
	private int mailID;
	private int recipientID;
	
	public Mail_Recipient_Bean(){
		this(-1, -1, -1);
	}
	
	/**
	 * 
	 * @param id
	 * @param mailID
	 * @param recipientID
	 */
	public Mail_Recipient_Bean(final int id, final int mailID, final int recipientID){
		super();
		this.id=id;
		this.mailID=mailID;
		this.recipientID=recipientID;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the mailID
	 */
	public int getMailID() {
		return mailID;
	}

	/**
	 * @param mailID the mailID to set
	 */
	public void setMailID(int mailID) {
		this.mailID = mailID;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + mailID;
		result = prime * result + recipientID;
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
		Mail_Recipient_Bean other = (Mail_Recipient_Bean) obj;
		if (id != other.id)
			return false;
		if (mailID != other.mailID)
			return false;
		if (recipientID != other.recipientID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Mail_Recipient_Bean [id=" + id + ", mailID=" + mailID + ", recipientID=" + recipientID + "]";
	}

	
}
