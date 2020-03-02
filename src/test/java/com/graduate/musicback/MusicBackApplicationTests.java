package com.graduate.musicback;

import com.graduate.musicback.entity.Account;
import com.graduate.musicback.repository.AccountRepository;
import com.graduate.musicback.repository.HistoryRepository;
import com.graduate.musicback.service.HistoryService;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.*;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class MusicBackApplicationTests {


    final static int NEIGHBORHOOD_NUM = 2;//临近的用户个数
    final static int RECOMMENDER_NUM = 3;//推荐物品的最大个数

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private DataSource dataSource;

    @Test
    public void test() throws IOException, TasteException {

        //获取模型
        DataModel model = new MySQLJDBCDataModel(dataSource, "t_history", "account_id", "songs_id", "points", "create_at");;

        UserSimilarity similarity = new PearsonCorrelationSimilarity (model);  //建立推荐模型
        UserNeighborhood neighborhood =  new NearestNUserNeighborhood (2, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender ( model, neighborhood, similarity);
        List<RecommendedItem> recommendations =  recommender.recommend(418909087259176960L, 1); //给用户1推荐2个项目
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation.getItemID());
        }
    }

    @Test
    public void test2() throws TasteException {
        DataModel model = new MySQLJDBCDataModel(dataSource, "t_history", "account_id", "songs_id", "points", "create_at");
        ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
        Recommender recommender1 = new GenericItemBasedRecommender(model,similarity);
        //为用户1推荐一件物品1,1
        List<RecommendedItem> recommendedItems = recommender1.recommend(426903501407531008L, 3);
        System.out.println(recommendedItems);
        //输出

    }

    @Test
    public void test3(){
        System.out.println(accountRepository.findAllReportPassed());

    }


    @Test
    public void test5(){
        Account account = new Account();
        account.setId(snowflakeIdWorker.nextId());
        account.setPassword("123456");
        account.setUsername("295332722@qq.com");
        account.setName("zhou");
        account.setType("user");
        account.setCreateAt(new Date());
        account.setCreateBy("admin");
        account.setUpdateAt(new Date());
        account.setUpdateBy("admin");
        accountRepository.saveAndFlush(account);
    }

    @Test
    public void testFloat(){
//        System.out.println(historyService.averagePoints("403221972219342848"));
    }
}
