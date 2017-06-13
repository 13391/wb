package com.jiavan.weibo.crawler;

import com.jiavan.weibo.entity.UserImpl;
import com.jiavan.weibo.dao.User;
import com.jiavan.weibo.util.ConnectionFactory;
import com.jiavan.weibo.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jiavan on 2017/4/4.
 * <p>
 * UserInfo Utils
 * The function of the module is get weibo user information.
 * Through table `user_entrance` get all user id as a list and
 * traversal it, and use weibo's api get json format user data.
 * <p>
 * Another way to get user info is visit user profile page, and
 * parse HTML, JavaScript code by regular expression.
 */
public class UserInfo extends Thread {

    private int index;
    private ConnectionFactory cf;
    private boolean isProxy;
    private int userCount;

    public UserInfo(boolean isProxy) {
        this.isProxy = isProxy;
        this.cf = ConnectionFactory.getInstance();
        this.userCount = 0;
    }

    public UserInfo(int index, boolean isProxy) {
        this.index = index;
        this.isProxy = isProxy;
        this.cf = ConnectionFactory.getInstance();
    }

    public void run() {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        User user;
        UserImpl userImpl;
        String sql;

        try {
            connection = this.cf.getConnection();
            userImpl = new UserImpl(connection);

            /**
             * Select user count from table `user_entance` and paging.
             */
            sql = "SELECT COUNT(*) AS count FROM user_entrance";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int count = 0, pageCount = 10000;
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }

            for (int i = 0; i <= count / pageCount; i++) {
                sql = "SELECT uid FROM user_entrance LIMIT " + (i * pageCount) + ", " + pageCount;
                resultSet = statement.executeQuery(sql);
                System.out.println(sql);
                while (resultSet.next()) {
                    try {
                        long uid = resultSet.getLong("uid");
                        user = UserInfo.getUserById(uid, this.isProxy);
                        if (user != null) {
                            userImpl.insert(user);
                            this.userCount++;
                            Log.i("#" + this.userCount + " " + user.getUsername() + ", " + user.getUid());
                        }
                    } catch (Exception e) {
                        Log.e(e.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Visit weibo page and search user by user name, parse page
     * and get target user's userid.
     *
     * @param username username
     * @return user id
     * @throws Exception URLEncoder unsupport exception
     */
    public static String getUserIdByUserName(String username) throws Exception {

        String url = Weibo.URL_SEARCH.replace("{USERNAME}", URLEncoder.encode(username, "UTF-8"));
        String pattern = "(uid=\\\\\")([0-9]+)\\\\\"";
        Pattern reg = Pattern.compile(pattern);
        String content = HttpRequest.get(url);
        Matcher matcher = reg.matcher(content);
        matcher.find();

        return matcher.group(matcher.groupCount());
    }

    /**
     * Get user gender number from text.
     *
     * @param gender male/female
     * @return 0, 1, 2
     */
    private static int getGenderByString(String gender) {

        if (gender.equals("男")) {
            return 1;
        } else if (gender.equals("女")) {
            return 2;
        }

        return 0;
    }

    /**
     * Get user information by weibo user id.
     *
     * @param userId  weibo user id
     * @param isProxy use proxy server or not
     * @return User instance
     * @throws Exception Parse m.weibo.cn API and filter info.
     */
    public static User getUserById(Long userId, boolean isProxy) throws Exception {

        User user = new User();
        user.setUid(userId);

        // Get user info api response
        String url = Weibo.URL_USER_INFO.replace("{CONTAINER_ID}", Weibo.CONTAINER_ID + userId);
        String response = isProxy ? HttpRequest.getByProxy(url, Proxy.getProxyPool()) : HttpRequest.get(url, true);

        if (!response.equals("")) {
            JSONObject json = new JSONObject(response);
            JSONArray cards = (JSONArray) json.get("cards");

            for (int i = 0; i < cards.length(); i++) {
                JSONObject card = (JSONObject) cards.get(i);
                if (card.has("card_group")) {
                    JSONArray cardGroup = (JSONArray) card.get("card_group");

                    if (cardGroup.length() > 0) {
                        for (int j = 0; j < cardGroup.length(); j++) {
                            JSONObject item = (JSONObject) cardGroup.get(j);

                            if (item.has("item_name") && item.has("item_content")) {
                                String itemName = item.get("item_name").toString();
                                String itemContent = item.get("item_content").toString();

                                if (itemName.equals("昵称")) {
                                    user.setUsername(itemContent);
                                }

                                if (itemName.equals("性别")) {
                                    user.setGender(getGenderByString(itemContent));
                                }

                                if (itemName.equals("所在地")) {
                                    user.setAddress(TextHandle.filterEmoji(itemContent));
                                }

                                if (itemName.equals("简介")) {
                                    user.setIntroduction(TextHandle.filterEmoji(itemContent));
                                }

                                if (itemName.equals("标签")) {
                                    user.setTags(itemContent);
                                }

                                if (itemName.equals("公司")) {
                                    user.setCompany((TextHandle.filterEmoji(itemContent)));
                                }

                                if (itemName.equals("博客")) {
                                    user.setBlog(TextHandle.filterEmoji(itemContent));
                                }

                                if (itemName.equals("阳光信用")) {
                                    user.setCredit(itemContent);
                                }

                                if (itemName.equals("注册时间")) {
                                    user.setRegTime(new SimpleDateFormat("yyyy-MM-dd").parse(itemContent).getTime());
                                }

                                if (itemName.equals("学校")) {
                                    user.setSchool(itemContent);
                                }
                            }
                        }
                    }
                }
            }

            // Get profile api response
//            url = com.jiavan.crawler.weibo.util.Weibo.URL_USER_PROFILE.replace("{USER_ID}", userId.toString());
//            response = isProxy ? HttpRequest.getByProxy(url, Proxy.getProxyPool()): HttpRequest.get(url, true);
//
//            if (!response.equals("")) {
//                json = new JSONObject(response);
//                JSONObject userInfo = (JSONObject) json.get("userInfo");
//                user.setFollowCount(Integer.parseInt(userInfo.get("follow_count").toString()));
//                user.setFansCount(Integer.parseInt(userInfo.get("followers_count").toString()));
//                user.setLevel(Integer.parseInt(userInfo.get("urank").toString()));
//                user.setTweetCount(Integer.parseInt(userInfo.get("statuses_count").toString()));
//                user.setHomePage(userInfo.get("profile_url").toString());
//                user.setAvatar(userInfo.get("profile_image_url").toString());
//            } else {
//                return null;
//            }

            return user;
        } else {
            return null;
        }
    }

    /**
     * Duplicate
     * Get user information by weibo user id
     *
     * @param userId weibo user id
     * @return
     */
    public static User getUserInfoById(String userId) {

        String url = Weibo.URL_USER_CENTER + userId;
        String content = HttpRequest.get(url);
        String pattern = "CONFIG\\['page_id'\\]='([0-9]+)'";
        Pattern reg = Pattern.compile(pattern);
        Matcher matcher = reg.matcher(content);

        matcher.find();
        String pageId = matcher.group(matcher.groupCount());
        url = Weibo.URL_USER_INFO.replace("{PAGE_ID}", pageId);

        return null;
    }

    public static void getUserList(String userId) throws Exception {

        String content = HttpRequest.get(Weibo.getUserListUrl(new Long("3855494782"), 1));
        JSONObject json;
        int page = 0;
        int count = 0;

        if (!content.equals("")) {

            json = new JSONObject(content);
            page = Integer.parseInt(json.get("maxPage").toString());
            count = Integer.parseInt(json.get("count").toString());
        } else {
            System.out.println("User list json is null");
        }

        Log.i("page: " + page + " , count: " + count);
        for (int i = 1; i < page; ++i) {
            content = HttpRequest.get(Weibo.getUserListUrl(new Long("3855494782"), i));
            if (!content.equals("")) {
                json = new JSONObject(content);
                JSONArray cards = json.getJSONArray("cards");

                for (int j = 0; j < cards.length(); ++j) {
                    JSONObject card = (JSONObject) cards.get(j);
                    JSONObject user = (JSONObject) card.get("user");
                    String username = user.get("screen_name").toString();
                    String uid = user.get("id").toString();

                    Log.i(username + uid);
                }
            }
        }
    }

    /**
     * Check table `user` whether have uid
     *
     * @param uid        weibo user id
     * @param connection mysql connection
     * @return
     * @throws Exception sql exception
     */
    public static boolean hasUser(long uid, Connection connection) throws Exception {

        String sql = "SELECT * FROM user WHERE uid=" + uid;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        boolean flag = resultSet.next();
        statement.close();

        return flag;
    }

    public static void main(boolean isProxy) throws Exception {
        if (isProxy) {
            UserInfo[] workers = new UserInfo[30];

            for (int i = 0; i < 30; i++) {
                workers[i] = new UserInfo(i, true);
                workers[i].start();
            }
        } else {
            UserInfo userInfo = new UserInfo(false);
            userInfo.run();
        }
    }
}
