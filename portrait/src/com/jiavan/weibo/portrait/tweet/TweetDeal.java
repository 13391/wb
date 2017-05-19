package com.jiavan.weibo.portrait.tweet;

import com.jiavan.weibo.util.ConnectionFactory;
import com.jiavan.weibo.util.Log;
import com.jiavan.weibo.util.Weibo;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;

/**
 * User Tweets deal
 * Created by Jiavan on 2017/5/19.
 */
public class TweetDeal {

    public static void main(String[] args) {
        ConnectionFactory cf = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Date startTime, endTime;

        Log.w("Start deal tweets");
        startTime = new Date();
        try {
            cf = ConnectionFactory.getInstance();
            connection = cf.getConnection();
            statement = connection.createStatement();
            resultSet = null;
            int recordCount = 0;

            String sql = "SELECT COUNT(*) AS count FROM tweets";
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            recordCount = resultSet.getInt("count");

            for (int i = 0; i <= recordCount / Weibo.MAX_PAGE_RECORD; i++) {
                sql = "SELECT id, uid, text, retweet_text FROM tweets LIMIT " + (i * Weibo.MAX_PAGE_RECORD) + ", 10000";
                resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    long tid = resultSet.getLong("id");
                    long uid = resultSet.getLong("uid");
                    String text = getKeyWords(resultSet.getString("text"), 5);
                    String retext = getKeyWords(resultSet.getString("retweet_text"), 5);

                    sql = "INSERT INTO tweets_deal(tid, uid, text, retext) VALUES(?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setLong(1, tid);
                    preparedStatement.setLong(2, uid);
                    preparedStatement.setString(3, text);
                    preparedStatement.setString(4, retext);
                    preparedStatement.executeUpdate();
                    Log.i("Deal " + tid);

                    preparedStatement.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Close database error");
            }
            endTime = new Date();
            Log.w("Deal tweets complete spend time: " + ((endTime.getTime() - startTime.getTime()) / 1000) + "s");
        }
    }


    /**
     * Get keywords from a string
     * @param text
     * @param keywordNum
     * @return
     */
    public static String getKeyWords(String text, int keywordNum) {

        if (text == null || text.equals("")) {
            return "";
        }

        KeyWordComputer kwc = new KeyWordComputer(keywordNum);
        Collection<Keyword> keywords = kwc.computeArticleTfidf(text);
        String result = "";

        int i = 0;
        for (Keyword keyword : keywords) {
            result += keyword.getName();
            if (i < keywords.size() - 1) {
                result += ",";
            }
            i++;
        }

        return result;
    }
}
