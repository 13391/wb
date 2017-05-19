package com.jiavan.weibo.crawler.test;

import com.jiavan.weibo.crawler.jdbc.ConnectionFactory;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

/**
 * Created by Jiavan on 2017/4/4.
 */
public class Test {
    public String name;
    public int age;

    public Test(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static void change(Test test) {
        test.name = "change";
    }

    public static void main(String[] args) {
//        KeyWordComputer kwc = new KeyWordComputer(5);
//        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
//        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
//        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
//
//        for (Keyword keyword : result) {
//            System.out.println(keyword.getName());
//        }

        try {
            ConnectionFactory cf = ConnectionFactory.getInstance();
            Connection connection = cf.getConnection();

            String sql = "select * from user order by id desc limit 10";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String introduction = resultSet.getString("introduction");
                KeyWordComputer kwc = new KeyWordComputer(5);
                Collection<Keyword> result = kwc.computeArticleTfidf(introduction);

                for (Keyword keyword : result) {
                    System.out.print(keyword.getName() + " ");
                }

                System.out.print("/" + introduction);
                System.out.println();
            }

//            org.ansj.domain.Result parse = NlpAnalysis.parse("洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹");
//            System.out.println(parse);
        } catch (Exception e) {

        }
    }
}
