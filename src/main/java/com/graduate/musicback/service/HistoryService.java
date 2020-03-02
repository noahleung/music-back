package com.graduate.musicback.service;

import com.graduate.musicback.dto.history.HistorySongsDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.History;
import com.graduate.musicback.repository.HistoryRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private HttpSession session;

    public void add(String id) {
        Account account = (Account) session.getAttribute("account");
        History history = historyRepository.findHistoryByAccountIdAndSongsId(Long.parseLong(account.getId()), Long.parseLong(id));
        if (history == null) {
            history = new History();
            history.setSongsId(Long.parseLong(id));
            history.setAccountId(Long.parseLong(account.getId()));
            history.setId(snowflakeIdWorker.nextId());
            history.setCreateAt(new Date());
            history.setCreateBy(account.getId());
            history.setUpdateAt(new Date());
            history.setUpdateBy(account.getId());
            history.setTimes(1);
            history.setIsDel(false);
            historyRepository.save(history);
        } else {
            history.setTimes(history.getTimes() + 1);
            history.setUpdateAt(new Date());
            history.setUpdateBy(account.getId());
            historyRepository.saveAndFlush(history);
        }
    }

    public Map<String, Integer> historyCharts() {
        List<HistorySongsDto> list = historyRepository.findSongsCharts();
        int pop = 0, rock = 0, jazz = 0, original = 0, cover = 0, light = 0;
        for (HistorySongsDto historySongsDto : list) {
            if (historySongsDto.getSongsType().equals("pop")) {
                pop = pop + historySongsDto.getTimes();
            } else if (historySongsDto.getSongsType().equals("rock")) {
                rock = rock + historySongsDto.getTimes();
            } else if (historySongsDto.getSongsType().equals("jazz")) {
                jazz = jazz + historySongsDto.getTimes();
            } else if (historySongsDto.getSongsType().equals("original")) {
                original = original + historySongsDto.getTimes();
            } else if (historySongsDto.getSongsType().equals("cover")) {
                cover = cover + historySongsDto.getTimes();
            } else if (historySongsDto.getSongsType().equals("light")) {
                light = light + historySongsDto.getTimes();
            }
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("pop", pop);
        map.put("rock", rock);
        map.put("jazz", jazz);
        map.put("original", original);
        map.put("cover", cover);
        map.put("light", light);
        return map;
    }

    public Float findAveragePoints(String songsId) {
        List<History> list = historyRepository.findHistoryBySongsId(Long.parseLong(songsId));
        if (list.isEmpty()) {
            return 0f;
        }else
        {
            int amount = list.size();
            int points = 0;
            for (History history : list) {
                points = points + history.getPoints();
            }
            // 保留1位小数
            BigDecimal b = new BigDecimal(points * 1.0f / amount * 1.0f);
            float m_price = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            return m_price;
        }
    }

    public Float giveMyPoints(String songsId, int points) {
        //  评分后返回平均分
        Account account = (Account) session.getAttribute("account");
        History history = historyRepository.findHistoryByAccountIdAndSongsId(Long.parseLong(account.getId()), Long.parseLong(songsId));
        if (history != null) {
            history.setPoints(points);
            historyRepository.saveAndFlush(history);
            return this.findAveragePoints(songsId);
        } else {
            return 0f;
        }

    }

    public int findMyPoints(String songsId) {
        Account account = (Account) session.getAttribute("account");
        History history = historyRepository.findHistoryByAccountIdAndSongsId(Long.parseLong(account.getId()), Long.parseLong(songsId));
        if (history != null) {
            return history.getPoints();
        } else {
            return 0;
        }
    }

}
