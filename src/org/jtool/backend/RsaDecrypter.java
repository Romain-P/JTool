package org.jtool.backend;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.jtool.shared.Crypter;

public class RsaDecrypter implements Crypter {
	private String privateKey;
	private String padding;
	
	public static RsaDecrypter of(String privateKey, String padding) {
		return new RsaDecrypter(privateKey, padding);
	}

	private RsaDecrypter(String privateKey, String padding) {
		this.privateKey = privateKey;
		this.padding = padding;
	}
	
    /**
     * @param cipherText encrypted text to make human readable.
     * @return the text decrypted from the private key
     */
    public String apply(String cipherText) {
        if (this.privateKey == null)
            return "Can't decrypt the message, you didn't specify the private key.";

        
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher cipher;
        
		try {
			cipher = Cipher.getInstance(padding);
		} catch (Exception e) {
			return "no rsa algorithm found: " + e.getMessage();
		}
        
        try {
			cipher.init(Cipher.DECRYPT_MODE, parsePrivateKey(privateKey));
		} catch (Exception e) {
			return "error while parsing private key: " + e.getMessage();
		}

        try {
			return new String(cipher.doFinal(bytes), Charset.forName("UTF-8"));
		} catch (Exception e) {
			return "error while rsa decrypting: " + e.getMessage();
		}
    }
	
    private PrivateKey parsePrivateKey(String base64key) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(base64key.getBytes());
        KeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePrivate(spec);
    }
    
   
    public RsaDecrypter setKey(String key) {
    	this.privateKey = key;
    	return this;
    }
    
    public RsaDecrypter setPadding(String padding) {
    	this.padding = padding;
    	return this;
    }
}
