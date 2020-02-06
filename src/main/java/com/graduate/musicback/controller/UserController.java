package com.graduate.musicback.controller;

import com.graduate.musicback.dto.PasswordDto;
import com.graduate.musicback.dto.Result;
import com.graduate.musicback.dto.reportcomments.ReportCommentsDto;
import com.graduate.musicback.dto.songs.SongsPlayDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.entity.Comments;
import com.graduate.musicback.entity.Songs;
import com.graduate.musicback.service.AccountService;
import com.graduate.musicback.service.CommentsService;
import com.graduate.musicback.service.ReportCommentsService;
import com.graduate.musicback.service.SongsService;
import com.graduate.musicback.utils.Recommend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Api(description = "评论、举报评论、修改信息等在此", tags = "已登录用户的接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpSession session;

    @Autowired
    private SongsService songsService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ReportCommentsService reportCommentsService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private Recommend recommend;

    @ApiOperation("通过id找歌曲")
    @GetMapping("/find_songs_by_id/{id}")
    public Result<Songs> findSongsById(@PathVariable("id") String id) {
        Result<Songs> result = new Result<>();
        result.setCode(HttpStatus.OK).setMessage("find_songs_by_id").setData(songsService.findById(id));
        return result;
    }

    @ApiOperation("播放歌曲")
    @GetMapping("/play_songs/{id}")
    public Result<SongsPlayDto> playSongs(@PathVariable("id") String id) {
        return new Result<SongsPlayDto>(HttpStatus.OK, "play_songs", songsService.playSongs(id));
    }

    // 评论
    @ApiOperation("添加评论")
    @PostMapping("/add_comments")
    public Result<String> addComments(@RequestBody Comments comments) {
        Result<String> result = new Result<>();
        commentsService.add(comments);
        result.setCode(HttpStatus.OK).setData("success").setMessage("add_comments");
        return result;
    }

    @ApiOperation("删除评论")
    @GetMapping("/delete_comments/{commentsId}")
    public Result<String> deleteComments(@PathVariable("commentsId") String commentsId) {

        Result<String> result = new Result<>();
        Boolean check = commentsService.delete(commentsId);
        if (check) {
            result.setCode(HttpStatus.OK).setMessage("delete_comments").setData("success");
        } else {
            result.setCode(HttpStatus.OK).setMessage("delete_comments").setData("fail");
        }
        return result;
    }

    // 举报
    @ApiOperation("举报评论")
    @PostMapping("/add_report_comments")
    public Result<String> addReportComments(@RequestBody ReportCommentsDto reportCommentsDto) {
        Result<String> result = new Result<>();
        reportCommentsService.add(reportCommentsDto);
        result.setCode(HttpStatus.OK).setMessage("add_report_comments").setData("success");
        return result;
    }

    // 修改密码
    @ApiOperation("修改密码")
    @PostMapping("/update_password")
    public Result<String> updatePassword(@RequestBody PasswordDto passwordDto) {
        Result<String> result = new Result<>();
        Account account = (Account) session.getAttribute("account");
        if (passwordDto.getBeforePassword().equals(account.getPassword())) {
            accountService.updatePassword(account.getId(), passwordDto.getAfterPassword());
            result.setCode(HttpStatus.OK).setMessage("update_password").setData("success");
            // 清空session
            Enumeration em = session.getAttributeNames();
            while (em.hasMoreElements()) {
                session.removeAttribute(em.nextElement().toString());
            }
        } else {
            result.setCode(HttpStatus.OK).setMessage("update_password").setData("fail");
        }
        return result;
    }

    @ApiOperation("修改名称")
    @PostMapping("/update_name")
    public Result<String> updateName(@RequestBody Account account) {
        Result<String> result = new Result<>();
        accountService.updateName(account.getName());
        result.setCode(HttpStatus.OK).setMessage("update_name").setData("success");
        return result;
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public Result<String> logout() {
        Result<String> result = new Result<>();
        Enumeration em = session.getAttributeNames();
        while (em.hasMoreElements()) {
            session.removeAttribute(em.nextElement().toString());
        }
        result.setCode(HttpStatus.OK).setMessage("logout").setData("success");
        return result;
    }

    @ApiOperation("猜你喜欢")
    @GetMapping("/guess")
    public Result<String> guess () {
        Result<String> result = new Result<>();
        String songsId = recommend.guess();
        result.setCode(HttpStatus.OK).setMessage("guess").setData(songsId);
        return result;
    }
}
