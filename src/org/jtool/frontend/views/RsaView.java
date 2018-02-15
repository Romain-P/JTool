package org.jtool.frontend.views;

import org.jtool.backend.Backend;
import org.jtool.backend.RsaGenerator;
import org.jtool.shared.ComponentUtil;
import org.jtool.shared.CryptionPadding;
import org.jtool.shared.Crypter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SuppressWarnings("serial")
public class RsaView extends JPanel {
    private final SettingsPanel settings;
    private final CryptionPanel cryption;

    public RsaView() {
        setLayout(null); // Absolute layout, no rules

        this.settings = new SettingsPanel(this);
        this.cryption = new CryptionPanel(this);

        handleEvents(); // Handle events on our components
    }

    private void handleEvents() {
        ComponentUtil.onKeyReleased(() -> applyErrorProtection(), settings.privateKeyText, settings.publicKeyText, cryption.cryptionInputText);
        ComponentUtil.onMouseReleased(() -> handleClearButton(), settings.clearButton);
        ComponentUtil.onMouseReleased(() -> handleGeneratedClearButton(), cryption.genClearButton);
        ComponentUtil.onMouseReleased(() -> handleInputClearButton(), cryption.clearButton);
        ComponentUtil.onMouseReleased(() -> generateRsaResult(Backend.get().getRsaEncrypter(), settings.publicKeyText), cryption.encrypteButton);
        ComponentUtil.onMouseReleased(() -> generateRsaResult(Backend.get().getRsaDecrypter(), settings.privateKeyText), cryption.decrypteButton);
        ComponentUtil.onMouseReleased(() -> generateKeyPair(), settings.generateButton);
        ComponentUtil.onMouseReleased(() -> cryption.cryptionInputText.setText(cryption.generatedText.getText()), cryption.useGeneratedButton);
    }

    private void applyErrorProtection() {
        boolean cryptionPossible = !settings.publicKeyText.getText().isEmpty() && !cryption.cryptionInputText.getText().isEmpty();
        cryption.encrypteButton.setEnabled(cryptionPossible);

        boolean decryptionPossible = !settings.privateKeyText.getText().isEmpty() && !cryption.cryptionInputText.getText().isEmpty();
        cryption.decrypteButton.setEnabled(decryptionPossible);

        boolean clearIsUseful = !settings.publicKeyText.getText().isEmpty() || !settings.privateKeyText.getText().isEmpty();
        settings.clearButton.setEnabled(clearIsUseful);
        
        boolean inputClearIsUseful = !cryption.cryptionInputText.getText().isEmpty();
        cryption.clearButton.setEnabled(inputClearIsUseful);
        
        boolean genClearIsUseful = !cryption.generatedText.getText().isEmpty();
        cryption.genClearButton.setEnabled(genClearIsUseful);

        boolean generatePossible = settings.publicKeyText.getText().isEmpty() && settings.privateKeyText.getText().isEmpty();
        settings.generateButton.setEnabled(generatePossible);
        
        boolean useGeneratedPossible = !cryption.generatedText.getText().isEmpty();
        cryption.useGeneratedButton.setEnabled(useGeneratedPossible);
    }

    private void handleClearButton() {
        settings.publicKeyText.setText("");
        settings.privateKeyText.setText("");
        applyErrorProtection();
    }
    
    private void handleGeneratedClearButton() {
        cryption.generatedText.setText("");
        applyErrorProtection();
    }
    
    private void handleInputClearButton() {
        cryption.cryptionInputText.setText("");
        applyErrorProtection();
    }

    private void generateKeyPair() {
        RsaGenerator generator = Backend.get().getRsaGenerator();
        RsaGenerator.Base64KeyPair pair = generator.generate();

        settings.privateKeyText.setText(pair.privateKey);
        settings.publicKeyText.setText(pair.publicKey);
        
        applyErrorProtection();
    }

    private void generateRsaResult(Crypter crypter, JTextArea keyPane) {
        String key = keyPane.getText();
        String padding = ((CryptionPadding) settings.paddingList.getSelectedItem()).getPath();
        crypter.setKey(key).setPadding(padding);

        String charset = cryption.cryptionInputText.getText();
        String result;
        
        try {
        	result = crypter.apply(charset);
        } catch (Exception e) {
        	result = "unknown error occured: " + e.getMessage();
        }

        cryption.generatedText.setText(result);
        applyErrorProtection();
    }

    private static class SettingsPanel extends JPanel {
        private final JLabel privateKeyLabel;
        private final JLabel publicKeyLabel;
        private final JLabel paddingLabel;
        private final JTextArea privateKeyText;
        private final JTextArea publicKeyText;
        private final JScrollPane privateKeyScroller;
        private final JScrollPane publicKeyScroller;
        private final JComboBox<CryptionPadding> paddingList;
        private final JButton generateButton;
        private final JButton clearButton;

        public SettingsPanel(RsaView parent) {
            /**
             * settings properties
             */
            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            setBounds(10, 11, 564, 224);
            setLayout(null);

            /**
             * private key
             */
            privateKeyLabel = new JLabel("Private Key");
            privateKeyLabel.setBounds(10, 24, 71, 14);
            privateKeyText = new JTextArea();
            privateKeyText.setLineWrap(true);
            privateKeyText.setBounds(10, 43, 525, 45);
            privateKeyScroller = new JScrollPane(privateKeyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            privateKeyScroller.setBounds(10, 43, 525, 45);

            /**
             * public key
             */
            publicKeyLabel = new JLabel("Public Key");
            publicKeyLabel.setBounds(10, 105, 71, 14);
            publicKeyText = new JTextArea();
            publicKeyText.setLineWrap(true);
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
            clearButton.setEnabled(false);

            /**
             * Padding
             */
            paddingLabel = new JLabel("Padding");
            paddingLabel.setBounds(10, 175, 46, 14);
            paddingList = new JComboBox<>();
            for (CryptionPadding padding : Backend.get().getRsaPaddings())
                paddingList.addItem(padding);
            paddingList.setBounds(10, 190, 233, 21);
            paddingList.setSelectedIndex(1);

            /**
             * Add all to our user interface
             */
            ComponentUtil.add(this, privateKeyLabel, publicKeyLabel, 
            		privateKeyScroller, publicKeyScroller, generateButton, 
            		clearButton, paddingLabel, paddingList);
            
            ComponentUtil.add(parent, this);
        }
    }

    private static class CryptionPanel extends JPanel {
        private final JLabel cryptionInputLabel;
        private final JLabel generatedLabel;
        private final JTextArea cryptionInputText;
        private final JTextArea generatedText;
        private final JScrollPane cryptionInputScroller;
        private final JScrollPane generatedScroller;
        private final JButton encrypteButton;
        private final JButton decrypteButton;
        private final JButton useGeneratedButton;
        private final JButton clearButton;
        private final JButton genClearButton;

        public CryptionPanel(RsaView parent) {
            /**
             * cryption properties
             */
            setLayout(null);
            setName("");
            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Encryption / Decryption", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            setBounds(10, 265, 564, 258);

            /**
             * cryption input
             */
            cryptionInputLabel = new JLabel("Cypher / Plain Text");
            cryptionInputLabel.setBounds(10, 24, 110, 14);
            cryptionInputText = new JTextArea();
            cryptionInputText.setLineWrap(true);
            cryptionInputText.setBounds(10, 43, 525, 45);
            cryptionInputScroller = new JScrollPane(cryptionInputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            cryptionInputScroller.setBounds(10, 43, 525, 45);

            /**
             * generated
             */
            generatedLabel = new JLabel("Generated");
            generatedLabel.setBounds(10, 149, 71, 14);
            generatedText = new JTextArea();
            generatedText.setLineWrap(true);
            generatedText.setEditable(false);
            generatedText.setBounds(10, 168, 525, 45);
            generatedScroller = new JScrollPane(generatedText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            generatedScroller.setBounds(10, 168, 525, 45);

            /**
             * buttons
             */
            encrypteButton = new JButton("Encrypte");
            encrypteButton.setBounds(10, 96, 136, 23);
            encrypteButton.setEnabled(false);
            decrypteButton = new JButton("Decrypte");
            decrypteButton.setBounds(160, 96, 136, 23);
            decrypteButton.setEnabled(false);
            useGeneratedButton = new JButton("Use Generated");
            useGeneratedButton.setBounds(10, 223, 136, 23);
            useGeneratedButton.setEnabled(false);
            genClearButton = new JButton("Clear");
            genClearButton.setBounds(399, 223, 136, 23);
            genClearButton.setEnabled(false);
            clearButton = new JButton("Clear");
            clearButton.setBounds(399, 96, 136, 23);
            clearButton.setEnabled(false);

            /**
             * Add all to our user interface
             */
            ComponentUtil.add(this, cryptionInputLabel, generatedLabel, 
            		cryptionInputScroller, generatedScroller, encrypteButton, 
            		decrypteButton, useGeneratedButton, genClearButton, clearButton);
            
            ComponentUtil.add(parent, this);
        }
    }
}
