package org.jtool.frontend;

import javax.swing.JTabbedPane;

import org.jtool.frontend.views.AesView;
import org.jtool.frontend.views.HashView;
import org.jtool.frontend.views.RsaView;

@SuppressWarnings("serial")
public class MainView extends JTabbedPane {

	/**
	 * Create the main view that contains all our module tabs.
	 */
	public MainView() {
		addTab("Module RSA", null, new RsaView(), "Encryption features with rsa key pair");
		addTab("Module AES", null, new AesView(), "Encryption features with aes key");
		addTab("Module HASH", null, new HashView(), "Hash generation from readable text");
	}
}
