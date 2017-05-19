package com.jiavan.weibo.crawler.entity;

import com.jiavan.weibo.crawler.dao.UserRef;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * UserRef DAO implement
 * Created by Jiavan on 2017/4/8.
 */
public class UserRefImpl {
    private Connection connection;
    private UserRef userRef;

    public UserRefImpl(Connection connection) {
        this.connection = connection;
    }
    public UserRefImpl(Connection connection, UserRef userRef) {
        this.connection = connection;
        this.userRef = userRef;
    }

    /**
     * Insert a UserRef
     * @param userRef userref instance
     * @return Affected rows
     * @throws Exception SQL exception
     */
    public int insert(UserRef userRef) throws Exception {

        String sql = "insert into user_ref(uid, username, depth) values(?, ?, ?)";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setLong(1, userRef.getUid());
        statement.setString(2, userRef.getUsername());
        statement.setInt(3, userRef.getDepth());

        int rows = statement.executeUpdate();
        statement.close();

        return rows;
    }
}
