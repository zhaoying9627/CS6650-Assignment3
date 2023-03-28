package org.example;
import org.apache.commons.dbcp2.*;

public class DBCPDataSource {
    private static BasicDataSource dataSource;

    // Set configuration
    private static final String HOST_NAME = "database1.cepbjjfe2ube.us-west-2.rds.amazonaws.com";
    private static final String PORT = "3306";
    private static final String DATABASE = "twinder";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    static {
        // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
        dataSource = new BasicDataSource();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
        dataSource.setUrl(url);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(60);
    }

    public static BasicDataSource getDataSource() {
        return dataSource;
    }
}