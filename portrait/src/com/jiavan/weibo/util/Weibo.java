package com.jiavan.weibo.util;

import java.util.Properties;
import java.util.Random;

/**
 * Weibo utils
 * Created by Jiavan on 2017/4/4.
 */
public class Weibo {
    public static final String URL_SEARCH = "http://s.weibo.com/user/{USERNAME}&Refer=weibo_user";
    public static final String CONTAINER_ID = "230283";
    public static final String URL_USER_INFO = "https://m.weibo.cn/container/getIndex?containerid={CONTAINER_ID}_-_INFO";
    public static final String URL_USER_CENTER = "http://weibo.com/u/";
    public static final String URL_USER_PROFILE = "https://m.weibo.cn/container/getIndex?type=uid&value={USER_ID}";
    // public static final String URL_USER_INFO = "http://weibo.com/p/{PAGE_ID}/info?mod=pedit_more";
    public static final String URL_USER_LIST = "http://m.weibo.cn/api/container/getSecond?containerid=100505{USER_ID}_-_FOLLOWERS&page=2";


    public static final int REQUEST_TICK = 0;
    public static final int SLEEP_TICK = 1000 * 5;
    public static final int CONNECTION_TIME_OUT = 1000 * 8;
    public static final int PROXY_MAX_TIME_OUT = 1000 * 3;
    public static final int MAX_RECONNECT = 5;

    // If reconnect over max count, give up reconnect
    public static final int MAX_TWEET = 1000;
    public static final int MAX_PAGE_RECORD = 10000;

    // Get cookie by properties file
    public static String getCookie() {
        Properties prop = null;
        try {
            prop = FileHandle.loadProp("config.properties");
            return prop.getProperty("cookie");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    // Get a random user agent
    public static String getUserAgent() {

        String[] uas = {
                "User-Agent,Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "User-Agent,Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "User-Agent,Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)",
                "User-Agent, Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv,2.0.1) Gecko/20100101 Firefox/4.0.1",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)",
                "User-Agent, MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
                "User-Agent, Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/999"
        };
        Random random = new Random();

        return uas[random.nextInt(uas.length)];
    }

    public static String getUserListUrl(long userId, int pageNum) {
        return "http://m.weibo.cn/api/container/getSecond?containerid=100505" + userId + "_-_FOLLOWERS&page=" + pageNum;
    }

    /**
     * Get tweet url by weibo user id and page number
     * @param uid weibo user id
     * @param pageNum page number
     * @return request url
     */
    public static String getTweetListUrl(long uid, int pageNum) {
        return "https://m.weibo.cn/api/container/getIndex?containerid=107603" + uid + "&page=" + pageNum;
    }
}
