package com.jiavan.weibo.portrait.user;

import com.jiavan.weibo.util.ConnectionFactory;
import com.jiavan.weibo.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by Jiavan on 2017/5/22.
 */
public class UserSource {
    public static void main(String[] args) {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Connection connection = null;
        Statement statement = null;
        Statement sourceState = null;
        ResultSet resultSet = null;
        ResultSet sourceSet = null;
        Date startTime, endTime;

        startTime = new Date();
        Log.w("Start deal user source");
        try {
            connection = cf.getConnection();
            String sql = "SELECT DISTINCT(uid) AS uid FROM tweets";
            statement = connection.createStatement();
            sourceState = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                long uid = resultSet.getLong("uid");
                sql = "select source, count(source) as count from tweets where uid='" + uid + "' group by source order by count desc limit 1";
                sourceSet = sourceState.executeQuery(sql);
                sourceSet.next();
                String source = sourceSet.getString("source");
                sql = "update user_deal set source='" + source + "' where uid='" + uid +"' limit 1";
                sourceState.executeUpdate(sql);

                Log.i(uid + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Update source error");
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
                sourceSet.close();
                sourceState.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Close db error");
            }
            endTime = new Date();
            Log.w("Deal user source complete spend: " + (endTime.getTime() - startTime.getTime()) / 1000 + "s");
        }
    }
}
