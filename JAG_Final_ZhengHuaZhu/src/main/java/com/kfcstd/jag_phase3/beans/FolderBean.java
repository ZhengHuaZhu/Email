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
public class FolderBean {
	
	private int folderID;
	private StringProperty name;
	
	public FolderBean(){
		this(-1, "");
	}

	/**
	 * 
	 * @param folderID
	 * @param name
	 */
	public FolderBean(final int folderID, final String name){
		super();
		this.folderID=folderID;
		this.name=new SimpleStringProperty(name);
	}

	/**
	 * @return the folderID
	 */
	public int getFolderID() {
		return folderID;
	}
	

	/**
	 * @param folderID the folderID to set
	 */
	public void setFolderID(int folderID) {
		this.folderID = folderID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name.get();
	}
	
	/**
	 * 
	 * @return the name property
	 */
	public StringProperty nameProperty(){
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name.set(name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + folderID;
		result = prime * result + ((name.get() == null) ? 0 : name.get().hashCode());
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
		FolderBean other = (FolderBean) obj;
		if (folderID != other.folderID)
			return false;
		if (name.get() == null) {
			if (other.name.get() != null)
				return false;
		} else if (!name.get().equals(other.name.get()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FolderBean [folderID=" + folderID + ", name=" + name.get() + "]";
	}
	
}
