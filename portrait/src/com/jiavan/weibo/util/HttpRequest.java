package com.jiavan.weibo.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;

/**
 * Created by Jiavan on 2017/4/3.
 * <p>
 * Http method utils
 * All `GET` type method support proxy type.
 * If request used proxy type, http request will use proxy server but own ip&port
 * and if request error, likes connection timeout or redirect, request will resend
 * again util over 10 times.
 * `isAnonymous` is meaning request attach cookie or not. Some get user information
 * request must attach user cookie.
 * All of HTTPS request will be trusted, and request User-Agent will be a random value.
 * <p>
 * If http response code is 4xx, thread will be sleep 5s and resend it again.
 */
public class HttpRequest {
    public static int requestCount = 1;

    public static String sendGet(String url, boolean isAnonymous) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        HttpURLConnection connection;
        int responseCode = 200;

        // Set http request headers
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        // Error set with gzip
        // requestHeaders.put("Accept-Encoding", "gzip, deflate, sdch");
        requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,da;q=0.2");
        requestHeaders.put("Cache-Control", "no-cache");
        requestHeaders.put("Connection", "keep-alive");

        // Get a random UA
        requestHeaders.put("User-Agent", Weibo.getUserAgent());

        if (!isAnonymous) {
            requestHeaders.put("Cookie", "_T_WM=dfd693abf33241c8df8773fd85d2eabc; H5_INDEX=3; H5_INDEX_TITLE=%E7%94%B2%E7%83%B7%E8%89%BE%E7%89%B9; ALF=1497029046; SCF=Avo9VsEzJnC7_QysMtXlMVTSxwwlmnD3Tb42OVi-_IK4W8YmwyUFnooGREfz_rJvbLCvrrcg8Cqr2KKIz1o0bd0.; SUB=_2A250FxT_DeRhGeVG7lcV-SrLwz6IHXVX-7y3rDV6PUJbktBeLUfdkW1O4E3-yF1fd1o079TB_MozuP-QuA..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWl.Ag.4MOQkUHLMwoumKUy5JpX5o2p5NHD95Q01h-fSh.XS0nEWs4Dqcj.i--NiK.Xi-zEi--NiKn0i-zNi--RiKn4i-i8i--NiKn4i-z4; SUHB=0G0egZZ3X2Q8MS; SSOLoginState=1494443183; M_WEIBOCN_PARAMS=featurecode%3D20000320%26oid%3D4104318392922949%26luicode%3D10000012%26lfid%3D1005053624304713_-_FOLLOWERS%26fid%3D1005051807790503_-_FOLLOWERS%26uicode%3D10000012");
        }

        try {

            // Setting and open a url
            Thread.sleep(Weibo.REQUEST_TICK);
            URL connectUrl = new URL(url);

            // Trusted HTTPS request
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            connection = (HttpURLConnection) connectUrl.openConnection();

            Log.i("Start fetching " + url);
            for (String key : requestHeaders.keySet()) {
                connection.setRequestProperty(key, requestHeaders.get(key));
            }

            // Set connect timeout 5s
            connection.setConnectTimeout(Weibo.CONNECTION_TIME_OUT);
            connection.setReadTimeout(Weibo.CONNECTION_TIME_OUT);
            connection.connect();
            responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                Log.w("Http response code " + responseCode);

                if (responseCode == 403 || responseCode == 414) {
                    Log.w("thread sleep " + Weibo.SLEEP_TICK + "s");
                    Thread.sleep(Weibo.SLEEP_TICK);
                }

                if (responseCode == 302) {
                    System.exit(0);
                }

                if (HttpRequest.requestCount > Weibo.MAX_RECONNECT) {
                    Log.e("Error more than 10 times and give up reconnect");

                    // Reset request count
                    HttpRequest.requestCount = 1;

                    return "";
                }
                result = new StringBuilder(HttpRequest.sendGet(url, isAnonymous));
            } else {

                // If all right read input stream and return result
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (SocketTimeoutException e) {

            // Connection timeout and reconnect
            Log.i("Connection timeout reconnect");
            HttpRequest.requestCount++;

            if (HttpRequest.requestCount > Weibo.MAX_RECONNECT) {
                Log.e("Error more than 10 times and give up reconnect");
                HttpRequest.requestCount = 1;

                return "";
            }
            return HttpRequest.sendGet(url, isAnonymous);
        } catch (Exception e) {
            Log.e("Send get request error!" + e);
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                Log.e("Close bufferedReader error!");
                e.printStackTrace();
            }
        }
        HttpRequest.requestCount = 1;

        return result.toString();
    }

    /**
     * Send a `GET` request by proxy server
     *
     * @param url       request target
     * @param proxyPool proxy server pool
     * @return http response
     */
    public static String getByProxy(String url, HashMap<Integer, Proxy> proxyPool) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        HttpURLConnection connection;
        int responseCode = 200;

        // Set http request headers
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,da;q=0.2");
        requestHeaders.put("Cache-Control", "no-cache");
        requestHeaders.put("Connection", "keep-alive");
        requestHeaders.put("User-Agent", Weibo.getUserAgent());

        try {
            // setting proxy
            Proxy proxyServer = Proxy.getRandomProxy(proxyPool);
            SocketAddress address = new InetSocketAddress(proxyServer.getIp(), proxyServer.getPort());
            java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, address);

            // setting and open url
            URL connectUrl = new URL(url);
            connection = (HttpURLConnection) connectUrl.openConnection(proxy);

            Log.i("Start fetching " + proxyServer.toString() + " -> " + url);
            for (String key : requestHeaders.keySet()) {
                connection.setRequestProperty(key, requestHeaders.get(key));
            }

            connection.setConnectTimeout(Weibo.PROXY_MAX_TIME_OUT);
            connection.setReadTimeout(Weibo.CONNECTION_TIME_OUT);
            connection.connect();
            responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                Log.i("Http response code " + responseCode + " change proxy ip");

                if (responseCode == 302) {
                    System.exit(0);
                } else {
                    HttpRequest.getByProxy(url, proxyPool);
                }

            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            Log.e("Send get request error!" + e);
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                Log.e("Close bufferedReader error!");
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    /**
     * Send http post method
     *
     * @param url  url string
     * @param body data
     * @return http response
     */
    public static String sendPost(String url, String body) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();

        try {
            URL connectUrl = new URL(url);
            URLConnection connection = connectUrl.openConnection();

            // Set http request headers
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            // Error set with gzip
            // requestHeaders.put("Accept-Encoding", "gzip, deflate, sdch");
            requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,da;q=0.2");
            requestHeaders.put("Cache-Control", "no-cache");
            requestHeaders.put("Connection", "keep-alive");

            connection.setDoOutput(true);
            connection.setDoInput(true);

            out = new PrintWriter(connection.getOutputStream());
            out.print(body);
            out.flush();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            Log.e("Send post error " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                Log.e("Close stream error " + e.getMessage());
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    /**
     * @param url   url string
     * @param query query string
     * @return http response
     * @throws Exception IO exception
     */
    public static String get(String url, String query, boolean isAnonymous) throws Exception {
        url = url + "?" + query;
        return sendGet(url, isAnonymous);
    }

    public static String get(String url) {
        return sendGet(url, false);
    }

    public static String get(String url, boolean isAnonymous) {
        return sendGet(url, isAnonymous);
    }

    /**
     * @param url      url string
     * @param queryMap query string as key-value
     * @return http response
     * @throws Exception IO exception
     */
    public static String get(String url, Map<String, String> queryMap) throws Exception {

        String query = "?";
        for (String key : queryMap.keySet()) {
            query += key + "=" + queryMap.get(key) + "&";
        }

        return get(url, query, false);
    }

    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            Log.i("Warning: URL Host: " + urlHostName + " vs. "
                    + session.getPeerHost());
            return true;
        }
    };

    /**
     * Trust all https type request
     *
     * @throws Exception Network IOException
     */
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
}
