package com.jiavan.weibo.test;

import com.jiavan.weibo.util.ConnectionFactory;
import com.jiavan.weibo.util.HttpRequest;
import com.jiavan.weibo.util.Log;
import com.jiavan.weibo.util.Proxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Jiavan on 2017/5/11.
 * <p>
 * Proxy utils test
 */
public class ProxyTest {

    public static void main(String[] args) {
        ProxyTest proxyTest = new ProxyTest();
        proxyTest.getRandomProxy();
        proxyTest.importProxyFromFile();
        proxyTest.clearProxy();
        Log.i(HttpRequest.getByProxy("http://httpbin.org/", Proxy.getProxyPool()));
    }

    private void getRandomProxy() {
        Proxy proxy = Proxy.getRandomProxy(Proxy.getProxyPool());
        Log.i(proxy.toString());
    }

    private void importProxyFromFile() {
        try {
            Proxy.importProxyToDB("./file/proxy_http.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearProxy() {
        try {
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();

            String sql = "SELECT * FROM proxy";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            HashMap<String, Integer> map = new HashMap<>();

            while (resultSet.next()) {
                map.put(resultSet.getString("ip"), resultSet.getInt("port"));
            }

            Proxy.clearDeathProxyIp(map, "http://httpbin.org/ip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
