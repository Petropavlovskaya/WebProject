package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public enum ApplicationConfiguration {
    INSTANCE;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private int initPoolSize = 12;
    private int maxPoolSize = 30;
    private int poolIncreaseStep = 2;

    ApplicationConfiguration() {
        initProperties();
    }

    private void initProperties() {
        try (InputStream inputStream = new FileInputStream("src/main/resources/application.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            dbUrl = properties.getProperty("jdbc.url");
            dbUser = properties.getProperty("jdbc.user");
            dbPassword = properties.getProperty("jdbc.password");
            if (Objects.nonNull(properties.getProperty("initPoolSize"))) {
                initPoolSize = Integer.parseInt(properties.getProperty("initPoolSize"));
            }
            if (Objects.nonNull(properties.getProperty("maxPoolSize"))) {
                maxPoolSize = Integer.parseInt(properties.getProperty("maxPoolSize"));
            }
            if (Objects.nonNull(properties.getProperty("poolIncreaseStep"))) {
                poolIncreaseStep = Integer.parseInt(properties.getProperty("poolIncreaseStep"));
            }
                                                                                    System.out.println("Property loaded successful");
        } catch (IOException e) {
//            log.error("Properties has not been loaded", e);
            throw new Error("Properties has not been loaded", e);
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public int getInitPoolSize() {
        return initPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getPoolIncreaseStep() {
        return poolIncreaseStep;
    }

    @Override
    public String toString() {
        return "ApplicationConfiguration{" +
                "dbUrl='" + dbUrl + '\'' +
                ", dbUser='" + dbUser + '\'' +
                ", dbPassword='" + dbPassword + '\'' +
                ", initPoolSize=" + initPoolSize + '\'' +
                ", maxPoolSize=" + maxPoolSize + '\'' +
                ", poolIncreaseStep=" + poolIncreaseStep +
                '}';
    }
}
