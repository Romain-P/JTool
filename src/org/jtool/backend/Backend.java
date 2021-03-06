package org.jtool.backend;

import org.jtool.shared.CryptionPadding;
import org.jtool.shared.Crypter;

import java.util.HashSet;
import java.util.Set;

public class Backend {
    private final Crypter rsaDecrypter;
    private final Crypter rsaEncrypter;
    private final RsaGenerator rsa2048Generator;
    private final Set<CryptionPadding> rsaPaddings;

    private Backend() {
        this.rsaDecrypter = RsaDecrypter.of(null, null);
        this.rsaEncrypter = RsaEncrypter.of(null, null);
        this.rsa2048Generator = RsaGenerator.withLength(2048);
        this.rsaPaddings = new HashSet<>();
    }

    /**
     * Call this method to get the instance of our backend.
     */
    public static Backend get() {
        return BackendHolder.instance;
    }

    public Backend initialize() {
        rsaPaddings.add(new CryptionPadding("PKCS1 1024/2048 bits", "RSA/ECB/PKCS1Padding"));
        rsaPaddings.add(new CryptionPadding("OAEPWithSHA-1 + MGF1 1024/2048 bits", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"));
        rsaPaddings.add(new CryptionPadding("OAEPWithSHA-256 + MGF1 1024/2048 bits", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"));

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

    public Set<CryptionPadding> getRsaPaddings() {
        return this.rsaPaddings;
    }

    /**
     * Create a lazy-loaded singleton of our backend, thread-safe.
     */
    private static class BackendHolder {
        private static final Backend instance = new Backend().initialize();
    }
}
