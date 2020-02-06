package com.graduate.musicback.utils;

import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.History;
import com.graduate.musicback.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.*;


@Component
public class Recommend {

    @Autowired
    private HttpSession session;

    @Autowired
    private HistoryRepository historyRepository;

    public String guess() {

        Account account = (Account) session.getAttribute("account");
        String accountId = account.getId();
        Set<String> stringSet = new HashSet<>();
        List<History> allHistory = historyRepository.findAll();
        String lastUserId = "-1";
        String preString = "";
        for (History h : allHistory) {
            String historyId = h.getAccountId();
            String historySongsId = h.getSongsId();

            if (lastUserId.equals("-1")) {
                lastUserId = historyId;
                preString = historyId + " " + historySongsId;
            } else if (!lastUserId.equals(historyId)) {
                stringSet.add(preString);
                lastUserId = historyId;
                preString = historyId + " " + historySongsId;
            } else if (lastUserId.equals(historyId)) {
                preString += " " + historySongsId;
            }
        }

        if (!stringSet.contains(preString)) {
            stringSet.add(preString);
        }

        String recommendQuestionId = this.recommend(stringSet, accountId);

        return recommendQuestionId;
    }

    public String recommend(Set<String> stringSet, String target_user) {
        //输入用户总量为N
        //建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】
        int N = stringSet.size();
        int[][] sparseMatrix = new int[N][N];
        //存储每一个用户对应的不同物品总数  eg: A 3
        Map<String, Integer> userItemLength = new HashMap<>();
        //建立物品到用户的倒排表 eg: a A B
        Map<String, Set<String>> itemUserCollection = new HashMap<>();
        //辅助存储物品集合
        Set<String> items = new HashSet<>();
        //辅助存储每一个用户的用户ID映射
        Map<String, Integer> userID = new HashMap<>();
        //辅助存储每一个ID对应的用户映射
        Map<Integer, String> idUser = new HashMap<>();

        for (int i = 0; i < N; i++) {
            String ss = stringSet.iterator().next();
            stringSet.remove(ss);

            String[] userItem = ss.split(" ");
            int length = userItem.length;
            //eg: A 3
            userItemLength.put(userItem[0], length - 1);
            //用户ID与稀疏矩阵建立对应关系
            userID.put(userItem[0], i);
            idUser.put(i, userItem[0]);
            //建立物品--用户倒排表
            for (int j = 1; j < length; j++) {
                //如果已经包含对应的物品--用户映射，直接添加对应的用户
                if (items.contains(userItem[j])) {
                    itemUserCollection.get(userItem[j]).add(userItem[0]);
                } else {
                    //否则创建对应物品--用户集合映射
                    items.add(userItem[j]);
                    //创建物品--用户倒排关系
                    itemUserCollection.put(userItem[j], new HashSet<String>());
                    itemUserCollection.get(userItem[j]).add(userItem[0]);
                }
            }
        }

        //计算相似度矩阵【稀疏】
        Set<Map.Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();
        Iterator<Map.Entry<String, Set<String>>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Set<String> commonUsers = iterator.next().getValue();
            for (String user_u : commonUsers) {
                for (String user_v : commonUsers) {
                    if (user_u.equals(user_v)) {
                        continue;
                    }
                    //计算用户u与用户v都有正反馈的物品总数
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;
                }
            }
        }

        String recommendUser = target_user;

        int recommendUserId;
        try {
            recommendUserId = userID.get(recommendUser);
        } catch (Exception e) {
            return "target_user 不存在！";
        }

        String questionIdMax = "";
        double lastMaxRecommendValue = 0;
        for (String item : items) {
            //得到购买当前物品的所有用户集合
            Set<String> users = itemUserCollection.get(item);
            //如果被推荐用户没有购买当前物品，则进行推荐度计算
            if (!users.contains(recommendUser)) {
                double itemRecommendDegree = 0.0;
                for (String user : users) {
                    //推荐度计算
                    itemRecommendDegree += sparseMatrix[userID.get(recommendUser)][userID.get(user)]
                            / Math.sqrt(userItemLength.get(recommendUser)
                            * userItemLength.get(user));
                }
//				System.out.println("The item " + item + " for " + recommendUser
//								+ "'s recommended degree:" + itemRecommendDegree);
                /**
                 * 选出针对用户筛选出的最高推荐度的题目id
                 */
                if (itemRecommendDegree > lastMaxRecommendValue) {
                    lastMaxRecommendValue = itemRecommendDegree;
                    questionIdMax = item;
                }

            }
        }
        return questionIdMax;
    }
}