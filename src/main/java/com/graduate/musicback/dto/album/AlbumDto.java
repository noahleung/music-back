package com.graduate.musicback.dto.album;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// 每次查询专辑都要带上歌手名字
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {
    private String id;
    private String introduction;
    private String picture;
    private String name;
    private String singerName;
    private String singerId;
}
