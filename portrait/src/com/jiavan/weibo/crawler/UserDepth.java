package com.jiavan.weibo.crawler;

import com.jiavan.weibo.entity.UserRefImpl;
import com.jiavan.weibo.dao.UserRef;
import com.jiavan.weibo.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Crawler userRef by depth
 * Created by Jiavan on 2017/4/9.
 */
public class UserDepth {
    // user's depth
    private int depth;

    public UserDepth(int depth) {
        this.depth = depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return this.depth;
    }

    public void crawler() throws Exception {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Connection connection = cf.getConnection();

        String sql = "SELECT * FROM user_ref WHERE depth=" + this.depth;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<UserRef> arrayList;
        long uid;
        UserList userList;
        UserRefImpl refImpl;

        while (resultSet.next()) {
            uid = resultSet.getLong("uid");
            userList = new UserList(uid, this.depth);
            arrayList = userList.crawler();

            if (arrayList != null) {
                refImpl = new UserRefImpl(connection);

                for (UserRef userRef : arrayList) {
                    refImpl.insert(userRef);
                }
            }
        }

        statement.close();
        connection.close();
    }
}
