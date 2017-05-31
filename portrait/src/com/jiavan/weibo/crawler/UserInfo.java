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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jiavan on 2017/4/4.
 * UserInfo Utils
 */
public class UserInfo extends Thread {

    private int index;
    private ConnectionFactory cf;
    private boolean isProxy;

    public UserInfo(boolean isProxy) {
        this.isProxy = isProxy;
    }
    public UserInfo(int index, boolean isProxy) {
        this.index = index;
        this.isProxy = isProxy;
        this.cf = ConnectionFactory.getInstance();
    }

    public void runAPI() {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        User user;
        UserImpl userImpl;
        String sql;
        try {
            connection = cf.getConnection();
            userImpl = new UserImpl(connection);

            sql = "SELECT uid FROM user_entrance LIMIT " + (this.index * 100000) +  ", 100000";
            System.out.println(sql);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            Long uid;

            while (resultSet.next()) {
                try {
                    uid = resultSet.getLong("uid");
                    user = UserInfo.getUserById(uid, this.isProxy);
                    if (user != null) {
                        userImpl.insert(user);
                        Log.i(user.getUsername() + ", " + user.getUid());
                    }
                } catch (Exception e) {
                    Log.e(e.toString());
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

    public static void crawler() {
        ConnectionFactory cf = ConnectionFactory.getInstance();
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        UserImpl userImpl;
        try {
            connection = cf.getConnection();
            userImpl = new UserImpl(connection);

            String sql = "SELECT count(*) AS count FROM user_entrance";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int count, pageCount = 10000;
            resultSet.next();
            count = resultSet.getInt("count");

            for (int i = 0; i <= count / pageCount; i++) {
                sql = "SELECT * FROM user_entrance LIMIT " + (i * pageCount) + ", " + pageCount;
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    long uid = resultSet.getLong("uid");
                    try {
                        User user = UserInfo.getUserByPage(uid, false);
                        if (user != null) {
                            userImpl.insert(user);
                            Log.i(user.getUsername() + ", " + user.getUid());
                        }
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Insert user info error");
                    }
                }
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
            }
        }

    }

    /**
     * Download crawler search page and find user id by username
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
     * @param gender male/female
     * @return 0, 1, 2
     */
    private static int getGenderByString(String gender) {

        if (gender.equals("男")) {
            return 1;
        } else if (gender.equals("女")){
            return 2;
        }

        return 0;
    }

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

    // Duplicate
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

        System.out.println("page: " + page + " , count: " + count);

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

                    System.out.println(username + uid);
                }
            }
        }
    }

    /**
     * check table `user` whether have uid
     * @param uid
     * @param connection
     * @return
     * @throws Exception
     */
    public static boolean hasUser(long uid, Connection connection) throws Exception {

        String sql = "SELECT * FROM user WHERE uid=" + uid;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        boolean flag = resultSet.next();
        statement.close();

        return flag;
    }

    public static User getUserByPage(Long userId, boolean isProxy) {
        User user = new User();
        String url = Weibo.getUserInfoUrl(userId);
        String content = HttpRequest.get(url, isProxy);

        // Regular rules
        String regxInfo = "<span\\sclass=(.*?)pt_title(.*?)>([^<>]+)<\\\\/span>" +
                "([^<>\\s])*<span\\sclass=(.*?)pt_detail(.*?)>([^<>]+)<\\\\/span>";
        String regxTags = "<a\\s(.*?)\\s+class=(.*?)W_btn_tag(.*?)>([^<>]+)<\\\\/a>";
        String regxSchool = "([^<>\\s])*<span\\sclass=(.*?)pt_detail(.*?)>([^<>]+)<a(.*?infedu.*?)>(.*?)<\\\\/a>(.*?)<\\\\/span>([^<>\\s])*";
        String regxCompany = "([^<>\\s])*<span\\sclass=(.*?)pt_detail(.*?)>([^<>]+)<a(.*?infjob.*?)>(.*?)<\\\\/a>(.*?)<\\\\/span>([^<>\\s])*";
        String regxDomain = "\\$CONFIG\\['domain'\\]='(.*?)'";

        Matcher m = null;
        try {
            // User have a short homepage address and redirect to new url
            if (content.contains("PCD_person_info")) {
                if (content.contains("icon_verify_co_v")) {
                    Log.i("This user type is company and will be skip");
                    return null;
                } else {
                    m = Pattern.compile(regxDomain).matcher(content);
                    if (m.find()) {
                        url = Weibo.getUserInfoUrl(userId, m.group(1));
                        Log.i("Request redirect to " + url);
                        content = HttpRequest.get(url, isProxy);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Parse user info page error");
        }

        if (content.equals("") || content == null) {
            return null;
        }

        try {
            // Find weibo user basic information
            // except eduction and tags
            m = Pattern.compile(regxInfo).matcher(content);
            while(m.find()){
                String key = m.group(3);
                String value = m.group(7).replaceAll("\\\\t|\\\\r|\\\\n", "");

                if (key.equals("昵称：")) {
                    user.setUsername(value);
                } else if (key.equals("所在地：")) {
                    user.setAddress(value);
                } else if (key.equals("性别：")) {
                    if (value.equals("男")) {
                        user.setGender(1);
                    } else if (key.equals("女")) {
                        user.setGender(2);
                    } else {
                        user.setGender(0);
                    }
                } else if (key.equals("生日：")) {
                    if (value.contains("座")) {
                        user.setConstellation(value);
                    } else {
                        try {
                            user.setBirthday(new SimpleDateFormat("yyyy年MM月dd日").parse(value).getTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(e.toString());
                        }
                    }
                } else if (key.equals("简介：")) {
                    user.setIntroduction(value);
                } else if (key.equals("性取向：")) {
                    user.setSexualOrientation(value);
                } else if (key.equals("感情状况：")) {
                    user.setLoveState(value);
                } else if (key.equals("血型：")) {
                    user.setBloodType(value);
                }
            }

            // Find school in html code
            m = Pattern.compile(regxSchool).matcher(content);
            if (m.find()) {
                String school = m.group(6);
                user.setSchool(school);
            }

            // Find company in html code
            m = Pattern.compile(regxCompany).matcher(content);
            if (m.find()) {
                String company = m.group(6);
                user.setCompany(company);
            }

            // Find tags in html page
            // tags will split by commas
            m = Pattern.compile(regxTags).matcher(content);
            ArrayList<String> tagList = new ArrayList<>();
            String tags = "";
            while (m.find()) {
                try {
                    String tag = m.group(4).replaceAll("\\\\t|\\\\r|\\\\n", "");
                    tagList.add(tag);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(e.getMessage());
                }
            }
            for (int i = 0; i < tagList.size(); i++) {
                tags += tagList.get(i);
                if (i < tagList.size() - 1) {
                    tags += ",";
                }
            }
            user.setTags(tags);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Parse user info page error");
        }

        return user;
    }

    public static void main() throws Exception {
//        UserInfo[] workers = new UserInfo[6];
//
//        for (int i = 0; i < workers.length; i++) {
//            workers[i] = new UserInfo(i, false);
//            workers[i].start();
//        }
//        UserInfo.crawler();
    }
}
