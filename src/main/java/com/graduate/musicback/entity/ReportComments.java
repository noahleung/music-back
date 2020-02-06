package com.graduate.musicback.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@ApiModel("举报实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="t_report_comments")
public class ReportComments extends BaseEntity{
    @ApiModelProperty("举报者id")
    @Column(name="account_id")
    private String accountId;

    @ApiModelProperty("被举报的评论id")
    @Column(name="comments_id")
    private String commentsId;

    @ApiModelProperty("举报的理由")
    @Column(name="reason")
    private String reason;

    @ApiModelProperty("举报的结果")
    @Column(name="result")
    private String result;


}
