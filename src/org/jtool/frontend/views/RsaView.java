package org.jtool.frontend.views;

import org.jtool.backend.Backend;
import org.jtool.backend.RsaGenerator;
import org.jtool.shared.ComponentUtil;
import org.jtool.shared.CryptagePadding;
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
        ComponentUtil.onMouseReleased(() -> generateRsaResult(Backend.get().getRsaEncrypter(), settings.publicKeyText), cryption.encrypteButton);
        ComponentUtil.onMouseReleased(() -> generateRsaResult(Backend.get().getRsaDecrypter(), settings.privateKeyText), cryption.decrypteButton);
        ComponentUtil.onMouseReleased(() -> generateKeyPair(), settings.generateButton);
    }

    private void applyErrorProtection() {
        boolean cryptionPossible = !settings.publicKeyText.getText().isEmpty() && !cryption.cryptionInputText.getText().isEmpty();
        cryption.encrypteButton.setEnabled(cryptionPossible);

        boolean decryptionPossible = !settings.privateKeyText.getText().isEmpty() && !cryption.cryptionInputText.getText().isEmpty();
        cryption.decrypteButton.setEnabled(decryptionPossible);

        boolean clearIsUseful = !settings.publicKeyText.getText().isEmpty() || !settings.privateKeyText.getText().isEmpty();
        settings.clearButton.setEnabled(clearIsUseful);

        boolean generatePossible = settings.publicKeyText.getText().isEmpty() && settings.privateKeyText.getText().isEmpty();
        settings.generateButton.setEnabled(generatePossible);
    }

    private void handleClearButton() {
        settings.publicKeyText.setText("");
        ;
        settings.privateKeyText.setText("");
        settings.generateButton.setEnabled(true);
        settings.clearButton.setEnabled(false);
    }

    private void generateKeyPair() {
        RsaGenerator generator = Backend.get().getRsaGenerator();
        RsaGenerator.Base64KeyPair pair = generator.generate();

        settings.privateKeyText.setText(pair.privateKey);
        settings.publicKeyText.setText(pair.publicKey);
        settings.clearButton.setEnabled(true);

        if (!generator.errorOccured())
            settings.generateButton.setEnabled(false);
    }

    private void generateRsaResult(Crypter crypter, JTextArea keyPane) {
        String key = keyPane.getText();
        String padding = ((CryptagePadding) settings.paddingList.getSelectedItem()).getPath();
        crypter.setKey(key).setPadding(padding);

        String charset = cryption.cryptionInputText.getText();
        String result = crypter.apply(charset);

        cryption.generatedText.setText(result);
    }

    private static class SettingsPanel {
        private final JPanel settings;
        private final JLabel privateKeyLabel;
        private final JLabel publicKeyLabel;
        private final JLabel paddingLabel;
        private final JTextArea privateKeyText;
        private final JTextArea publicKeyText;
        private final JScrollPane privateKeyScroller;
        private final JScrollPane publicKeyScroller;
        private final JComboBox<CryptagePadding> paddingList;
        private final JButton generateButton;
        private final JButton clearButton;

        public SettingsPanel(RsaView parent) {
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
            for (CryptagePadding padding : Backend.get().getRsaPaddings())
                paddingList.addItem(padding);
            paddingList.setBounds(10, 190, 233, 21);
            paddingList.setSelectedIndex(1);

            /**
             * Add all to our user interface
             */
            ComponentUtil.add(settings, privateKeyLabel, publicKeyLabel, privateKeyScroller, publicKeyScroller, generateButton, clearButton, paddingLabel, paddingList);
            ComponentUtil.add(parent, settings);
        }
    }

    private static class CryptionPanel {
        private final JPanel cryption;
        private final JLabel cryptionInputLabel;
        private final JLabel generatedLabel;
        private final JTextArea cryptionInputText;
        private final JTextArea generatedText;
        private final JScrollPane cryptionInputScroller;
        private final JScrollPane generatedScroller;
        private final JButton encrypteButton;
        private final JButton decrypteButton;

        public CryptionPanel(RsaView parent) {
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
            encrypteButton.setBounds(64, 96, 136, 23);
            encrypteButton.setEnabled(false);
            decrypteButton = new JButton("Decrypte");
            decrypteButton.setBounds(382, 99, 136, 23);
            decrypteButton.setEnabled(false);

            /**
             * Add all to our user interface
             */
            ComponentUtil.add(cryption, cryptionInputLabel, generatedLabel, cryptionInputScroller, generatedScroller, encrypteButton, decrypteButton);
            ComponentUtil.add(parent, cryption);
        }
    }
}
