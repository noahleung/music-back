package com.graduate.musicback.service;

import com.graduate.musicback.dto.reportcomments.ReportCommentsDto;
import com.graduate.musicback.dto.reportcomments.ReportCommentsSearchDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.ReportComments;
import com.graduate.musicback.repository.ReportCommentsRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class ReportCommentsService {
    @Autowired
    private HttpSession session;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private ReportCommentsRepository reportCommentsRepository;

    @Autowired
    private CommentsService commentsService;

    public void add(ReportCommentsDto reportCommentsDto) {
        Account account = (Account) session.getAttribute("account");
        ReportComments reportComments = new ReportComments();
        reportComments.setAccountId(account.getId());
        reportComments.setReason(reportCommentsDto.getReason());
        reportComments.setCommentsId(reportCommentsDto.getCommentsId());
        reportComments.setResult("none");
        reportComments.setId(snowflakeIdWorker.nextId());
        reportComments.setCreateAt(new Date());
        reportComments.setCreateBy(account.getId());
        reportComments.setUpdateAt(new Date());
        reportComments.setUpdateBy(account.getId());
        reportComments.setIsDel(false);
        reportCommentsRepository.save(reportComments);
    }

    // 通过pass (评论会被删除)或驳回reject
    public void passOrReject(String ids, String result) {
//        Account account = (Account) session.getAttribute("account");
        String id[] = ids.split(",");
        for(String cid : id) {
            ReportComments reportComments = reportCommentsRepository.findReportCommentsById(cid);
            reportComments.setResult(result);
            reportComments.setUpdateAt(new Date());
//            reportComments.setUpdateBy(account.getId());
            reportComments.setUpdateBy("test"); // session 代替
            if (result.equals("pass")) {
                commentsService.delete(reportComments.getCommentsId());
            } else {
                commentsService.restore(reportComments.getCommentsId());
            }
            reportCommentsRepository.saveAndFlush(reportComments);
        }
    }

    // type只能是pass / reject / none
    public Page<ReportCommentsSearchDto> findAllByType(int page, int size, String type) {
        page--;
        Pageable pageable = PageRequest.of(page, size);
        Page<ReportCommentsSearchDto> page1 = reportCommentsRepository.findAllByType(pageable, type);
        return page1;
    }

}
