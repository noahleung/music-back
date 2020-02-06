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

@ApiModel("歌手实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="t_singer")
public class Singer extends BaseEntity{

    @ApiModelProperty("歌手封面")
    private String picture;

    @ApiModelProperty("歌手介绍")
    private String introduction;

    @ApiModelProperty("歌手姓名")
    private String name;
}
