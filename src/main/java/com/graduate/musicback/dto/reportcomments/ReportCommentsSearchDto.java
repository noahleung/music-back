package com.graduate.musicback.dto.reportcomments;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCommentsSearchDto {
    private String id; // 评论id
    private String accountId; // 发评论人的id
    private String accountName; // 发评论人的名字
    private String commentsId; // 评论id
    private String commentsContent; // 评论内容
    private String reason; // 举报理由
    private String result; // 结果
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createAt; // 评论日期
}
