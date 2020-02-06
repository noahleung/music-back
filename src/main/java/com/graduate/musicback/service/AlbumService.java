package com.graduate.musicback.service;

import com.graduate.musicback.dto.Result;
import com.graduate.musicback.dto.SearchDto;
import com.graduate.musicback.dto.album.AlbumDto;
import com.graduate.musicback.dto.search.SingerDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.Album;
import com.graduate.musicback.entity.Singer;
import com.graduate.musicback.repository.AlbumRepository;
import com.graduate.musicback.repository.SingerRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private HttpSession session;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private SongsService songsService;

    @Autowired
    private SingerRepository singerRepository;

    public Page<AlbumDto> findAllByKeywords(SearchDto searchDto) {
        // page页数已自动减1
        Pageable pageable = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());
        Page<AlbumDto> page = albumRepository.findAllByKeywords(searchDto.getKeywords(), pageable);
        return page;
    }

    public List<com.graduate.musicback.dto.search.AlbumDto> findSearchAlbumDto () {
               return albumRepository.findSearchAlbumDto();
    }

    public AlbumDto findById(String id) {
        return albumRepository.findAlbumDtoById(id);
    }
    public Page<AlbumDto> findAlbumBySingerId(int page,int size,String singerId) {
        page--;
        // page页数已自动减1
        Pageable pageable =PageRequest.of(page,size);
        Page<AlbumDto> page1 = albumRepository.findAlbumDtoBySingerId(singerId,pageable);
        return page1;
    }

    public List<AlbumDto> findAlbumByRandom () {
        List<AlbumDto> list = new ArrayList<>();
        List<Object[]> objectList = albumRepository.findAlbumByRandom();
        for(int i =0;i<objectList.size();i++) {
            AlbumDto album = new AlbumDto();
            album.setId((String)objectList.get(i)[0]);
            album.setIntroduction((String)objectList.get(i)[1]);
            album.setPicture((String)objectList.get(i)[2]);
            album.setName((String)objectList.get(i)[3]);
            album.setSingerName((String)objectList.get(i)[4]);
            album.setSingerId((String) objectList.get(i)[5]);
            list.add(album);
        }
       return list;
    }
    public Page<AlbumDto> findAllDeletedAlbum(SearchDto searchDto){
        // page页数已自动减1
        Pageable pageable = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());
        Page<AlbumDto> page = albumRepository.findAllDeleted(searchDto.getKeywords(),pageable);
        return page;
    }

    public void addOrUpdate(Album album) {
        Account account = (Account) session.getAttribute("account");
        if (album.getId().equals("")) {
            album.setId(snowflakeIdWorker.nextId());
            album.setCreateAt(new Date());
            album.setUpdateAt(new Date());
            album.setCreateBy(account.getId()); // session获取
            album.setUpdateBy(account.getId()); // session获取
            album.setIsDel(false);
            if(album.getPicture().equals("") || album.getPicture() == null) {
                // 当专辑的图片为空，则使用人物的照片作为专辑照片
                Singer singer = singerRepository.findSingerById(album.getSingerId());
                album.setPicture(singer.getPicture());
            }
            albumRepository.save(album);
        } else
        {
            Album album1 = albumRepository.findAlbumById(album.getId());
            album1.setIntroduction(album.getIntroduction());
            album1.setName(album.getName());
            album1.setSingerId(album.getSingerId());
            album1.setPicture(album.getPicture());
            album1.setUpdateAt(new Date());
            album1.setUpdateBy(account.getId()); // session获取
            albumRepository.saveAndFlush(album1);
        }

    }

    public void delete(String ids){
        Account account = (Account) session.getAttribute("account");
        String deleteIds[] = ids.split(",");
        Date updateAt = new Date();
        for(String i : deleteIds){
            albumRepository.delete(i,updateAt,account.getId()); // session获取
            songsService.deleteByAlbumId(i);
        }
    }

    public void deleteBySingerId(String singerId){
        List<Album> list=albumRepository.findAlbumBySingerId(singerId);
        for(Album album : list){
            this.delete(album.getId());
        }
    }

    public void restore(String ids){
        Account account = (Account) session.getAttribute("account");
        String restoreIds[] = ids.split(",");
        Date updateAt = new Date();
        for(String i : restoreIds){
            albumRepository.restore(i,updateAt,account.getId()); // session获取
            songsService.restoreByAlbumId(i);
        }
    }

    public void restoreBySingerId(String singerId){
        List<Album> list=albumRepository.findAlbumBySingerId(singerId);
        for(Album album : list){
            this.restore(album.getId());
        }
    }
}
