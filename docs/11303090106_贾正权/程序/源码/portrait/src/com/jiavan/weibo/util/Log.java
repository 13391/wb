package com.jiavan.weibo.util;

import java.util.Date;

/**
 * Created by Jiavan on 2017/5/10.
 * <p>
 * Log utils
 * Include three different levels -> `i`, `w`, `e`.
 * `i` is information logs like some object to string or request url.
 * `e` is error, include some exception logs.
 * `w` it's warning log, will use a special format to print.
 * <p>
 * All of logs will attach log's time.
 */
public class Log {

    public static void i(String info) {
        System.out.println("[INFO] " + new Date().toString() + "\n" + info);
    }

    public static void e(String error) {
        System.out.println("[ERROR] " + new Date().toString() + "\n" + error);
    }

    public static void w(String warning) {
        System.out.println("[WARNING] " + new Date().toString() + "\n" + "###################### " + warning + " ######################\n");
    }
}
