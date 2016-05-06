/**
 * 
 */
package com.kfcstd.jag_phase3.javafxWebView.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

/**
 * @author 
 *
 */
public class WebViewController {
	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	@FXML
    private WebView webviewPane;
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. 
	 */
	@FXML
	public void initialize(String filename) {
		final String html = filename;
        final java.net.URI uri = java.nio.file.Paths.get(html).toAbsolutePath().toUri();
        log.info("uri= " + uri.toString());
 
        // create WebView with specified local content
        webviewPane.getEngine().load(uri.toString());
	}
}
