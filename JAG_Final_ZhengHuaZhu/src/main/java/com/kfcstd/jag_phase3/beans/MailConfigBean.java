/**
 * 
 */
package com.kfcstd.jag_phase3.beans;

/**
 * @author Zheng Hua Zhu
 *
 */
public class MailConfigBean {
	
	private String url;
	private String emailaddress;
	private String password;

	/**
	 * Default Constructor
	 */
	public MailConfigBean() {
		this("", "", "");
	}

    /**
     * 
     * @param url
     * @param user
     * @param password
     */
	public MailConfigBean(final String url, final String emailaddress, final String password) {
		super();
		this.url = url;
		this.emailaddress = emailaddress;
		this.password = password;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getEmailaddress() {
		return emailaddress;
	}

	/**
	 * @param user the user to set
	 */
	public void setEmaiaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
