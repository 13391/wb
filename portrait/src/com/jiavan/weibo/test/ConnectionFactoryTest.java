package com.jiavan.weibo.test;


import com.jiavan.weibo.util.ConnectionFactory;
import com.jiavan.weibo.util.Log;

import java.sql.Connection;

/**
 * Created by Jiavan on 2017/4/5.
 * <p>
 * Connection Factory Singleton unit test
 */
public class ConnectionFactoryTest {
    public static void main(String[] args) {
        try {
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();

            Log.i(connection.toString());
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
