package com.jiavan.weibo.test;

import com.jiavan.weibo.crawler.UserInfo;
import com.jiavan.weibo.dao.User;
import com.jiavan.weibo.util.Log;

/**
 * Created by Jiavan on 2017/4/8.
 * <p>
 * UserInfo crawler test
 */
public class UserInfoTest {
    public static void main(String[] args) {
        try {
            User user = UserInfo.getUserById(new Long("3855494782"), false);
            Log.i(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
