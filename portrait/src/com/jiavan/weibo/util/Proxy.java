package com.jiavan.weibo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Proxy Server utils
 * Created by Jiavan on 2017/5/11.
 */
public class Proxy {
    private String ip;
    private int port;

    public Proxy() {}
    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Get proxy server from database
     * @return
     * @throws Exception
     */
    public static HashMap<Integer, Proxy> getProxyPool() {
        HashMap<Integer, Proxy> map = new HashMap<>();
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = cf.getConnection();

            String sql = "SELECT * FROM proxy";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int index = 0;
            while (resultSet.next()) {
                Proxy proxy = new Proxy(resultSet.getString("ip"), resultSet.getInt("port"));
                map.put(index++, proxy);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Get proxy error");
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Close database error");
            }
        }

        return map;
    }

    /**
     * Get a random proxy from proxy pool
     * @param map
     * @return
     */
    public static Proxy getRandomProxy(HashMap<Integer, Proxy> map) {
        Random r = new Random();
        int x = r.nextInt(map.size());

        return map.get(x);
    }

    public static void clearDeathProxyIp(Map<String, Integer> proxyIpMap, String reqUrl) throws Exception{

        int aliveCount = 0;
        for (String proxyHost : proxyIpMap.keySet()) {
            Integer proxyPort = proxyIpMap.get(proxyHost);

            SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
            java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, addr);
            int statusCode = 0;
            try {
                HttpURLConnection connection;
                Map<String, String> requestHeaders = new HashMap<>();
                requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                // Error set with gzip
                // requestHeaders.put("Accept-Encoding", "gzip, deflate, sdch");
                requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,da;q=0.2");
                requestHeaders.put("Cache-Control", "no-cache");
                requestHeaders.put("Connection", "keep-alive");
                requestHeaders.put("User-Agent", Weibo.getUserAgent());

                URL connectUrl = new URL(reqUrl);
                connection = (HttpURLConnection) connectUrl.openConnection(proxy);
                // set connect timeout 5s
                connection.setConnectTimeout(Weibo.PROXY_MAX_TIME_OUT);
                connection.setReadTimeout(Weibo.CONNECTION_TIME_OUT);

                Log.i("Start fetching " + reqUrl);
                for (String key : requestHeaders.keySet()) {
                    connection.setRequestProperty(key, requestHeaders.get(key));
                }

                connection.connect();
                statusCode = connection.getResponseCode();

                if (statusCode == 200) {
                    aliveCount++;
                } else {
                    removeDeathProxyIp(proxyHost);
                }
            } catch (Exception e) {
                // block
                e.printStackTrace();
                removeDeathProxyIp(proxyHost);
            }

            System.out.format("%s:%s-->%s %s\n", proxyHost, proxyPort, statusCode, aliveCount);
        }
    }

    public static int removeDeathProxyIp(String ip) throws Exception {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Connection dbConnection = cf.getConnection();
        Statement statement = null;

        String sql = "DELETE FROM proxy WHERE ip='" + ip + "' LIMIT 1";
        statement = dbConnection.createStatement();
        Log.i("Delete proxy " + ip);
        return statement.executeUpdate(sql);
    }

    public static void checkProxyIp(String proxyIp, int proxyPort, String reqUrl) {
        Map<String, Integer> proxyIpMap = new HashMap<String, Integer>();
        proxyIpMap.put(proxyIp, proxyPort);
//        checkProxyIp(proxyIpMap, reqUrl);
    }

    public static int importProxyToDB(String filepath) throws Exception {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Connection connection = cf.getConnection();

        File fileHandle = new File(filepath);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileHandle));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        PreparedStatement statement = null;
        String line;
        int count = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String[] proxy = line.split(":");
            String sql = "INSERT INTO proxy VALUES(NULL, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, proxy[0]);
            statement.setInt(2, Integer.parseInt(proxy[1]));
            statement.executeUpdate();
            count++;
        }

        statement.close();
        inputStreamReader.close();
        bufferedReader.close();
        connection.close();

        return count;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
