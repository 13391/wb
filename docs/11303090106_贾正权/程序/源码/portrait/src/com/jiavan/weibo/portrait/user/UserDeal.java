package com.jiavan.weibo.portrait.user;

import com.jiavan.weibo.portrait.tweet.TweetDeal;
import com.jiavan.weibo.util.ConnectionFactory;
import com.jiavan.weibo.util.Log;
import com.jiavan.weibo.util.Weibo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by Jiavan on 2017/5/19.
 * <p>
 * Deal user info and save data to database
 */
public class UserDeal {
    public static void main() {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Date startTime, endTime;

        Log.w("Start deal user info");
        startTime = new Date();

        try {
            connection = cf.getConnection();
            statement = connection.createStatement();

            String sql = "SELECT COUNT(id) as count FROM user";
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            int count = resultSet.getInt("count");

            for (int i = 0; i <= count / Weibo.MAX_PAGE_RECORD; i++) {

                sql = "SELECT uid, address, school FROM user LIMIT " + (i * Weibo.MAX_PAGE_RECORD) + "," + Weibo.MAX_PAGE_RECORD;
                resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    long uid = resultSet.getLong("uid");
                    Log.i("Start deal uid -> " + uid);
                    String address = resultSet.getString("address");
                    String school = resultSet.getString("school");

                    sql = "INSERT INTO user_deal(uid, province, education) VALUES(?, ?, ?)";
                    PreparedStatement state = connection.prepareStatement(sql);
                    state.setLong(1, uid);
                    state.setString(2, getProvince(address));
                    state.setString(3, getEducation(school));
                    state.executeUpdate();

                    /**
                     * Get user all tweets from table `tweets_deal`
                     * update user_deal and set keywords value is user
                     * all tweets keywords
                     */
                    try {
                        sql = "SELECT text, retext FROM tweets_deal WHERE uid=" + uid;
                        Statement tweetsState = connection.createStatement();
                        ResultSet tweetsRet = tweetsState.executeQuery(sql);
                        String allContent = "", keywords = "";

                        if (tweetsRet.next()) {
                            tweetsRet.previous();
                            while (tweetsRet.next()) {
                                allContent += tweetsRet.getString("text");
                            }
                            keywords = TweetDeal.getKeyWords(allContent, 10);
                            sql = "UPDATE user_deal SET keywords='" + keywords + "' WHERE uid=" + uid;
                            tweetsState.executeUpdate(sql);
                        }

                        sql = "SELECT source, count(source) as count FROM tweets WHERE uid=" + uid + " GROUP BY source  ORDER BY count DESC LIMIT 1";
                        tweetsRet = tweetsState.executeQuery(sql);
                        if (tweetsRet.next()) {
                            String source = tweetsRet.getString("source");
                            sql = "UPDATE user_deal SET source='" + source + "' WHERE uid=" + uid;
                            tweetsState.executeUpdate(sql);
                        }

                        tweetsRet.close();
                        tweetsState.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("set user portrait error uid->" + uid);
                    }

                    state.close();
                }
            }

            UserDeal.dealCompany(connection, "小米", "小米");
            UserDeal.dealCompany(connection, "微软", "微软");
            UserDeal.dealCompany(connection, "淘宝", "阿里巴巴");
            UserDeal.dealCompany(connection, "新浪", "新浪");
            UserDeal.dealCompany(connection, "微博", "新浪");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Database error");
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Database close error");
            }

            endTime = new Date();
            Log.i("Spend time: " + ((endTime.getTime() - startTime.getTime()) / 1000) + "s");
            Log.w("Complete Deal");
        }
    }

    public static void dealCompany(Connection connection, String key, String value) throws Exception {
        Log.i("Start deal company " + value);
        String sql = "UPDATE user SET company='" + value + "' WHERE company like '%" + key + "%'";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    /**
     * Get user province from address
     *
     * @param address
     * @return
     */
    private static String getProvince(String address) {
        if (address == null || address.equals("")) {
            return "未知地区";
        } else {
            return address.split(" ")[0];
        }
    }

    /**
     * Get education level from school
     *
     * @param school
     * @return
     */
    private static String getEducation(String school) {

        if (school == null || school.equals("")) {
            return "未知学历";
        }

        if (school.contains("大学") || school.contains("学院")) {
            return "大学";
        } else if (school.contains("中学")) {
            return "中学";
        } else if (school.contains("小学")) {
            return "小学";
        } else {
            return "未知学历";
        }
    }
}
