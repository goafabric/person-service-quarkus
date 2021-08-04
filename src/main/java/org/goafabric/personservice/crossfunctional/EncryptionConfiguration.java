package org.goafabric.personservice.crossfunctional;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEEncryptorRegistry;
import org.jasypt.iv.IvGenerator;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;
import java.util.UUID;

@RegisterForReflection(targets= {
        java.text.Normalizer.class,
        java.text.Normalizer.Form.class
        //com.ibm.icu.text.Normalizer.class
})
@Slf4j
public class EncryptionConfiguration {
    @ConfigProperty(name = "security.encryption.key", defaultValue = " ")
    String encryptionKey;
    
    @Startup
    @Produces
    @ApplicationScoped
    public StandardPBEStringEncryptor hibernateEncryptor() {
        final StandardPBEStringEncryptor encryptor =
                getAES256Encryptor(getEncryptionKey(), new RandomIvGenerator(), new RandomSaltGenerator());

        HibernatePBEEncryptorRegistry.getInstance()
                .registerPBEStringEncryptor("hibernateStringEncryptor", encryptor);

        return encryptor;
    }

    private StandardPBEStringEncryptor getAES256Encryptor(String configKey, IvGenerator ivGenerator, SaltGenerator saltGenerator) {
        final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator(ivGenerator);
        encryptor.setSaltGenerator(saltGenerator);
        encryptor.setPassword(configKey);
        return encryptor;
    }

    private String getEncryptionKey() {
        if (" ".equals(encryptionKey)) {
            log.warn("security.encryption.key is empty, generating one for temporary usage");
        }
        return " ".equals(encryptionKey) ? UUID.randomUUID().toString() : encryptionKey;
    }

}
