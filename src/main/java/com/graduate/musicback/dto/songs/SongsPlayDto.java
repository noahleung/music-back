package com.graduate.musicback.dto.songs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongsPlayDto {
    // 播放器一首歌需要的信息，给播放者用
    private String id;
    private String name; // title
    private String url;
    private String picture;
    private String lyrics;
    private String singerName;
    private String albumName;
    private String type;
}
