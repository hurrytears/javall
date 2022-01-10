package com.apachee.core;

import org.apache.hadoop.conf.Configuration;

public interface KmsClient {
    // Wraps a key - encrypts it with the master key.
    public String wrapKey(byte[] keyBytes, String masterKeyIdentifier);

    // Decrypts (unwraps) a key with the master key.
    public byte[] unwrapKey(String wrappedKey, String masterKeyIdentifier);

    // Use of initialization parameters is optional.
    public void initialize(Configuration configuration, String kmsInstanceID,
                           String kmsInstanceURL, String accessToken);
}
