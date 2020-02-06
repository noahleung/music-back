package com.graduate.musicback;


import com.graduate.musicback.entity.History;
import com.graduate.musicback.entity.Songs;
import com.graduate.musicback.repository.*;
import com.graduate.musicback.service.*;
import com.graduate.musicback.utils.Recommend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class MusicBackApplicationTests {


    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryService historyService;


    @Test
     public void test() throws ParseException, MessagingException {

        String accountId = "418909087259176960";

        Set<String> stringSet = new HashSet<>();
        List<History> allHistory = historyRepository.findAll();
        String lastUserId = "-1";
        String preString = "";
        for(History h:allHistory){
            String historyId = h.getAccountId();
            String historySongsId = h.getSongsId();

            if(lastUserId.equals("-1")) {
                lastUserId = historyId;
                preString = historyId + " " + historySongsId;
            } else if (!lastUserId.equals(historyId)) {
                stringSet.add(preString);
                lastUserId = historyId;
                preString = historyId + " " + historySongsId;
            }else if (lastUserId.equals(historyId)) {
                preString += " " + historySongsId;
            }
        }

        if (!stringSet.contains(preString)) {
            stringSet.add(preString);
        }

        Recommend collaborativeFilter = new Recommend();

        String recommendQuestionId = collaborativeFilter.recommend(stringSet, accountId);


    }

}
