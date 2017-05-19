package com.jiavan.weibo.crawler.test;

import com.jiavan.weibo.crawler.crawler.UserList;
import com.jiavan.weibo.crawler.entity.UserRefImpl;
import com.jiavan.weibo.crawler.dao.UserRef;
import com.jiavan.weibo.crawler.jdbc.ConnectionFactory;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Crawler UserList unit test
 * Created by Jiavan on 2017/4/9.
 */
public class UserListTest {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        try {
            UserList userList = new UserList(new Long("3855494782"), 1);
            ArrayList<UserRef> arrayList = userList.crawler();
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();
            UserRefImpl userRefImpl = new UserRefImpl(connection);

            for (UserRef userRef : arrayList) {
                userRefImpl.insert(userRef);
                System.out.println(userRef.toString());
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
