package com.jiavan.weibo.util;

import java.util.Date;

/**
 * Created by Jiavan on 2017/5/10.
 * Log utils
 */
public class Log {

    /**
     * log information
     * @param info
     */
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
