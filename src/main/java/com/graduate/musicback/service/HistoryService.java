package com.graduate.musicback.service;

import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.History;
import com.graduate.musicback.repository.HistoryRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

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
        History  history = historyRepository.findHistoryByAccountIdAndSongsId(account.getId(), id);
        if (history == null) {
            history = new History();
            history.setSongsId(id);
            history.setAccountId(account.getId());
            history.setId(snowflakeIdWorker.nextId());
            history.setCreateAt(new Date());
            history.setCreateBy(account.getId());
            history.setUpdateAt(new Date());
            history.setUpdateBy(account.getId());
            history.setIsDel(false);
            historyRepository.save(history);
        } else {
            history.setUpdateAt(new Date());
            history.setUpdateBy(account.getId());
            historyRepository.saveAndFlush(history);
        }
    }
}
