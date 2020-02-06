package com.graduate.musicback.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@ApiModel("歌曲实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="t_songs")
public class Songs extends BaseEntity{
    @ApiModelProperty("歌曲封面")
    private String picture;

    @ApiModelProperty("歌曲地址")
    private String url;

    @ApiModelProperty("歌曲名称")
    private String name;

    @ApiModelProperty("歌词")
    private String lyrics;

    @ApiModelProperty("所属专辑的id")
    @Column(name="album_id")
    private String albumId;

    @ApiModelProperty("歌曲类型")
    @Column(name="type")
    private String type;

}
