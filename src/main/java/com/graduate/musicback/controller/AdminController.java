package com.graduate.musicback.controller;

import com.graduate.musicback.dto.PageResult;
import com.graduate.musicback.dto.Result;
import com.graduate.musicback.dto.SearchDto;
import com.graduate.musicback.dto.album.AlbumDto;
import com.graduate.musicback.dto.reportcomments.ReportCommentsSearchDto;
import com.graduate.musicback.dto.search.SingerDto;
import com.graduate.musicback.dto.songs.SongsDto;
import com.graduate.musicback.entity.Album;
import com.graduate.musicback.entity.Singer;
import com.graduate.musicback.entity.Songs;
import com.graduate.musicback.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Api(description = "", tags = "工作人员接口")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AlbumService albumService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private SongsService songsService;


    @Autowired
    private ReportCommentsService reportCommentsService;

    @PostMapping("/find_all_deleted_album")
    public Result<PageResult<AlbumDto>> findAllDeletedAlbum(@RequestBody SearchDto searchDto) {
        Result<PageResult<AlbumDto>> result = new Result<>();
        try {
            PageResult<AlbumDto> pageResult = new PageResult<>();
            Page<AlbumDto> page = albumService.findAllDeletedAlbum(searchDto);
            pageResult.setTotal(page.getTotalElements()).setSize(searchDto.getSize()).setPage(searchDto.getPage()).setData(page.getContent());
            result.setCode(HttpStatus.OK).setData(pageResult).setMessage("find_all_deleted_album");
        } catch (Exception e) {
            result.setCode(HttpStatus.OK).setData(null).setMessage("find_all_deleted_album");
        }

        return result;
    }

    // 获取专辑标签
    @GetMapping("/find_search_album_dto")
    public Result<List<com.graduate.musicback.dto.search.AlbumDto>> findSearchAlbumDto () {
        Result<List<com.graduate.musicback.dto.search.AlbumDto>> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_search_album_dto").setData(albumService.findSearchAlbumDto());
        return result;
    }


    @GetMapping("/album_delete/{ids}")
    public Result<String> albumDelete(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        albumService.delete(ids);
        result.setCode(HttpStatus.OK).setMessage("album_delete").setData("success");
        return result;
    }

    @GetMapping("/album_restore/{ids}")
    public Result<String> albumRestore(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        albumService.restore(ids);
        result.setCode(HttpStatus.OK).setMessage("album_restore").setData("success");
        return result;
    }

    @PostMapping("/album_add_or_update")
    public Result<String> albumAddOrUpdate(@RequestBody Album album) throws ParseException {
        Result<String> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("album_addOrUpdate").setData("success");
        albumService.addOrUpdate(album);
        return result;
    }
    // -----------------singer---------------
    @PostMapping("/singer_add_or_update")
    public Result<String> singerAddOrUpdate(@RequestBody Singer singer) {
        Result<String> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("singer_addOrUpdate").setData("success");
        singerService.addOrUpdate(singer);
        return result;
    }

    //    @PostMapping("/singer_update")
    @GetMapping("/singer_delete/{ids}")
    public Result<String> singerDelete(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        singerService.delete(ids);
        result.setCode(HttpStatus.OK).setMessage("singer_delete").setData("success");
        return result;
    }

    @GetMapping("/singer_restore/{ids}")
    public Result<String> singerRestore(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        singerService.restore(ids);
        result.setCode(HttpStatus.OK).setMessage("singer_restore").setData("success");
        return result;
    }

    // 获取歌手标签
    @GetMapping("/find_search_singer_dto")
    public Result<List<SingerDto>> findSearchSingerDto () {
        Result<List<SingerDto>> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_search_singer_dto").setData(singerService.findSearchSingerDto());
        return result;
    }


    @PostMapping("/find_all_deleted_singer")
    public Result<PageResult<Singer>> findAllDeletedSinger(@RequestBody SearchDto searchDto) {
        Result<PageResult<Singer>> result = new Result<>();
        try {
            PageResult<Singer> pageResult = new PageResult<>();
            Page<Singer> page = singerService.findAllDeletedSinger(searchDto);
            pageResult.setTotal(page.getTotalElements()).setSize(searchDto.getSize()).setPage(searchDto.getPage()).setData(page.getContent());
            result.setCode(HttpStatus.OK).setData(pageResult).setMessage("find_all_deleted_singer");
        } catch (Exception e) {
            result.setCode(HttpStatus.OK).setData(null).setMessage("find_all_deleted_singer");
        }

        return result;
    }
    //  -----------------songs--------------
    @GetMapping("/songs_restore/{ids}")
    public Result<String> songsRestore(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        songsService.restore(ids);
        result.setCode(HttpStatus.OK).setMessage("songs_restore").setData("success");
        return result;
    }

    @PostMapping("/find_all_deleted_songs")
    public Result<PageResult<SongsDto>> findAllDeletedSongs(@RequestBody SearchDto searchDto) {
        Result<PageResult<SongsDto>> result = new Result<>();
        try {
            PageResult<SongsDto> pageResult = new PageResult<>();
            Page<SongsDto> page = songsService.findAllDeletedSongs(searchDto);
            pageResult.setTotal(page.getTotalElements()).setSize(searchDto.getSize()).setPage(searchDto.getPage()).setData(page.getContent());
            result.setCode(HttpStatus.OK).setData(pageResult).setMessage("find_all_deleted_songs");
        } catch (Exception e) {
            result.setCode(HttpStatus.OK).setData(null).setMessage("find_all_deleted_songs");
        }
        return result;
    }

    @PostMapping("/songs_add_or_update")
    public Result<String> songsAddOrUpdate(@RequestBody Songs songs) {
        Result<String> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("songs_add_or_update").setData("success");
        songsService.addOrUpdate(songs);
        return result;
    }

    @GetMapping("/songs_delete/{ids}")
    public Result<String> songsDelete(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        songsService.delete(ids);
        result.setCode(HttpStatus.OK).setMessage("songs_delete").setData("success");
        return result;
    }
    // ---------------report-------------
    @GetMapping("/report_find_all_none/{page}/{size}")
    public Result<PageResult<ReportCommentsSearchDto>> reportFindAllNone (@PathVariable("page") int page,@PathVariable("size") int size){
        Result<PageResult<ReportCommentsSearchDto>> result = new Result<>();
        PageResult<ReportCommentsSearchDto> pageResult =new PageResult<>();
        Page<ReportCommentsSearchDto> page1 =reportCommentsService.findAllByType(page,size,"none");
        pageResult.setData(page1.getContent()).setTotal(page1.getTotalElements()).setSize(size).setPage(page);
        result.setData(pageResult).setCode(HttpStatus.OK).setMessage("find_all_none");
        return result;
    }

    @GetMapping("/report_find_all_pass/{page}/{size}")
    public Result<PageResult<ReportCommentsSearchDto>> reportFindAllPass (@PathVariable("page") int page,@PathVariable("size") int size){
        Result<PageResult<ReportCommentsSearchDto>> result = new Result<>();
        PageResult<ReportCommentsSearchDto> pageResult =new PageResult<>();
        Page<ReportCommentsSearchDto> page1 =reportCommentsService.findAllByType(page,size,"pass");
        pageResult.setData(page1.getContent()).setTotal(page1.getTotalElements()).setSize(size).setPage(page);
        result.setData(pageResult).setCode(HttpStatus.OK).setMessage("find_all_pass");
        return result;
    }

    @GetMapping("/report_find_all_reject/{page}/{size}")
    public Result<PageResult<ReportCommentsSearchDto>> reportFindAllReject (@PathVariable("page") int page,@PathVariable("size") int size){
        Result<PageResult<ReportCommentsSearchDto>> result = new Result<>();
        PageResult<ReportCommentsSearchDto> pageResult =new PageResult<>();
        Page<ReportCommentsSearchDto> page1 =reportCommentsService.findAllByType(page,size,"reject");
        pageResult.setData(page1.getContent()).setTotal(page1.getTotalElements()).setSize(size).setPage(page);
        result.setData(pageResult).setCode(HttpStatus.OK).setMessage("find_all_reject");
        return result;
    }


    // 审核通过，删除某条评论
    @GetMapping("/report_pass/{ids}")
    public Result<String> reportPass(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        reportCommentsService.passOrReject(ids,"pass");
        result.setCode(HttpStatus.OK).setMessage("report_pass").setData("suceess");
        return result;
    }

    // 审核不通过，某评论不被删除
    @GetMapping("/report_reject/{ids}" )
    public Result<String> reportReject(@PathVariable("ids") String ids) {
        Result<String> result = new Result<>();
        reportCommentsService.passOrReject(ids,"reject");
        result.setCode(HttpStatus.OK).setMessage("report_reject").setData("suceess");
        return result;
    }

    @GetMapping("/check_login")
    public Result<String> checkLogin(){
        return new Result<String>().setCode(HttpStatus.OK).setMessage("check_login").setData("success");
    }
}
