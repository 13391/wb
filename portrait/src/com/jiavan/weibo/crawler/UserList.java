package com.jiavan.weibo.crawler;

import com.jiavan.weibo.dao.UserRef;
import com.jiavan.weibo.util.HttpRequest;
import com.jiavan.weibo.util.Log;
import com.jiavan.weibo.util.Weibo;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Crawler user list
 * Created by Jiavan on 2017/4/8.
 */
public class UserList {
    private long uid;
    private ArrayList<UserRef> userList;
    private int depth;

    public UserList() {
        this.userList = new ArrayList<>();
    }

    public UserList(long uid) {
        this();
        this.uid = uid;
    }

    /**
     * Input parent node userRef, the next depth add one
     * @param userRef UserRef instance
     */
    public UserList(UserRef userRef) {
        this.uid = userRef.getUid();
        this.depth = userRef.getDepth() + 1;
    }

    /**
     * Set a userlist entrance
     * @param uid weibo user id
     * @param depth this user id current depth
     */
    public UserList(long uid, int depth) {
        this();
        this.uid = uid;
        this.depth = depth + 1;
    }

    /**
     * Get user ref arraylist by user id
     * @return userref arraylist
     * @throws Exception
     */
    public ArrayList<UserRef> crawler() throws Exception {

        int page = 1;
        JSONObject json;
        String content = HttpRequest.get(Weibo.getUserListUrl(this.uid, 1));

        try {
            if (!content.equals("")) {
                json = new JSONObject(content);
                page = Integer.parseInt(json.get("maxPage").toString());
            } else {
                Log.i("User list json is null");
                return null;
            }

            // TODO: 为什么page有时候会少一页
            for (int i = 1; i <= page; ++i) {
                content = HttpRequest.get(Weibo.getUserListUrl(this.uid, i));
                if (!content.equals("")) {
                    json = new JSONObject(content);
                    JSONArray cards = json.getJSONArray("cards");

                    for (int j = 0; j < cards.length(); ++j) {
                        JSONObject card = (JSONObject) cards.get(j);
                        if (card != null) {
                            JSONObject user = (JSONObject) card.get("user");
                            String username = user.get("screen_name").toString();
                            String uid = user.get("id").toString();

                            UserRef userRef = new UserRef(Long.parseLong(uid), username, this.depth);
                            this.userList.add(userRef);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR_CRAWLER: parse json error");
        }

        return this.userList;
    }
}
