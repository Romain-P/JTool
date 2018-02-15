package org.jtool.shared;

public final class CryptionPadding {
    private final String alias;
    private final String javaPath;

    /**
     * @param alias    the alias of a given padding name e.g PKCS1
     * @param javaPath the defined path in the java standard libraries e.g RSA/ECB/PKCS1Padding
     */
    public CryptionPadding(String alias, String javaPath) {
        this.alias = alias;
        this.javaPath = javaPath;
    }

    public String getPath() {
        return this.javaPath;
    }

    @Override
    public String toString() {
        return this.alias;
    }
}
