package com.jiavan.weibo.test;


import com.jiavan.weibo.util.ConnectionFactory;

import java.sql.Connection;

/**
 * Connection Factory Singleton unit test
 * Created by Jiavan on 2017/4/5.
 */
public class ConnectionFactoryTest {
    public static void main(String[] args) {
        try {
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();

            System.out.print(connection);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
