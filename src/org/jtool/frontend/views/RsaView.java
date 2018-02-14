package org.jtool.frontend.views;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.jtool.shared.ComponentUtil;
import org.jtool.shared.CryptagePadding;

@SuppressWarnings("serial")
public class RsaView extends JPanel {
	/**
	 * Settings fields
	 */
	private JPanel settings;
	private JLabel privateKeyLabel;
	private JLabel publicKeyLabel;
	private JLabel paddingLabel;
	private JTextPane privateKeyText;
	private JTextPane publicKeyText;
	private JScrollPane privateKeyScroller;
	private JScrollPane publicKeyScroller;
	private JComboBox<CryptagePadding> paddingList;
	private JButton generateButton;
	private JButton clearButton;
	
	/**
	 * Cryption fields
	 */
	private JPanel cryption;
	private JLabel cryptionInputLabel;
	private JLabel generatedLabel;
	private JTextPane cryptionInputText;
	private JTextPane generatedText;
	private JScrollPane cryptionInputScroller;
	private JScrollPane generatedScroller;
	private JButton encrypteButton;
	private JButton decrypteButton;
	
	public RsaView() {
		setLayout(null); // Absolute layout, no rules
		buildSettings(); // Build settings panel
		buildCryption(); // Build encryption / decryption panel
		handleEvents(); // Handle events on our components
	}
	
	private void handleEvents() {
		
	}
	
	private void buildCryption() {
		/**
		 * cryption properties
		 */
		cryption = new JPanel();
		cryption.setLayout(null);
		cryption.setName("");
		cryption.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Encryption / Decryption", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		cryption.setBounds(10, 265, 564, 224);
		
		/**
		 * cryption input
		 */
		cryptionInputLabel = new JLabel("Cypher / Plain Text");
		cryptionInputLabel.setBounds(10, 24, 110, 14);
		cryptionInputText = new JTextPane();
		cryptionInputText.setBounds(10, 43, 525, 45);
		cryptionInputScroller = new JScrollPane(cryptionInputText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		cryptionInputScroller.setBounds(10, 43, 525, 45);
		
		/**
		 * generated
		 */
		generatedLabel = new JLabel("Generated");
		generatedLabel.setBounds(10, 149, 71, 14);
		generatedText = new JTextPane();
		generatedText.setEditable(false);
		generatedText.setBounds(10, 168, 525, 45);
		generatedScroller = new JScrollPane(generatedText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		generatedScroller.setBounds(10, 168, 525, 45);
		
		/**
		 * buttons
		 */
		encrypteButton = new JButton("Encrypte");
		encrypteButton.setBounds(64, 96, 136, 23);
		decrypteButton = new JButton("Decrypte");
		decrypteButton.setBounds(382, 99, 136, 23);
		
		/**
		 * Add all to our user interface
		 */
		ComponentUtil.add(cryption, cryptionInputLabel, generatedLabel, cryptionInputText, generatedText, cryptionInputScroller, generatedScroller, encrypteButton, decrypteButton);
		ComponentUtil.add(this, cryption);
	}
	
	private void buildSettings() {
		/**
		 * settings properties
		 */
		settings = new JPanel();
		settings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		settings.setBounds(10, 11, 564, 224);
		settings.setLayout(null);
		
		/**
		 * private key
		 */
		privateKeyLabel = new JLabel("Private Key");
		privateKeyLabel.setBounds(10, 24, 71, 14);
		privateKeyText = new JTextPane();
		privateKeyText.setBounds(10, 43, 525, 45);
		privateKeyScroller = new JScrollPane(privateKeyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		privateKeyScroller.setBounds(10, 43, 525, 45);
		
		/**
		 * public key
		 */
		publicKeyLabel = new JLabel("Public Key");
		publicKeyLabel.setBounds(10, 105, 71, 14);
		publicKeyText = new JTextPane();
		publicKeyText.setBounds(10, 124, 525, 45);
		publicKeyScroller = new JScrollPane(publicKeyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		publicKeyScroller.setBounds(10, 124, 525, 45);
		
		/**
		 * Buttons
		 */
		generateButton = new JButton("Generate key pair");
		generateButton.setBounds(253, 190, 136, 23);
		clearButton = new JButton("Clear");
		clearButton.setBounds(399, 190, 136, 23);
		
		/**
		 * Padding
		 */
		paddingLabel = new JLabel("Padding");
		paddingLabel.setBounds(10, 175, 46, 14);
		paddingList = new JComboBox<>();
		paddingList.addItem(new CryptagePadding("PKCS1 1024/2048 bits", "RSA/ECB/PKCS1Padding"));
		paddingList.addItem(new CryptagePadding("OAEPWithSHA-1 + MGF1 1024/2048 bits", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"));
		paddingList.addItem(new CryptagePadding("OAEPWithSHA-256 + MGF1 1024/2048 bits", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"));
		paddingList.setBounds(10, 190, 233, 21);
		
		/**
		 * Add all to our user interface
		 */
		ComponentUtil.add(settings, privateKeyLabel, publicKeyLabel, privateKeyScroller, publicKeyScroller, generateButton, clearButton, paddingLabel, paddingList);
		ComponentUtil.add(this, settings);
	}
}
