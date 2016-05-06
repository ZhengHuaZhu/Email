package com.kfcstd.jag_phase3.beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class to contain the properties for an email account and the database access 
 * credentials. 
 * 
 * @author Zheng Hua Zhu
 * 
 */
public class PropertiesBean {

	private StringProperty username;
	private StringProperty emailaddress;
	private StringProperty name;
	private StringProperty emailpassword;
	private StringProperty smtpurl;
	private StringProperty imapurl;
	
	private StringProperty hosturl;
	private StringProperty portnumber;
	private StringProperty databasename;
	private StringProperty user;
	private StringProperty password;
	
	private String receiveremailaddress;
	private String receiverpassword;

	/**
	 * Default Constructor
	 */
	public PropertiesBean() {
		this("", "", "", "", "", "", "", "3306", "", "", "");
	}

	/**
	 * @param host
	 * @param userEmailAddress
	 * @param password
	 */
	public PropertiesBean(final String username, final String emailaddress, final String name, final String emailpassword,
						  final String smtpurl, final String imapurl, final String hosturl, final String portnumber, 
						  final String databasename, final String user, final String password) {
		super();
		this.username = new SimpleStringProperty(username);
		this.emailaddress =  new SimpleStringProperty(emailaddress);
		this.name=new SimpleStringProperty(name);
		this.emailpassword=new SimpleStringProperty(emailpassword);
		this.smtpurl=new SimpleStringProperty(smtpurl);
		this.imapurl=new SimpleStringProperty(imapurl);
		this.hosturl=new SimpleStringProperty(hosturl);
		this.portnumber=new SimpleStringProperty(portnumber);
		this.databasename=new SimpleStringProperty(databasename);
		this.user=new SimpleStringProperty(user);
		this.password = new SimpleStringProperty(password);
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username.get();
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username.set(username);
	}
	
	/**
	 * @return the username property
	 */
	public StringProperty usernameProperty(){
		return this.username;
	}

	/**
	 * @return the emailaddress
	 */
	public String getEmailaddress() {
		return this.emailaddress.get();
	}

	/**
	 * @param emailaddress the emailaddress to set
	 */
	public void setEmailaddress(String emailaddress) {
		this.emailaddress.set(emailaddress);
	}
	
	/**
	 * @return the emailaddress property
	 */
	public StringProperty emailaddressProperty(){
		return this.emailaddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name.get();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name.set(name);
	}
	
	/**
	 * @return the name property
	 */
	public StringProperty nameProperty(){
		return this.name;
	}

	/**
	 * @return the emailpassword
	 */
	public String getEmailpassword() {
		return this.emailpassword.get();
	}

	/**
	 * @param emailpassword the emailpassword to set
	 */
	public void setEmailpassword(String emailpassword) {
		this.emailpassword.set(emailpassword);
	}
	
	/**
	 * @return the emailpassword property
	 */
	public StringProperty emailpasswordProperty(){
		return this.emailpassword;
	}

	/**
	 * @return the smtpurl
	 */
	public String getSmtpurl() {
		return this.smtpurl.get();
	}

	/**
	 * @param smtpurl the smtpurl to set
	 */
	public void setSmtpurl(String smtpurl) {
		this.smtpurl.set(smtpurl);
	}
	
	/**
	 * @return the smtpurl property
	 */
	public StringProperty smtpurlProperty(){
		return this.smtpurl;
	}

	/**
	 * @return the imapurl
	 */
	public String getImapurl() {
		return this.imapurl.get();
	}

	/**
	 * @param imapurl the imapurl to set
	 */
	public void setImapurl(String imapurl) {
		this.imapurl.set(imapurl);
	}
	
	/**
	 * @return the imapurl property
	 */
	public StringProperty imapurlProperty(){
		return this.imapurl;
	}

	/**
	 * @return the hosturl
	 */
	public String getHosturl() {
		return this.hosturl.get();
	}

	/**
	 * @param hosturl the hosturl to set
	 */
	public void setHosturl(String hosturl) {
		this.hosturl.set(hosturl);
	}
	
	/**
	 * @return the hosturl property
	 */
	public StringProperty hosturlProperty(){
		return this.hosturl;
	}

	/**
	 * @return the portnumber
	 */
	public String getPortnumber() {
		return this.portnumber.get();
	}

	/**
	 * @param portnumber the portnumber to set
	 */
	public void setPortnumber(final String portnumber) {
		this.portnumber.set(portnumber);
	}
	
	/**
	 * @return the portnumber property
	 */
	public StringProperty portnumberProperty(){
		return this.portnumber;
	}

	/**
	 * @return the databasename
	 */
	public String getDatabasename() {
		return this.databasename.get();
	}

	/**
	 * @param databasename the databasename to set
	 */
	public void setDatabasename(String databasename) {
		this.databasename.set(databasename);
	}
	
	/**
	 * @return the databasename property
	 */
	public StringProperty databasenameProperty(){
		return this.databasename;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user.get();
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user.set(user);
	}
	
	/**
	 * @return the user property
	 */
	public StringProperty userProperty(){
		return this.user;
	}
	
	/**
	 * @return the password
	 */
	public final String getPassword() {
		return this.password.get();
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public final void setPassword(final String password) {
		this.password.set(password);
	}
	
	/**
	 * @return the password property
	 */
	public StringProperty passwordProperty(){
		return this.password;
	}
	
	public String getReceiveremailaddress(){
		return this.receiveremailaddress;
	}
	
	public final void setReceiveremailaddress(final String rea) {
		this.receiveremailaddress=rea;
	}
	
	public String getReceiverpassword(){
		return this.receiverpassword;
	}
	
	public final void setReceiverpassword(final String rp) {
		this.receiverpassword=rp;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((databasename.get() == null) ? 0 : databasename.get().hashCode());
		result = prime * result + ((emailaddress.get() == null) ? 0 : emailaddress.get().hashCode());
		result = prime * result + ((emailpassword.get() == null) ? 0 : emailpassword.get().hashCode());
		result = prime * result + ((hosturl.get() == null) ? 0 : hosturl.get().hashCode());
		result = prime * result + ((imapurl.get() == null) ? 0 : imapurl.get().hashCode());
		result = prime * result + ((name.get() == null) ? 0 : name.get().hashCode());
		result = prime * result + ((password.get() == null) ? 0 : password.get().hashCode());
		result = prime * result + ((portnumber.get() == null) ? 0 : portnumber.get().hashCode());
		result = prime * result + ((smtpurl.get() == null) ? 0 : smtpurl.get().hashCode());
		result = prime * result + ((user.get() == null) ? 0 : user.get().hashCode());
		result = prime * result + ((username.get() == null) ? 0 : username.get().hashCode());
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
		PropertiesBean other = (PropertiesBean) obj;
		if (databasename.get() == null) {
			if (other.databasename.get() != null)
				return false;
		} else if (!databasename.get().equals(other.databasename.get()))
			return false;
		if (emailaddress.get() == null) {
			if (other.emailaddress.get() != null)
				return false;
		} else if (!emailaddress.get().equals(other.emailaddress.get()))
			return false;
		if (emailpassword.get() == null) {
			if (other.emailpassword.get() != null)
				return false;
		} else if (!emailpassword.get().equals(other.emailpassword.get()))
			return false;
		if (hosturl.get() == null) {
			if (other.hosturl.get() != null)
				return false;
		} else if (!hosturl.get().equals(other.hosturl.get()))
			return false;
		if (imapurl.get() == null) {
			if (other.imapurl.get() != null)
				return false;
		} else if (!imapurl.get().equals(other.imapurl.get()))
			return false;
		if (name.get() == null) {
			if (other.name.get() != null)
				return false;
		} else if (!name.get().equals(other.name.get()))
			return false;
		if (password.get() == null) {
			if (other.password.get() != null)
				return false;
		} else if (!password.get().equals(other.password.get()))
			return false;
		if (portnumber.get() == null) {
			if (other.portnumber.get() != null)
				return false;
		} else if (!portnumber.get().equals(other.portnumber.get()))
			return false;
		if (smtpurl.get() == null) {
			if (other.smtpurl.get() != null)
				return false;
		} else if (!smtpurl.get().equals(other.smtpurl.get()))
			return false;
		if (user.get() == null) {
			if (other.user.get() != null)
				return false;
		} else if (!user.get().equals(other.user.get()))
			return false;
		if (username.get() == null) {
			if (other.username.get() != null)
				return false;
		} else if (!username.get().equals(other.username.get()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertiesBean [username=" + username.get() + ", emailaddress=" + emailaddress.get() + ", name=" + name.get()
				+ ", emailpassword=" + emailpassword.get() + ", smtpurl=" + smtpurl.get() + ", imapurl=" + imapurl.get() + ", hosturl="
				+ hosturl.get() + ", portnumber=" + portnumber.get() + ", databasename=" + databasename.get() + ", user=" + user.get()
				+ ", password=" + password.get() + "]";
	}
	
}