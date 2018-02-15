package org.jtool.backend;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RsaGenerator {
	private final int length;
	private boolean errorOccured;
	
	public static RsaGenerator withLength(int length) {
		return new RsaGenerator(length);
	}
	
	private RsaGenerator(int length) {
		this.length = length;
	}
	
	public Base64KeyPair generate() {
        KeyPairGenerator generator;
        
		try {
			generator = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			String error = "error while generating rsa key-pair: " + e.getMessage();
			errorOccured = true;
			return new Base64KeyPair(error, error);
		}
		
        generator.initialize(length); 
        KeyPair pair = generator.generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        
        return new Base64KeyPair(privateKey, publicKey);
	}
	
	/**
	 * @return true if an error occured in the last generation
	 */
	public boolean errorOccured() {
		return this.errorOccured;
	}
	
	public static final class Base64KeyPair {
		public final String privateKey;
		public final String publicKey;
		
		public Base64KeyPair(String privateKey, String publicKey) {
			this.privateKey = privateKey;
			this.publicKey = publicKey;
		}
	}
}
