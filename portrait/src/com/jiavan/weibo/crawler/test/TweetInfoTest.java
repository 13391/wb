package com.jiavan.weibo.crawler.test;

import com.jiavan.weibo.crawler.crawler.TweetInfo;
import com.jiavan.weibo.crawler.jdbc.ConnectionFactory;

import java.sql.Connection;

/**
 * TweetInfo test
 * Created by Jiavan on 2017/5/17.
 */
public class TweetInfoTest {
    public static void main(String[] args) {
        try {
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();
            TweetInfo.crawler(new Long("5380841592"), connection, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
