package org.jtool.backend;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.jtool.shared.Crypter;

public class RsaEncrypter implements Crypter {
	private String publicKey;
	private String padding;
	
	public static RsaEncrypter of(String publicKey, String padding) {
		return new RsaEncrypter(publicKey, padding);
	}

	private RsaEncrypter(String publicKey, String padding) {
		this.publicKey = publicKey;
		this.padding = padding;
	}
	
    /**
     * @param plaintext human readable text to encrypt.
     * @return the text encrypted from the public key
     */
    public String apply(String plaintext) {
        if (this.publicKey == null)
            return "Can't encrypt the message, you didn't specify the public key.";

        Cipher cipher;
		try {
			cipher = Cipher.getInstance(padding);
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
    
    public RsaEncrypter setKey(String key) {
    	this.publicKey = key;
    	return this;
    }
    
    public RsaEncrypter setPadding(String padding) {
    	this.padding = padding;
    	return this;
    }
}
