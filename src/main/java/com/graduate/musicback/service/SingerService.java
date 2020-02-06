package com.graduate.musicback.service;

import com.graduate.musicback.dto.SearchDto;
import com.graduate.musicback.dto.search.SingerDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.Singer;
import com.graduate.musicback.repository.SingerRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
@Lazy
public class SingerService {


    @Autowired
    private HttpSession session;
    @Autowired
    private SingerRepository singerRepository;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private AlbumService albumService;

    public Page<Singer> findAllByKeywords(SearchDto searchDto) {
        // page页数已自动减1
        Pageable pageable = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());
        Page<Singer> page = singerRepository.findAllByKeywords(searchDto.getKeywords(), pageable);
        return page;
    }

    public Singer findById(String id) {
        return singerRepository.findSingerById(id);
    }

    public Page<Singer> findAllDeletedSinger(SearchDto searchDto){
        // page页数已自动减1
        Pageable pageable = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());
        Page<Singer> page = singerRepository.findAllDeleted(searchDto.getKeywords(),pageable);
        return page;
    }

    public List<SingerDto> findSearchSingerDto(){
        return singerRepository.findSearchSingerDto();
    }

    public void addOrUpdate(Singer singer) {
        Account account = (Account) session.getAttribute("account");
        if (singer.getId().equals("")) {
            singer.setId(snowflakeIdWorker.nextId());
            singer.setCreateAt(new Date());
            singer.setUpdateAt(new Date());
            singer.setCreateBy(account.getId()); // session获取
            singer.setUpdateBy(account.getId()); // session获取
            singer.setIsDel(false);
            singerRepository.save(singer);
        } else
        {
            Singer singer1 = singerRepository.findSingerById(singer.getId());
            singer1.setIntroduction(singer.getIntroduction());
            singer1.setName(singer.getName());
            singer1.setPicture(singer.getPicture());
            singer1.setUpdateAt(new Date());
            singer1.setUpdateBy(account.getId()); // session获取
            singerRepository.saveAndFlush(singer1);
        }

    }

    public void delete(String ids){
        Account account = (Account) session.getAttribute("account");
        String deleteIds[] = ids.split(",");
        Date updateAt = new Date();
        for(String i : deleteIds){
            singerRepository.delete(i,updateAt,account.getId()); // session获取
            albumService.deleteBySingerId(i);

        }
    }

    public void restore(String ids){
        Account account = (Account) session.getAttribute("account");
        String restoreIds[] = ids.split(",");
        Date updateAt = new Date();
        for(String i : restoreIds){
            singerRepository.restore(i,updateAt,account.getId()); // session获取
            albumService.restoreBySingerId(i);
        }
    }
}
