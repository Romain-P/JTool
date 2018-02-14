package org.jtool.backend;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RsaDecrypter {
	private String privateKey;
	
	public static RsaDecrypter withPrivateKey(String privateKey) {
		return new RsaDecrypter(privateKey);
	}

	private RsaDecrypter(String privateKey) {
		this.privateKey = privateKey;
	}
	
    /**
     * @param cipherText encrypted text to make human readable.
     * @return the text decrypted from the private key
     */
    public String decrypt(String cipherText, String padding) {
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
    
   
    public void setPrivateKey(String key) {
    	this.privateKey = key;
    }
}
