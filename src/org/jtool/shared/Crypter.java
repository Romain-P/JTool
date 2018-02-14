package org.jtool.shared;

public interface Crypter {
	Crypter setKey(String key);
	Crypter setPadding(String padding);
	String apply(String charset);
}
