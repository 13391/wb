package com.jiavan.weibo.test;

import com.jiavan.weibo.util.FileHandle;
import com.jiavan.weibo.util.HttpRequest;
import com.jiavan.weibo.util.Log;
import com.jiavan.weibo.util.Proxy;

import java.util.HashMap;

/**
 * Created by Jiavan on 2017/4/3.
 * <p>
 * HttpRequest test suit
 */
public class HttpRequestTest {
    private String url;
    private String response;

    private HttpRequestTest(String url) {
        this.url = url;
        this.response = "";
    }

    private void getWithNoParams() {
        try {
            this.response = HttpRequest.get(this.url);
            FileHandle.write("file/test.getWithNoParams.html", this.response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(this.response);
    }

    private void getWithQueryMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("tab", "repositories");

        this.response = "";
        try {
            response = HttpRequest.get(this.url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(response);
    }

    private void getWithQueryString() {
        this.response = "";
        try {
            this.response = HttpRequest.get(this.url, "is_hot=1", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(this.response);
    }

    private void getByProxy() {
        String content = HttpRequest.getByProxy(this.url, Proxy.getProxyPool());
        Log.i(content);
    }

    public static void main(String[] args) {
        HttpRequestTest test = new HttpRequestTest("https://m.weibo.cn/container/getIndex?containerid=2302831642632024_-_INFO");
        test.getByProxy();

        Log.i(HttpRequest.get("https://m.weibo.cn/api/container/getIndex?containerid=2304133855494782&page=3"));
    }
}
