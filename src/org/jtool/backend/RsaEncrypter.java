package org.jtool.backend;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RsaEncrypter {
	private String publicKey;
	
	public static RsaEncrypter withPublicKey(String publicKey) {
		return new RsaEncrypter(publicKey);
	}

	private RsaEncrypter(String publicKey) {
		this.publicKey = publicKey;
	}
	
    /**
     * @param plaintext human readable text to encrypt.
     * @return the text encrypted from the public key
     */
    public String encrypt(String plaintext) {
        if (this.publicKey == null)
            return "Can't encrypt the message, you didn't specify the public key.";

        Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (Exception e) {
			return "no rsa algorithm found: " + e.getMessage();
		}
		
        try {
			cipher.init(Cipher.ENCRYPT_MODE, parsePublicKey(publicKey));
		} catch (Exception e) {
			return "error while parsing public key: " + e.getMessage();
		}

        byte[] bytes;
        
		try {
			bytes = cipher.doFinal(plaintext.getBytes(Charset.forName("UTF-8")));
		} catch (Exception e) {
			return "error while rsa encrypting: " + e.getMessage();
		} 

        return Base64.getEncoder().encodeToString(bytes);
    }
	
	
    private PublicKey parsePublicKey(String base64Key) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(base64Key.getBytes());
        KeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePublic(spec);
    }
    
    public void setPublicKey(String key) {
    	this.publicKey = key;
    }
}
