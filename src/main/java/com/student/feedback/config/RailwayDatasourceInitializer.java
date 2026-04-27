package com.student.feedback.config;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class RailwayDatasourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String PROPERTY_SOURCE_NAME = "railwayDatasourceOverrides";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> overrides = new LinkedHashMap<>();

        String datasourceUrl = resolveDatasourceUrl(environment);
        if (hasText(datasourceUrl)) {
            overrides.put("spring.datasource.url", datasourceUrl);
        }

        String username = firstNonBlank(
            environment.getProperty("spring.datasource.username"),
            environment.getProperty("DB_USERNAME"),
            environment.getProperty("MYSQLUSER"),
            usernameFromUrl(environment.getProperty("MYSQL_PUBLIC_URL")),
            usernameFromUrl(environment.getProperty("DATABASE_URL"))
        );
        if (hasText(username)) {
            overrides.put("spring.datasource.username", username);
        }

        String password = firstNonBlank(
            environment.getProperty("spring.datasource.password"),
            environment.getProperty("DB_PASSWORD"),
            environment.getProperty("MYSQLPASSWORD"),
            passwordFromUrl(environment.getProperty("MYSQL_PUBLIC_URL")),
            passwordFromUrl(environment.getProperty("DATABASE_URL"))
        );
        if (hasText(password)) {
            overrides.put("spring.datasource.password", password);
        }

        if (!overrides.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, overrides));
        }
    }

    private String resolveDatasourceUrl(ConfigurableEnvironment environment) {
        String publicUrl = normalizeMysqlUrl(environment.getProperty("MYSQL_PUBLIC_URL"));
        if (hasText(publicUrl)) {
            return publicUrl;
        }

        String databaseUrl = normalizeMysqlUrl(environment.getProperty("DATABASE_URL"));
        if (isReachableExternalUrl(databaseUrl)) {
            return databaseUrl;
        }

        String datasourceUrl = normalizeMysqlUrl(environment.getProperty("spring.datasource.url"));
        if (isReachableExternalUrl(datasourceUrl)) {
            return datasourceUrl;
        }

        String mysqlUrl = normalizeMysqlUrl(environment.getProperty("MYSQL_URL"));
        if (isReachableExternalUrl(mysqlUrl)) {
            return mysqlUrl;
        }

        return firstNonBlank(databaseUrl, datasourceUrl, mysqlUrl);
    }

    private String normalizeMysqlUrl(String value) {
        if (!hasText(value)) {
            return null;
        }
        if (value.startsWith("jdbc:mysql://")) {
            return value;
        }
        if (value.startsWith("mysql://")) {
            return "jdbc:" + value;
        }
        return value;
    }

    private boolean isReachableExternalUrl(String value) {
        if (!hasText(value)) {
            return false;
        }

        try {
            URI uri = URI.create(value.replaceFirst("^jdbc:", ""));
            String host = uri.getHost();
            return hasText(host) && !host.endsWith(".internal");
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private String usernameFromUrl(String value) {
        return userInfoPart(value, 0);
    }

    private String passwordFromUrl(String value) {
        return userInfoPart(value, 1);
    }

    private String userInfoPart(String value, int index) {
        if (!hasText(value)) {
            return null;
        }

        try {
            URI uri = URI.create(value.replaceFirst("^jdbc:", ""));
            String userInfo = uri.getUserInfo();
            if (!hasText(userInfo)) {
                return null;
            }

            String[] parts = userInfo.split(":", 2);
            if (index >= parts.length || !hasText(parts[index])) {
                return null;
            }
            return parts[index];
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
