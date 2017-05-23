package com.jiavan.weibo.util;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Import utils
 * Created by Jiavan on 2017/5/19.
 */
public class Import {
    private ConnectionFactory cf;
    private Connection connection;

    public Import() {
        try {
            this.cf = ConnectionFactory.getInstance();
            this.connection = cf.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void province(String filePath) throws Exception{
        Statement statement = connection.createStatement();
        String content = FileHandle.read(filePath);
        String[] provinces = content.split("-");

        for (int i = 0; i < provinces.length; i++) {
            String sql = "INSERT INTO province(name) VALUES('" + provinces[i] + "')";
            statement.executeUpdate(sql);
        }
        statement.close();
    }

    public static void main(String[] args) {
        Import in = new Import();
        try {
            in.province("file/province.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
