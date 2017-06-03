package com.jiavan.weibo.test;

import com.jiavan.weibo.crawler.TweetInfo;
import com.jiavan.weibo.dao.Tweet;
import com.jiavan.weibo.util.ConnectionFactory;
import com.jiavan.weibo.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Jiavan on 2017/5/17.
 * <p>
 * TweetInfo test
 */
public class TweetInfoTest {
    public static void main(String[] args) {
        try {
            long uid = new Long("5613198473");
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();
            TweetInfo.crawler(uid, connection, false);

            String sql = "SELECT * FROM tweets WHERE uid=" + uid;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Tweet tweet = new Tweet();
                tweet.setCreatedAt(resultSet.getLong("created_at"));
                tweet.setId(resultSet.getLong("id"));
                tweet.setUid(resultSet.getLong("uid"));
                tweet.setSource(resultSet.getString("source"));
                tweet.setText(resultSet.getString("text"));
                tweet.setRetweetText(resultSet.getString("retweet_text"));
                tweet.setUrl(resultSet.getString("url"));

                Log.i(tweet.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
