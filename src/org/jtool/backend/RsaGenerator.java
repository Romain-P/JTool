package org.jtool.backend;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RsaGenerator {
    private final int length;

    private RsaGenerator(int length) {
        this.length = length;
    }

    public static RsaGenerator withLength(int length) {
        return new RsaGenerator(length);
    }

    public Base64KeyPair generate() {
        KeyPairGenerator generator;

        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            String error = "error while generating rsa key-pair: " + e.getMessage();
            return new Base64KeyPair(error, error);
        }

        generator.initialize(length);
        KeyPair pair = generator.generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());

        return new Base64KeyPair(privateKey, publicKey);
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
