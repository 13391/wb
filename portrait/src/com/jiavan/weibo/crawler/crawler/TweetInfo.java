package com.jiavan.weibo.crawler.crawler;

import com.jiavan.weibo.crawler.entity.TweetImpl;
import com.jiavan.weibo.crawler.dao.Tweet;
import com.jiavan.weibo.crawler.jdbc.ConnectionFactory;
import com.jiavan.weibo.crawler.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Crawler Tweet info
 * Created by Jiavan on 2017/4/21.
 */
public class TweetInfo {
    public static ArrayList<Tweet> getTweetByUid(long uid, boolean isProxy) throws Exception {

        ArrayList<Tweet> tweets = new ArrayList<>();
        int pageNo = 1;
        while (true) {
            String url = Weibo.getTweetListUrl(uid, pageNo++);
            String content = isProxy ? HttpRequest.getByProxy(url, Proxy.getProxyPool()) : HttpRequest.get(url, true);

            try {
                JSONObject json = new JSONObject(content);

                if (json.has("cardlistInfo") && (Integer.parseInt(json.getString("ok")) == 1)) {
                    JSONObject cardlistInfo = (JSONObject) json.get("cardlistInfo");
                    if (cardlistInfo.has("total")) {
                        if (cardlistInfo.getInt("total") > Weibo.MAX_TWEET) {
                            Log.i("Over 2000 records");
                            break;
                        }
                    }
                }

                if (json.has("cards") && (Integer.parseInt(json.getString("ok")) == 1)) {
                    JSONArray cards = (JSONArray) json.get("cards");
                    if (cards.length() > 0) {
                        for (int i = 0; i < cards.length(); i++) {
                            try {
                                JSONObject card = (JSONObject) cards.get(i);
                                Tweet tweet = new Tweet();
                                tweet.setUid(uid);

                                if (card.has("scheme")) {
                                    tweet.setUrl(card.getString("scheme"));
                                    if (card.has("mblog")) {

                                        JSONObject mblog = card.getJSONObject("mblog");
                                        if (mblog.has("source")) {
                                            tweet.setSource(TextHandle.filterEmoji(mblog.getString("source")));
                                        }

                                        if (mblog.has("text")) {
                                            tweet.setText(TextHandle.filterEmoji(mblog.getString("text")));
                                        }

                                        if (mblog.has("created_at")) {

                                            try {
                                                String createdAt = mblog.getString("created_at");
                                                if (createdAt.length() < 14) {

                                                    Calendar calendar = Calendar.getInstance();
                                                    String year = calendar.get(Calendar.YEAR) + "";
                                                    if (createdAt.contains("今天")) {
                                                        String month = null;
                                                        String date = calendar.get(calendar.DATE) + "";

                                                        int m = calendar.get(calendar.MONTH) + 1;

                                                        if (m < 10) {
                                                            month = "0" + m;
                                                        }
                                                        if (Integer.parseInt(date) < 10) {
                                                            date = "0" + date;
                                                        }

                                                        createdAt = createdAt.replace("今天", year + "-" + month + "-" + date);
                                                    } else {
                                                        createdAt = year + "-" + createdAt;
                                                    }
                                                }
                                                tweet.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(createdAt).getTime());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Log.e("Parse date error");
                                            }
                                        }

                                        if (mblog.has("retweeted_status")) {
                                            JSONObject retweet = mblog.getJSONObject("retweeted_status");
                                            if (retweet.has("text")) {
                                                if (!retweet.getString("text").equals("")) {
                                                    tweet.setRetweetText(TextHandle.filterEmoji(retweet.getString("text")));
                                                }
                                            }
                                        }
                                    }
                                }
                                tweets.add(tweet);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Get tweet error");
                            }
                        }
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Parse Json error");
            }
        }

        return tweets;
    }

    public static void crawler(long uid, Connection connection, boolean isProxy) throws Exception {
        TweetImpl tweetImpl = new TweetImpl(connection);
        ArrayList<Tweet> tweets = getTweetByUid(uid, isProxy);

        if (tweets.size() > 0) {
            for (Tweet tweet : tweets) {
                tweetImpl.insert(tweet);
            }
        }
    }

    public static void main() {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = cf.getConnection();
            String sql = "SELECT uid FROM user_entrance LIMIT 0, 300000";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                long uid = resultSet.getLong("uid");
                crawler(uid, connection, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Database close error");
            }
        }
    }
}
