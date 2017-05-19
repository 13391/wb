package com.jiavan.weibo.crawler.test;

import com.jiavan.weibo.crawler.crawler.UserList;
import com.jiavan.weibo.crawler.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * UserList crawler test
 * Created by Jiavan on 2017/4/8.
 */
public class UserInfoTest {
    public static void main(String[] args) {
        try {
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();
            ResultSet resultSet = null;

            Statement statement = connection.createStatement();
            String sql = "select * from user_ref where depth=1";
            resultSet = statement.executeQuery(sql);

            if (resultSet.first()) {
                UserList userList = new UserList(resultSet.getLong("uid"));
                userList.crawler();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
