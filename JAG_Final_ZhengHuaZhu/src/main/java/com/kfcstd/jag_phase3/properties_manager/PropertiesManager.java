/**
 * 
 */
package com.kfcstd.jag_phase3.properties_manager;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.kfcstd.jag_phase3.beans.PropertiesBean;

/**
 * A class that handles accessing properties
 * 
 * @author Zheng Hua Zhu
 * 
 */
public class PropertiesManager {

	/**
	 * Returns a PropertiesBean object with the contents of the properties file
	 * 
	 * @param path
	 *            Must exist, will not be created
	 * @param propFileName
	 *            Name of the properties file
	 * @return The bean loaded with the properties
	 * @throws IOException
	 */
	public final PropertiesBean loadTextProperties(final String path, final String propFileName) throws IOException {

		Properties prop = new Properties();

		Path txtFile = get(path, propFileName);

		PropertiesBean pb = new PropertiesBean();

		// File must exist
		if (Files.exists(txtFile)) {
			try (InputStream propFileStream = newInputStream(txtFile);) {
				prop.load(propFileStream);
			}
			pb.setUsername(prop.getProperty("username"));
			pb.setEmailaddress(prop.getProperty("emailaddress"));
			pb.setEmailpassword(prop.getProperty("emailpassword"));
			pb.setName(prop.getProperty("name"));
			pb.setSmtpurl(prop.getProperty("smtpurl"));
			pb.setImapurl(prop.getProperty("imapurl"));
			pb.setHosturl(prop.getProperty("hosturl"));
			pb.setDatabasename(prop.getProperty("databasename"));
			pb.setPortnumber(prop.getProperty("portnumber"));
			pb.setUser(prop.getProperty("user"));
			pb.setPassword(prop.getProperty("password"));
		}
		return pb;
	}

	/**
	 * Creates a plain text properties file based on the parameters
	 * 
	 * @param path
	 *            Must exist, will not be created
	 * @param propFileName
	 *            Name of the properties file
	 * @param dcb
	 *            The bean to store into the properties
	 * @throws IOException
	 */
	public final void writeTextProperties(final String path, final String propFileName, final PropertiesBean pb) throws IOException {

		Properties prop = new Properties();
 
		prop.setProperty("username", pb.getUsername());
		prop.setProperty("emailaddress", pb.getEmailaddress());
		prop.setProperty("name", pb.getName());
		prop.setProperty("emailpassword", pb.getEmailpassword());
		prop.setProperty("smtpurl", pb.getSmtpurl());
		prop.setProperty("imapurl", pb.getImapurl());
		prop.setProperty("hosturl", pb.getHosturl());
		prop.setProperty("portnumber", pb.getPortnumber());
		prop.setProperty("databasename", pb.getDatabasename());
		prop.setProperty("user", pb.getUser());
		prop.setProperty("password", pb.getPassword());

		Path txtFile = get(path, propFileName);

		// Creates the file or if file exists it is truncated to length of zero
		// before writing
		try (OutputStream propFileStream = newOutputStream(txtFile)) {
			prop.store(propFileStream, "JAG Properties");
		}
	}

	/**
	 * Returns a PropertiesBean object with the contents of the properties file
	 * that is in an XML format
	 * 
	 * @param path
	 *            Must exist, will not be created. Empty string means root of
	 *            program folder
	 * @param propFileName
	 *            Name of the properties file
	 * @return The bean loaded with the properties
	 * @throws IOException
	 */
	public final PropertiesBean loadXmlProperties(final String path, final String propFileName) throws IOException {

		Properties prop = new Properties();

		// The path of the XML file
		Path xmlFile = get(path, propFileName + ".xml");

		PropertiesBean pb = new PropertiesBean();

		// File must exist
		if (Files.exists(xmlFile)) {
			try (InputStream propFileStream = newInputStream(xmlFile);) {
				prop.loadFromXML(propFileStream);
			}
			pb.setUsername(prop.getProperty("username"));
			pb.setEmailaddress(prop.getProperty("emailaddress"));
			pb.setEmailpassword(prop.getProperty("emailpassword"));
			pb.setName(prop.getProperty("name"));
			pb.setSmtpurl(prop.getProperty("smtpurl"));
			pb.setImapurl(prop.getProperty("imapurl"));
			pb.setHosturl(prop.getProperty("hosturl"));
			pb.setDatabasename(prop.getProperty("databasename"));
			pb.setPortnumber(prop.getProperty("portnumber"));
			pb.setUser(prop.getProperty("user"));
			pb.setPassword(prop.getProperty("password"));
		}
		return pb;
	}

	/**
	 * Creates an XML properties file based on the parameters
	 * 
	 * @param path
	 *            Must exist, will not be created
	 * @param propFileName
	 *            Name of the properties file. Empty string means root of
	 *            program folder
	 * @param dcb
	 *            The bean to store into the properties
	 * @throws IOException
	 */
	public final void writeXmlProperties(final String path, final String propFileName, final PropertiesBean pb) throws IOException {

		Properties prop = new Properties();

		prop.setProperty("username", pb.getUsername());
		prop.setProperty("emailaddress", pb.getEmailaddress());
		prop.setProperty("name", pb.getName());
		prop.setProperty("emailpassword", pb.getEmailpassword());
		prop.setProperty("smtpurl", pb.getSmtpurl());
		prop.setProperty("imapurl", pb.getImapurl());
		prop.setProperty("hosturl", pb.getHosturl());
		prop.setProperty("portnumber", String.valueOf(pb.getPortnumber()));
		prop.setProperty("databasename", pb.getDatabasename());
		prop.setProperty("user", pb.getUser());
		prop.setProperty("password", pb.getPassword());

		Path xmlFile = get(path, propFileName + ".xml");

		// Creates the file or if file exists it is truncated to length of zero
		// before writing
		try (OutputStream propFileStream = newOutputStream(xmlFile)) {
			prop.storeToXML(propFileStream, "XML JAG Properties");
		}
	}

	/**
	 * Retrieve the properties file. This syntax rather than normal File I/O is
	 * employed because the properties file is inside the jar. The technique
	 * shown here will work in both Java SE and Java EE environments. A similar
	 * technique is used for loading images.
	 * 
	 * In a Maven project all configuration files, images and other files go
	 * into src/main/resources. The files and folders placed there are moved to
	 * the root of the project when it is built.
	 * 
	 * @param propertiesFileName
	 *            : Name of the properties file
	 * @throws IOException
	 *             : Error while reading the file
	 * @throws NullPointerException
	 *             : File not found
	 * @return The bean loaded with the properties
	 */
	public final PropertiesBean loadJarTextProperties(final String propertiesFileName) throws IOException, NullPointerException {
		
		Properties prop = new Properties();
		PropertiesBean pb = new PropertiesBean();

		// There is no exists method for files in a jar so we try to get the
		// resource and if its not there it returns a null
		if (this.getClass().getResource("/" + propertiesFileName) != null) {
			// Assumes that the properties file is in the root of the
			// project/jar.
			// Include a path from the root if required.
			try (InputStream stream = this.getClass().getResourceAsStream("/" + propertiesFileName);) {
				prop.load(stream);
			}
			
			pb.setUsername(prop.getProperty("username"));
			pb.setEmailaddress(prop.getProperty("emailaddress"));
			pb.setEmailpassword(prop.getProperty("emailpassword"));
			pb.setName(prop.getProperty("name"));
			pb.setSmtpurl(prop.getProperty("smtpurl"));
			pb.setImapurl(prop.getProperty("imapurl"));
			pb.setHosturl(prop.getProperty("hosturl"));
			pb.setDatabasename(prop.getProperty("databasename"));
			pb.setPortnumber(prop.getProperty("portnumber"));
			pb.setUser(prop.getProperty("user"));
			pb.setPassword(prop.getProperty("password"));
			pb.setReceiveremailaddress(prop.getProperty("receiveremailaddress"));
			pb.setReceiverpassword(prop.getProperty("receiverpassword"));
		}
		return pb;
	}
}
