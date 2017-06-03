package com.jiavan.weibo.portrait.user;

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
                    String address = resultSet.getString("address");
                    String school = resultSet.getString("school");

                    sql = "INSERT INTO user_deal(uid, province, education) VALUES(?, ?, ?)";
                    PreparedStatement state = connection.prepareStatement(sql);
                    state.setLong(1, uid);
                    state.setString(2, getProvince(address));
                    state.setString(3, getEducation(school));
                    state.executeUpdate();

                    state.close();
                }
            }

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
