package com.graduate.musicback.utils;

import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.History;
import com.graduate.musicback.repository.HistoryRepository;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.*;


@Component
public class Recommend {

    @Autowired
    private HttpSession session;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private HistoryRepository historyRepository;

    public String guess() throws TasteException {
        Account account = (Account) session.getAttribute("account");

        DataModel model = new MySQLJDBCDataModel(dataSource, "t_history", "account_id", "songs_id", "points", "create_at");;

        UserSimilarity similarity = new UncenteredCosineSimilarity(model);  //建立推荐模型
        UserNeighborhood neighborhood =  new NearestNUserNeighborhood(2, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender( model, neighborhood, similarity);
        List<RecommendedItem> recommendations =  recommender.recommend(Long.parseLong(account.getId()), 1); //给用户1推荐2个项目
        if(recommendations.isEmpty()){
            return "none";
        }else
        {
           return recommendations.get(0).getItemID()+""; // 转String类型
        }
    }
}