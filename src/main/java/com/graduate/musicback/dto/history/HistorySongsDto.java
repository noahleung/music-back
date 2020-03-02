package com.graduate.musicback.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorySongsDto {
    // 统计不同类型歌曲播放比

    private String songsType;
    private int times;
}
