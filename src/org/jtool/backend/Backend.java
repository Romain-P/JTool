package org.jtool.backend;

public class Backend {
	private final RsaDecrypter rsaDecrypter;
	private final RsaEncrypter rsaEncrypter;
	
	private Backend() {
		this.rsaDecrypter = RsaDecrypter.withPrivateKey(null);
		this.rsaEncrypter = RsaEncrypter.withPublicKey(null);
	}
	
	public RsaDecrypter getRsaDecrypter() {
		return this.rsaDecrypter;
	}
	
	public RsaEncrypter getRsaEncrypter() {
		return this.rsaEncrypter;
	}
	
	/**
	 * Create a lazy-loaded singleton of our backend, thread-safe.
	 */
	private static class BackendHolder {
		private static final Backend instance = new Backend();
	}
	
	/**
	 * Call this method to get the instance of our backend.
	 */
	public static Backend get() {
		return BackendHolder.instance;
	}
}
