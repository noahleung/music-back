package com.graduate.musicback.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@ApiModel("播放历史")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="t_history")
public class History extends BaseEntity{

    @Column(name="account_id")
    @ApiModelProperty("播放着id")
    private Long accountId;

    @Column(name = "songs_id")
    @ApiModelProperty("歌曲的id")
    private Long songsId;

    private int times;

    @Id
    private String id;

    @ApiModelProperty("创建人员")
    @Column(name = "create_by")
    private String createBy;

    @ApiModelProperty("更新人员")
    @Column(name = "update_by")
    private String updateBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_at")
    private Date createAt;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_at")
    private Date updateAt;

    @ApiModelProperty("是否删除")
    @Column(name = "is_del")
    private Boolean isDel = Boolean.FALSE;

    private int points;

}
