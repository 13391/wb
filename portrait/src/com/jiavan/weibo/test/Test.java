package com.jiavan.weibo.test;

import com.jiavan.weibo.crawler.UserInfo;
import com.jiavan.weibo.dao.User;
import com.jiavan.weibo.util.FileHandle;
import com.jiavan.weibo.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jiavan on 2017/4/4.
 */
public class Test {

    public static void main(String[] args) {
        try {
            Log.i(UserInfo.getUserByPage(new Long("1191220232"),false).toString());
        } catch (Exception e) {}
    }
}
