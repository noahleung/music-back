package com.graduate.musicback.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@ApiModel("专辑实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name="t_album")
public class Album extends BaseEntity{

    @ApiModelProperty("专辑封面")
    private String picture;

    @ApiModelProperty("专辑介绍")
    private String introduction;

    @ApiModelProperty("歌手id")
    @Column(name = "singer_id")
    private String singerId;

    @ApiModelProperty("专辑名称")
    private String name;
}
