package com.jiavan.weibo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * MySQL connection factory / singleton
 * Created by Jiavan on 2017/3/29.
 */
public class ConnectionFactory {
    private static String driver;
    private static String dburl;
    private static String user;
    private static String password;

    private static final ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;

    static {
        Properties prop = null;
        try {
            prop = FileHandle.loadProp("config.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver = prop.getProperty("driver");
        dburl = prop.getProperty("dburl");
        user = prop.getProperty("user");
        password = prop.getProperty("password");
    }

    private ConnectionFactory() { }

    public static ConnectionFactory getInstance() {
        return factory;
    }

    public Connection getConnection() throws Exception{
        Class.forName(driver);
        this.connection = DriverManager.getConnection(dburl, user, password);
        return this.connection;
    }
}