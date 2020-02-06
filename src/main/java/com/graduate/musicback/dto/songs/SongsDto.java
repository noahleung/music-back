package com.graduate.musicback.dto.songs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 每次查询歌曲都要带上专辑名和歌手名，给admin用
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongsDto implements Serializable {
    private String id;
    private String name;
    private String albumName;
    private String singerName;
    private String picture;
    private String albumId;
    private String singerId;
    private String type;
}
