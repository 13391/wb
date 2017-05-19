package com.jiavan.weibo.crawler.entity;

import com.jiavan.weibo.crawler.dao.User;
import com.jiavan.weibo.crawler.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;

/**
 * User data access object implement
 * Created by Jiavan on 2017/4/4.
 */
public class UserImpl {
    private Connection connection;

    /**
     * @param connection SQL connection handle
     */
    public UserImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Insert a user data
     * @param user User entity
     * @return Affect rows
     * @throws Exception SQL exception
     */
    public int insert(User user) throws Exception {

        String sql = "INSERT INTO user(uid, avatar, username, home_page, gender, address, introduction, " +
                "company, school, blog, level, credit, reg_time, follow_count, fans_count, birthday, tweet_count, " +
                "job, tags, last_modify) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        this.setStatement(statement, user);
        int rows = statement.executeUpdate();

        if (rows > 0) {
            Log.i("Affect " + rows + "rows");
        }
        statement.close();

        return rows;
    }

    public int update(User user) throws Exception {

        String sql = "UPDATE user uid=?, avatar=?, username=?, home_page=?, gender=?, address=?, " +
                "introduction=?, company=?, school=?, blog=?, level=?, credit=?, reg_time=?, follow_count=?," +
                "fans_count=?, birthday=?, tweet_count=?, job=?, tags=?, last_modify=? where id=" + user.getId();
        PreparedStatement statement = connection.prepareStatement(sql);
        this.setStatement(statement, user);
        int rows = statement.executeUpdate();

        if (rows > 0) {
            Log.i("Affect " + rows + "rows");
        }
        statement.close();

        return rows;
    }

    /**
     * @param user User entity
     * @return Affect rows
     * @throws Exception SQL exception
     */
    public int delete(User user) throws Exception {
        String sql = "DELETE FROM user where id=" + user.getId() + "LIMIT 1";
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    /**
     * Delete a record by crawler uid
     * @param uid crawler userid
     * @return Affect rows
     * @throws Exception SQL exception
     */
    public int deleteByWeiboId(long uid) throws Exception {
        String sql = "DELETE FROM user where uid=" + uid + "LIMIT 1";
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    /**
     * Delete a record by crawler username
     * @param username UserInfo username
     * @return Affect rows
     * @throws Exception SQL exception
     */
    public int deleteByUserName(String username) throws Exception {
        String sql = "DELETE FROM user where username=" + username + "LIMIT 1";
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    /**
     * @param statement prepared statement
     * @param user user entity
     * @throws Exception Throw SQL exception
     */
    private void setStatement(PreparedStatement statement, User user) throws Exception {
        statement.setLong(1, user.getUid());
        statement.setString(2, user.getAvatar());
        statement.setString(3, user.getUsername());
        statement.setString(4, user.getHomePage());
        statement.setInt(5, user.getGender());
        statement.setString(6, user.getAddress());
        statement.setString(7, user.getIntroduction());
        statement.setString(8, user.getCompany());
        statement.setString(9, user.getSchool());
        statement.setString(10, user.getBlog());
        statement.setInt(11, user.getLevel());
        statement.setString(12, user.getCredit());
        statement.setLong(13, user.getRegTime());
        statement.setLong(14, user.getFollowCount());
        statement.setLong(15, user.getFansCount());
        statement.setLong(16, user.getBirthday());
        statement.setInt(17, user.getTweetCount());
        statement.setString(18, user.getJob());
        statement.setString(19, user.getTags());

        if (user.getLastModify() == 0) {
            statement.setLong(20, new Date().getTime());
        } else {
            statement.setLong(20, user.getLastModify());
        }
    }
}
