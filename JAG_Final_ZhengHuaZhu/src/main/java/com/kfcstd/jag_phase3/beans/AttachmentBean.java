/**
 * 
 */
package com.kfcstd.jag_phase3.beans;

/**
 * @author Zheng Hua Zhu
 *
 */
public class AttachmentBean {
	
	private int attachmentID;
	private int mailID;
	
	private String name;
	private String CID;
	private int size;
	private byte[] image;
	
	public AttachmentBean(){
		this(-1, -1, "", "", 0);
	}
	
	/**
	 * 
	 * @param attachmentID
	 * @param name
	 * @param CID
	 * @param size
	 * @param image
	 */
	public AttachmentBean(final int attachmentID, final int mailID, final String name, final String CID,
			              final int size){
		super();
		this.attachmentID=attachmentID;
		this.mailID=mailID;
		this.name=name;
		this.CID=CID;
		this.size=size;
		this.image=new byte[0];
		
	}
	
	/**
	 * @return the attachmentID
	 */
	public int getAttachmentID() {
		return attachmentID;
	}
	/**
	 * @param attachmentID the attachmentID to set
	 */
	public void setAttachmentID(int attachmentID) {
		this.attachmentID = attachmentID;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the cID
	 */
	public String getCID() {
		return CID;
	}
	/**
	 * @param cID the cID to set
	 */
	public void setCID(String cID) {
		CID = cID;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CID == null) ? 0 : CID.hashCode());
		result = prime * result + attachmentID;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + size;
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
		AttachmentBean other = (AttachmentBean) obj;
		if (CID == null) {
			if (other.CID != null)
				return false;
		} else if (!CID.equals(other.CID))
			return false;
		if (attachmentID != other.attachmentID)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AttachmentBean [attachmentID=" + attachmentID + ", mailID=" + mailID + ", name=" + name + ", CID=" + CID
				+ ", size=" + size + "]";
	}
	
}
