package com.jiavan.weibo.entity;

import com.jiavan.weibo.dao.Tweet;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Jiavan on 2017/4/23.
 * <p>
 * Tweet data info to database
 */
public class TweetImpl {
    private Connection connection;

    public TweetImpl(Connection connection) {
        this.connection = connection;
    }

    public int insert(Tweet tweet) throws Exception {
        String sql = "INSERT INTO tweets(uid, url, created_at, source, text, retweet_text) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setLong(1, tweet.getUid());
        statement.setString(2, tweet.getUrl());
        statement.setLong(3, tweet.getCreatedAt());
        statement.setString(4, tweet.getSource());
        statement.setString(5, tweet.getText());
        statement.setString(6, tweet.getRetweetText());

        int rows = statement.executeUpdate();
        statement.close();

        return rows;
    }
}
