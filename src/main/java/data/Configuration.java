package data;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final Properties properties;

    static {

        String passwordFromEnv = System.getenv("CONFIG_SECRET");

        if (passwordFromEnv == null) {
            throw new IllegalStateException("CONFIG_SECRET environment variable was not found");
        }

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(passwordFromEnv);
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());

        properties = new EncryptableProperties(encryptor);
        try {
            properties.load(Configuration.class.getClassLoader().getResourceAsStream("configuration.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getConfig(String propertyKey) {
        return properties.getProperty(propertyKey);
    }

    public static Integer getConfigNumber(String propertyKey) {
        return Integer.parseInt(properties.getProperty(propertyKey));
    }


}
