package com.graduate.musicback.repository;

import com.graduate.musicback.dto.reportcomments.ReportCommentsSearchDto;
import com.graduate.musicback.entity.ReportComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReportCommentsRepository extends JpaRepository<ReportComments, String> {
    // 软删除一个举报信息
    @Modifying
    @Transactional
    @Query(value = "update t_report_comments set is_del = true where id = :#{#id}", nativeQuery = true)
    void delete(String id);

    ReportComments findReportCommentsById(String id);

    @Query(value = "select new com.graduate.musicback.dto.reportcomments.ReportCommentsSearchDto" +
            " (report.id, account.id, account.name, comments.id, comments.content,report.reason,report.result,report.createAt) " +
            " from ReportComments report,Comments comments, Account account " +
            " where report.commentsId = comments.id and report.accountId = account.id and" +
            " report.result = :#{#result} ")
    Page<ReportCommentsSearchDto> findAllByType(Pageable pageable,String result);

}
