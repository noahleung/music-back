package com.graduate.musicback.utils;

import com.graduate.musicback.dto.songs.SongsDto;
import com.graduate.musicback.service.SongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Rank {
    // 对排行榜进行排行，避免了每个用户重复查询

    @Autowired
    private SongsService songsService;

    public static List<SongsDto> topSongsList = new ArrayList<>();

    @PostConstruct // spring初始化时执行以下这个方法
    public void RankTest() {
        Rank.topSongsList = songsService.findTopTenSongs();
    }

    // 每晚12点更新排行
    @Scheduled(cron = "0 0 0 * * ?")
    public void rank() {
        if (Rank.topSongsList.isEmpty()) {
            Rank.topSongsList = songsService.findTopTenSongs();
        }
    }
}
