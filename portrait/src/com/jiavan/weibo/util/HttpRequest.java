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
 * Http method utils
 */
public class HttpRequest {
    public static int requestCount = 1;

    public static String sendGet(String url, boolean isAnonymous) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        int responseCode = 200;

        // Set http request headers
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        // Error set with gzip
        // requestHeaders.put("Accept-Encoding", "gzip, deflate, sdch");
        requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,da;q=0.2");
        requestHeaders.put("Cache-Control", "no-cache");
        requestHeaders.put("Connection", "keep-alive");
        requestHeaders.put("User-Agent", Weibo.getUserAgent());

        if (!isAnonymous) {
//            requestHeaders.put("Cookie", Weibo.getCookie());
            requestHeaders.put("Cookie", "SINAGLOBAL=85894817700.2583.1491277662743; __gads=ID=036e8beeedb31cf6:T=1491283540:S=ALNI_Mb47-qhlgi9BfxRub6bDshLUTpdKA; _ga=GA1.2.130437670.1491280765; UM_distinctid=15be7602576828-06211a0477f662-396e7807-fa000-15be76025773a; YF-Page-G0=4c69ce1a525bc6d50f53626826cd2894; _s_tentry=-; Apache=9298119610514.89.1496219353372; ULV=1496219353536:14:5:1:9298119610514.89.1496219353372:1495441672842; YF-V5-G0=9717632f62066ddd544bf04f733ad50a; YF-Ugrow-G0=ad83bc19c1269e709f753b172bddb094; login_sid_t=2567714fd4d4743636464493536826de; UOR=,,login.sina.com.cn; WBStorage=02e13baf68409715|undefined; WBtopGlobal_register_version=4641949e9f3439df; SCF=Avo9VsEzJnC7_QysMtXlMVTSxwwlmnD3Tb42OVi-_IK49L0oDyNZEE_7iLDbFXtMvk3yJOAp13IlCVH54U18xuk.; SUB=_2A250KpSzDeRhGeVG7lcV-SrLwz6IHXVXQYF7rDV8PUNbmtANLXXmkW-JZLjnkxU0YUUEWlC5n9_lIP6T7g..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWl.Ag.4MOQkUHLMwoumKUy5JpX5K2hUgL.FoeRSK-X1KBN1hz2dJLoIEBLxKML1KBLBozLxKML1heLBoMLxKnL1h.LB.-LxKML1h.LBo.t; SUHB=0cSXJ-u3mq2vxT; ALF=1496850281; SSOLoginState=1496245481; un=18983663252");
        }

        try {
            // setting and open url
            Thread.sleep(Weibo.REQUEST_TICK);
            URL connectUrl = new URL(url);

            // Skip https
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            connection = (HttpURLConnection) connectUrl.openConnection();

            Log.i("Start fetching " + url);
            for (String key : requestHeaders.keySet()) {
                connection.setRequestProperty(key, requestHeaders.get(key));
            }

            // set connect timeout 5s
            connection.setConnectTimeout(Weibo.CONNECTION_TIME_OUT);
            connection.setReadTimeout(Weibo.CONNECTION_TIME_OUT);
            connection.connect();
            responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                if (responseCode == 403 || responseCode == 414) {
                    Log.i("################################################\n"
                            + "##### Http response " + responseCode + " thread sleep " + Weibo.SLEEP_TICK + " ######\n"
                            + "################################################\n");
                    Thread.sleep(Weibo.SLEEP_TICK);
                }

                if (responseCode == 302) {
                    return "";
                }

                if (HttpRequest.requestCount > Weibo.MAX_RECONNECT) {
                    Log.i("Over max reconnect count, and give up reconnect");
                    HttpRequest.requestCount = 1;
                    return "";
                }
                result = new StringBuilder(HttpRequest.sendGet(url, isAnonymous));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (SocketTimeoutException e) {

            // Connection timeout and reconnect
            Log.i("Connection timeout reconnect");
            HttpRequest.requestCount++;

            if (HttpRequest.requestCount > Weibo.MAX_RECONNECT) {
                Log.i("Over max reconnect count, and give up reconnect");
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

    public static String getByProxy(String url, HashMap<Integer, Proxy> proxyPool) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
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
                Log.i("################################################\n"
                        + "##### Http response " + responseCode + " change proxy ip " + " ######\n"
                        + "################################################\n");

                if (responseCode == 302) {
                    System.exit(0);
                } else {
                    HttpRequest.getByProxy(url, proxyPool);
                }

            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
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
//            requestHeaders.put("Cookie", FileHandle.read("./file/cookie.txt"));

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
