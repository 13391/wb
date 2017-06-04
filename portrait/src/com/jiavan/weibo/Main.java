package com.jiavan.weibo;

import com.jiavan.weibo.crawler.TweetInfo;
import com.jiavan.weibo.crawler.UserDepth;
import com.jiavan.weibo.crawler.UserInfo;
import com.jiavan.weibo.portrait.tweet.TweetDeal;
import com.jiavan.weibo.portrait.user.UserDeal;
import com.jiavan.weibo.util.Log;

/**
 * Created by Jiavan on 2017/5/10.
 * <p>
 * System entrance
 * Usage: nohup java -jar portrait.jar xxx > wb.log 2>&1 &
 * Start system and write log information into `wb.log`.
 * <p>
 * If input depth will start a user depth crawler, and save result to
 * table `user_ref`. If input is userinfo will start a userinfo crawler
 * and save data to `user` table. Arguments is tweets will run a tweets
 * content crawler. If arguments is deal_xxx, it will start data deal
 * system.
 */
public class Main {
    public static void main(String[] args) {

        Main weibo = new Main();
        if (args.length > 0) {
            Log.i("");
            System.out.println("###########################################\n" +
                    "#                                         #\n" +
                    "#\tWEIBO INFORMATION CRAWL&DEAL SYSTEM   #\n" +
                    "#\t (Tribute to our first c program)     #\n" +
                    "#                                         #\n" +
                    "###########################################\n");

            String input = args[0];
            Log.i("Will running " + input);
            if (input.equals("depth")) {
                weibo.crawlDepth();
            } else if (input.equals("userinfo")) {
                weibo.crawlUserInfo();
            } else if (input.equals("tweets")) {
                TweetInfo.main();
            } else if (input.equals("deal_users")) {
                UserDeal.main();
            } else if (input.equals("dea")) {
                TweetDeal.main();
            } else if (input.equals("deal")) {
                UserDeal.main();
                TweetDeal.main();
            }
        } else {
            Log.i("No params input!");
        }
    }

    private void crawlDepth() {
        int maxDepth = 6;
        try {
            UserDepth userDepth = new UserDepth(1);

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
            UserInfo.main(false);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("crawlUserInfo error");
        }
    }
}
