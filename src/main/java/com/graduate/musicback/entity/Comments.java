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

@ApiModel("歌曲实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="t_comments")
public class Comments extends BaseEntity{
    @Column(name = "account_id")
    @ApiModelProperty("评论者id")
    private String accountId;

    @Column(name = "object_id")
    @ApiModelProperty("被评论的歌曲/专辑的id")
    private String objectId;

    @ApiModelProperty("评论内容")
    private String content;
}
