package com.graduate.musicback.service;

import com.graduate.musicback.dto.SearchDto;
import com.graduate.musicback.dto.songs.SongsDto;
import com.graduate.musicback.dto.songs.SongsPlayDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.Album;
import com.graduate.musicback.entity.Songs;
import com.graduate.musicback.repository.AlbumRepository;
import com.graduate.musicback.repository.SongsRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SongsService {

    @Autowired
    private HttpSession session;
    @Autowired
    private SongsRepository songsRepository;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private AlbumRepository albumRepository;

    public Page<SongsDto> findAllByKeywords(SearchDto searchDto) {
        // page页数已自动减1
        Pageable pageable = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());
        Page<SongsDto> page = songsRepository.findAllByKeywords(searchDto.getKeywords(), pageable);
        return page;
    }

    public Songs findById(String id) {
        return songsRepository.findSongsById(id);
    }

    public List<Songs> findSongsByAlbumId(String albumId) {
        return songsRepository.findSongsByAlbumId(albumId);
    }

    public Page<SongsDto> findAllDeletedSongs(SearchDto searchDto){
        // page页数已自动减1
        Pageable pageable = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());
        Page<SongsDto> page = songsRepository.findAllDeleted(searchDto.getKeywords(),pageable);
        return page;
    }

    public Page<SongsDto> findSongsBySingerId (int page,int size,String singerId) {
        page--;
        Pageable pageable=PageRequest.of(page,size);
        Page<SongsDto> page1=songsRepository.findSongsDtoBySingerId(singerId,pageable);
        return page1;
    }

    // 随机推送10首歌
    public List<SongsDto> findSongsByRandom(){
        List<SongsDto> list = new ArrayList<>();
        List<Object []> list1 = songsRepository.find_songs_by_random();

        for(int i=0;i<list1.size();i++){
            SongsDto songs = new SongsDto();
            songs.setId((String)list1.get(i)[0]);
            songs.setName((String)list1.get(i)[1]);
            songs.setAlbumName((String)list1.get(i)[2]);
            songs.setSingerName((String)list1.get(i)[3]);
            songs.setPicture((String)list1.get(i)[4]);
            songs.setAlbumId((String)list1.get(i)[5]);
            songs.setSingerId((String)list1.get(i)[6]);
            songs.setType((String) list1.get(i)[7]);
            list.add(songs);
        }
        return list;
    }

    // 统计7天内被播放次数最多的歌曲
    public List<SongsDto> findTopTenSongs(){
        Date nowDate= new Date();
        Date oldDate= new Date( (nowDate.getTime()) - (1000*60*60*24*7));
        List<SongsDto> list = new ArrayList<>();
        List<Object[]> list1 = songsRepository.find_top_ten_songs(oldDate,nowDate);
        for(int i=0;i<list1.size();i++){
            SongsDto songs = new SongsDto();
            songs.setId((String)list1.get(i)[0]);
            songs.setName((String)list1.get(i)[1]);
            songs.setAlbumName((String)list1.get(i)[2]);
            songs.setSingerName((String)list1.get(i)[3]);
            songs.setPicture((String)list1.get(i)[4]);
            songs.setAlbumId((String)list1.get(i)[5]);
            songs.setSingerId((String)list1.get(i)[6]);
            songs.setType((String) list1.get(i)[7]);
            list.add(songs);
        }

        return list;
    }

    public SongsDto findSongsDtoById(String id){

        return songsRepository.findSongsDtoById(id);
    }

    public void addOrUpdate(Songs songs) {
        Account account = (Account) session.getAttribute("account");
        if (songs.getId().equals("")) {
            songs.setId(snowflakeIdWorker.nextId());
            songs.setCreateAt(new Date());
            songs.setUpdateAt(new Date());
            songs.setCreateBy(account.getId()); // session获取
            songs.setUpdateBy(account.getId()); // session获取
            songs.setIsDel(false);
            if(songs.getPicture().equals("") || songs.getPicture() == null) {
                Album album = albumRepository.findAlbumById(songs.getAlbumId());
                songs.setPicture(album.getPicture());
            }
            songsRepository.save(songs);
        } else
        {
            Songs songs1 = songsRepository.findSongsById(songs.getId());
            songs1.setName(songs.getName());
            songs1.setPicture(songs.getPicture());
            songs1.setLyrics(songs.getLyrics());
            songs1.setAlbumId(songs.getAlbumId());
            songs1.setType(songs.getType());
            songs1.setUrl(songs.getUrl());
            songs1.setUpdateAt(new Date());
            songs1.setUpdateBy(account.getId()); // session获取
            songsRepository.saveAndFlush(songs1);
        }

    }

    public void delete(String ids) {
        Account account = (Account) session.getAttribute("account");
        String deletedIds[] = ids.split(",");
        Date updateAt = new Date();
        for(String id : deletedIds){
            songsRepository.delete(id,updateAt,account.getId()); // session获取
        }
    }

    public void deleteByAlbumId(String albumId)
    {
        List<Songs> list = songsRepository.findSongsByAlbumId(albumId);
        for(Songs songs : list){
            this.delete(songs.getId());
        }
    }

    public void restore(String ids){
        Account account = (Account) session.getAttribute("account");
        String restoreIds[] = ids.split(",");
        for(String id : restoreIds){
            songsRepository.restore(id,new Date(),account.getId()); // session获取
        }
    }

    public void restoreByAlbumId(String albumId){

        List<Songs> list = songsRepository.findSongsByAlbumId(albumId);
        for(Songs songs : list){
            this.restore(songs.getId());
        }
    }

    public SongsPlayDto playSongs(String id){
        historyService.add(id);
        return songsRepository.playSongs(id);
    }
}
