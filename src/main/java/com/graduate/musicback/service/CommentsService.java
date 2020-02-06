package com.graduate.musicback.service;

import com.graduate.musicback.dto.comments.CommentsDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.Comments;
import com.graduate.musicback.repository.CommentsRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class CommentsService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private HttpSession session;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    // 通过某一个object的id查找所有评论
    public Page<CommentsDto> findCommentsByObjectId(int page, int size, String objectId) {
        page--; // 已减一
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentsDto> page1 = commentsRepository.findCommentsByObjectId(pageable, objectId);
        return page1;
    }

    public void add(Comments comments) {
        String username = (String) session.getAttribute("user");
        Account account = (Account) session.getAttribute("account");
        comments.setAccountId(account.getId()); // session获取
        comments.setId(snowflakeIdWorker.nextId());
        comments.setUpdateAt(new Date());
        comments.setUpdateBy(account.getId()); //session获取
        comments.setCreateAt(new Date());
        comments.setCreateBy(account.getId()); //session获取
        commentsRepository.save(comments);
    }

    public boolean delete(String commentsId) {
        Account account = (Account) session.getAttribute("account");
        Comments comments = commentsRepository.findCommentsById(commentsId);
        comments.setIsDel(true);
        comments.setUpdateAt(new Date());
        comments.setUpdateBy(account.getId()); // session获取
        commentsRepository.saveAndFlush(comments);
        return true;
    }

    public boolean restore(String commentsId) {
        Account account = (Account) session.getAttribute("account");
        Comments comments = commentsRepository.findCommentsById(commentsId);
        comments.setIsDel(false);
        comments.setUpdateAt(new Date());
        comments.setUpdateBy(account.getId()); // session获取
        commentsRepository.saveAndFlush(comments);
        return true;

    }
}
