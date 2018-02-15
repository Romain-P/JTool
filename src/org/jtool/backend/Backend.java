package org.jtool.backend;

import java.util.HashSet;
import java.util.Set;

import org.jtool.shared.CryptagePadding;
import org.jtool.shared.Crypter;

public class Backend {
	private final Crypter rsaDecrypter;
	private final Crypter rsaEncrypter;
	private final RsaGenerator rsa2048Generator;
	private final Set<CryptagePadding> rsaPaddings;
	
	private Backend() {
		this.rsaDecrypter = RsaDecrypter.of(null, null);
		this.rsaEncrypter = RsaEncrypter.of(null, null);
		this.rsa2048Generator = RsaGenerator.withLength(2048);
		this.rsaPaddings = new HashSet<>();
	}
	
	public Backend initialize() {
		rsaPaddings.add(new CryptagePadding("PKCS1 1024/2048 bits", "RSA/ECB/PKCS1Padding"));
		rsaPaddings.add(new CryptagePadding("OAEPWithSHA-1 + MGF1 1024/2048 bits", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"));
		rsaPaddings.add(new CryptagePadding("OAEPWithSHA-256 + MGF1 1024/2048 bits", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"));
		
		return this;
	}
	
	public Crypter getRsaDecrypter() {
		return this.rsaDecrypter;
	}
	
	public Crypter getRsaEncrypter() {
		return this.rsaEncrypter;
	}
	
	public RsaGenerator getRsaGenerator() {
		return this.rsa2048Generator;
	}
	
	public Set<CryptagePadding> getRsaPaddings() {
		return this.rsaPaddings;
	}
	
	/**
	 * Create a lazy-loaded singleton of our backend, thread-safe.
	 */
	private static class BackendHolder {
		private static final Backend instance = new Backend().initialize();
	}
	
	/**
	 * Call this method to get the instance of our backend.
	 */
	public static Backend get() {
		return BackendHolder.instance;
	}
}
