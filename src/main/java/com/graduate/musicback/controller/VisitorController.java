package com.graduate.musicback.controller;

import com.graduate.musicback.dto.PageResult;
import com.graduate.musicback.dto.Result;
import com.graduate.musicback.dto.SearchDto;
import com.graduate.musicback.dto.album.AlbumDto;
import com.graduate.musicback.dto.comments.CommentsDto;
import com.graduate.musicback.dto.songs.SongsDto;
import com.graduate.musicback.entity.Singer;
import com.graduate.musicback.entity.Songs;
import com.graduate.musicback.service.*;
import com.graduate.musicback.utils.Rank;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(description = "", tags = "游客接口")
@RestController
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    private SingerService singerService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SongsService songsService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private HistoryService historyService;


    // ----------------album

    // 通过名字模糊查询专辑
    @ApiOperation("通过名字模糊查询专辑")
    @PostMapping("/find_album_by_keywords")
    public Result<PageResult<AlbumDto>> findAlbumByKeywords(@RequestBody SearchDto searchDto) {
        Result<PageResult<AlbumDto>> result = new Result<>();
        try {
            Page<AlbumDto> page = albumService.findAllByKeywords(searchDto);
            PageResult<AlbumDto> pageResult = new PageResult<>();
            pageResult.setData(page.getContent()).setPage(searchDto.getPage()).setSize(searchDto.getSize()).setTotal(page.getTotalElements());
            result.setCode(HttpStatus.OK).setMessage("find_album_by_album_name").setData(pageResult);
        } catch (Exception e) {
            result.setCode(HttpStatus.OK).setMessage("find_album_by_album_name").setData(null);
        }
        return result;
    }

    // 通过id找专辑
    @ApiOperation("通过id找专辑")
    @GetMapping("/find_album_by_id/{id}")
    public Result<AlbumDto> findAlbumById(@PathVariable("id") String id) {
        Result<AlbumDto> result = new Result<>();
        AlbumDto album = albumService.findById(id);
        result.setCode(HttpStatus.OK).setMessage("find_album_by_id").setData(album);
        return result;
    }

    @ApiOperation("通过歌手id找全部专辑")
    @GetMapping("/find_album_by_singer_id/{page}/{size}/{singerId}")
    public Result<PageResult<AlbumDto>> findAlbumBySingerId(@PathVariable("page")int page,@PathVariable("size") int size,@PathVariable("singerId") String singerId) {
        Result<PageResult<AlbumDto>> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_album_by_singer_id");
        PageResult<AlbumDto> pageResult =new PageResult<>();
        Page<AlbumDto> page1 =albumService.findAlbumBySingerId(page,size,singerId);
        pageResult.setData(page1.getContent()).setPage(page).setSize(size).setTotal(page1.getTotalElements());
        result.setData(pageResult);
        return result;
    }


    @ApiOperation("随机推荐专辑")
    @GetMapping("/find_album_by_random")
    public Result<List<AlbumDto>> findAlbumByRandom() {
        Result<List<AlbumDto>> result = new Result<>();
        List<AlbumDto> list = albumService.findAlbumByRandom();
        result.setCode(HttpStatus.OK).setMessage("find_album_by_random").setData(list);
        return  result;
    }

// -------------------Songs
    // 通过歌名模糊查询歌曲
    @ApiOperation("通过歌名模糊查询歌曲")
    @PostMapping("/find_songs_by_keywords")
    public Result<PageResult<SongsDto>> findSongsByKeywords(@RequestBody SearchDto searchDto) {
        Result<PageResult<SongsDto>> result = new Result<>();
        try {
            Page<SongsDto> page = songsService.findAllByKeywords(searchDto);
            PageResult<SongsDto> pageResult = new PageResult<>();
            pageResult.setData(page.getContent()).setPage(searchDto.getPage()).setSize(searchDto.getSize()).setTotal(page.getTotalElements());
            result.setCode(HttpStatus.OK).setMessage("find_songs_by_songs_name").setData(pageResult);
        } catch (Exception e) {
            result.setCode(HttpStatus.OK).setMessage("find_songs_by_songs_name").setData(null);
        }

        return result;
    }

    @ApiOperation("通过歌手Id找所有歌曲")
    @GetMapping("/find_songs_by_singer_id/{page}/{size}/{singerId}")
    public Result<PageResult<SongsDto>> findSongsBySingerId(@PathVariable("page")int page,@PathVariable("size") int size,@PathVariable("singerId") String singerId) {
        Result<PageResult<SongsDto>> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_songs_by_singer_id");
        PageResult<SongsDto> pageResult = new PageResult<>();
        Page<SongsDto> page1 = songsService.findSongsBySingerId(page,size,singerId);
        pageResult.setData(page1.getContent()).setPage(page).setSize(size).setTotal(page1.getTotalElements());
        result.setData(pageResult);
        return result;
    }
    @ApiOperation("通过专辑id找所有属于该专辑的歌曲")
    @GetMapping("/find_songs_by_album_id/{albumId}")
    public Result<List<Songs>> findSongsByAlbumId(@PathVariable("albumId") String albumId) {
      Result<List<Songs>> result = new Result<>(HttpStatus.OK,"find_songs_by_album_id",songsService.findSongsByAlbumId(albumId));
      return  result;
    }


    // 查找每7天播放量前十的歌曲 排行榜，每天12点更细
    @ApiOperation("查找每周播放量前十的歌曲")
    @GetMapping("/find_top_ten_songs")
    public Result<List<SongsDto>> findTopTenSongs() {
        Result<List<SongsDto>> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_top_ten_songs").setData(Rank.topSongsList);
        return result;
    }

    // 随机推送10首歌
    @ApiOperation("随机推送10首歌")
    @GetMapping("/find_songs_by_random")
    public Result<List<SongsDto>> findSongsByRandom() {
        Result<List<SongsDto>> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_songs_by_random").setData(songsService.findSongsByRandom());
        return result;
    }

    @ApiOperation("获取歌曲dto")
    @GetMapping("/find_songs_dto_by_id/{id}")
    public Result<SongsDto> findSongsDtoById (@PathVariable("id") String id ){
        Result<SongsDto> result = new Result<>();
        SongsDto songsDto =songsService.findSongsDtoById(id);
        result.setCode(HttpStatus.OK).setMessage("find_songs_dto_by_id").setData(songsDto);
        return result;
    }


// --------------singer

    // 通过歌手名模糊查询歌手
    @ApiOperation("通过歌手名模糊查询歌手")
    @PostMapping("/find_singer_by_keywords")
    public Result<PageResult<Singer>> findSingerByKeywords(@RequestBody SearchDto searchDto) {
        Result<PageResult<Singer>> result = new Result<>();
        try {
            Page<Singer> page = singerService.findAllByKeywords(searchDto);
            PageResult<Singer> pageResult = new PageResult<>();
            pageResult.setData(page.getContent()).setPage(searchDto.getPage()).setSize(searchDto.getSize()).setTotal(page.getTotalElements());
            result.setCode(HttpStatus.OK).setMessage("find_singer_by_singer_name").setData(pageResult);
        } catch (Exception e) {
            result.setCode(HttpStatus.OK).setMessage("find_singer_by_singer_name").setData(null);
        }

        return result;
    }

    // 通过id找歌手
    @ApiOperation("通过id找歌手")
    @GetMapping("/find_singer_by_id/{id}")
    public Result<Singer> findSingerById(@PathVariable("id") String id) {
        Singer singer = singerService.findById(id);
        Result<Singer> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("success").setData(singer);
        return result;
    }

    @GetMapping("/find_comments_by_object_id/{page}/{size}/{objectId}")
    public Result<PageResult<CommentsDto>> findCommentsByObjectId(@PathVariable("objectId") String objectId, @PathVariable("page") int page, @PathVariable("size") int size){
        Result<PageResult<CommentsDto>> result= new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_comments_by_object_id");
        PageResult<CommentsDto> pageResult =new PageResult<>();
        Page<CommentsDto> page1 = commentsService.findCommentsByObjectId(page,size,objectId);
        pageResult.setPage(page).setSize(size).setTotal(page1.getTotalElements()).setData(page1.getContent());
        result.setData(pageResult);
        return result;
    }

    @GetMapping("/find_average_points/{songsId}")
    public Result<Float> findAveragePoints(@PathVariable("songsId") String songsId){
        Result<Float> result =new Result<>();
        result.setCode(HttpStatus.OK).setMessage("average_points").setData(historyService.findAveragePoints(songsId));
        return result;
    }

}
