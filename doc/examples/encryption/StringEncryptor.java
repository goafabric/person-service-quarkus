package org.goafabric.personservice.persistence.encryption;


import jakarta.persistence.AttributeConverter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

//Source: https://sultanov.dev/blog/database-column-level-encryption-with-spring-data-jpa/
public class StringEncryptor implements AttributeConverter<String, String> {

    private static final String SECRET = "secret-key-12345"; //should be injected by @Value from a secure environment

    private final Key key;
    private final Cipher cipher;

    public StringEncryptor() throws Exception {
        key = new SecretKeySpec(SECRET.getBytes(), "AES");
        cipher = Cipher.getInstance("AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}