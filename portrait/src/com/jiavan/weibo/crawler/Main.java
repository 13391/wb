package com.jiavan.weibo.crawler;

import com.jiavan.weibo.util.Log;

/**
 * crawler entrance
 * Created by Jiavan on 2017/5/10.
 */
public class Main {
    public static void main(String[] args) {

        Main crawler = new Main();
        if (args.length > 0) {

            String crawlerName = args[0];
            int crawlIndex = 0;

            if (args.length == 2) {
                crawlIndex = Integer.parseInt(args[1]);
            }
            if (crawlerName.equals("depth")) {
                crawler.crawlDepth();
            } else if (crawlerName.equals("userinfo")) {
                crawler.crawlUserInfo();
            } else if (crawlerName.equals("tweets")) {
                TweetInfo.main();
            }
        } else {
            Log.i("No params input!");
        }
    }

    private void crawlDepth() {
        int maxDepth = 6;
        try {
            UserDepth userDepth = new UserDepth(1);

            // set author weibo id as first depth user id
            UserList userList = new UserList(new Long("3855494782"), userDepth.getDepth());

            while (userDepth.getDepth() < maxDepth) {
                userDepth.crawler();
                userDepth.setDepth(userDepth.getDepth() + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crawlUserInfo() {
        try {
            UserInfo.main();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("crawlUserInfo error");
        }
    }
}
