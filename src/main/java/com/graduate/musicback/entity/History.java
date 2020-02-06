package com.graduate.musicback.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    private String accountId;

    @Column(name = "songs_id")
    @ApiModelProperty("歌曲的id")
    private String songsId;


}
