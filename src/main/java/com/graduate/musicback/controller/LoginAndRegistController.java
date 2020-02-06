package com.graduate.musicback.controller;

import com.graduate.musicback.dto.*;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.service.AccountService;
import com.graduate.musicback.utils.EmailSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@RestController
@RequestMapping("/login_and_regist")
@Api(tags = "注册登录用的")
public class LoginAndRegistController {

    @Autowired
    private HttpSession session;
    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailSender emailSender;

    // 普通用户登录
    @ApiOperation("普通用户登录")
    @PostMapping("/user_login")
    public Result<String> userLogin(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        Result<String> result = new Result<>();

        if (accountService.isUserExistByUsername(loginDto.getUsername())) {
            // 类型为user的用户存在
            Account account = accountService.findUserByUsername(loginDto.getUsername());
            if (account.getPassword().equals(loginDto.getPassword())) {
                request.getSession().setAttribute("user", account.getUsername());
                request.getSession().setAttribute("account", account);
                //密码正确
                result.setCode(HttpStatus.OK).setMessage("success").setData(account.getUsername());
            } else {
                result.setCode(HttpStatus.OK).setMessage("fail").setData("password_wrong");
                // 密码错误
            }
        } else {
            result.setCode(HttpStatus.OK).setMessage("fail").setData("not exist");
        }
        return result;
    }

    // 管理员登录
    @ApiOperation("管理员登录")
    @PostMapping("/admin_login")
    public Result<String> adminLogin(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        Result<String> result = new Result<>();

        if (accountService.isAdminExistByUsername(loginDto.getUsername())) {
            // 类型为user的用户存在

            Account account = accountService.findAdminByUsername(loginDto.getUsername());
            if (account.getPassword().equals(loginDto.getPassword())) {
                request.getSession().setAttribute("admin", account.getUsername());
                request.getSession().setAttribute("account", account);
                //密码正确
                result.setCode(HttpStatus.OK).setMessage("success").setData(account.getUsername());
            } else {
                result.setCode(HttpStatus.OK).setMessage("fail").setData("password_wrong");
                // 密码错误
            }
        } else {
            result.setCode(HttpStatus.OK).setMessage("fail").setData("not exist");
        }
        return result;
    }

    // 注册
    @ApiOperation("注册")
    @PostMapping("/regist")
    public Result<String> regist(@RequestBody RegistDto registDto, HttpServletRequest request) {
        Result<String> result = new Result<>();
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute("code");
        String username = (String) session.getAttribute("username");
        if (registDto.getCode().equals(code) && registDto.getUsername().equals(username)) {
            Account account = new Account();
            account.setName(registDto.getName());
            account.setPassword(registDto.getPassword());
            account.setUsername(registDto.getUsername());
            accountService.add(account);
            result.setCode(HttpStatus.OK).setData("success").setMessage("regist");
        } else {
            Enumeration em = request.getSession().getAttributeNames();
            while (em.hasMoreElements()) {
                request.getSession().removeAttribute(em.nextElement().toString());
            }
            result.setCode(HttpStatus.OK).setData("fail").setMessage("regist");
        }

        return result;
    }

    @ApiOperation("发送注册码")
    @PostMapping("/code")
    public Result<String> registCode(@RequestBody EmailDto emailDto, HttpServletRequest request) throws MessagingException {
        Result<String> result = new Result<>();
        try {
            String code = emailSender.sendRegistCode(emailDto.getUsername());
            request.getSession().setAttribute("code", code);
            request.getSession().setAttribute("username", emailDto.getUsername());
            result.setCode(HttpStatus.OK).setMessage("regist_code").setData("success");
        } catch (Exception e) {
            result.setCode(HttpStatus.OK).setMessage("regist_code").setData("fail");
        }
        return result;
    }


    // 检查名字是否可用
    @ApiOperation("检查名字是否可用")
    @PostMapping("/check_name")
    public Result<String> checkName(@RequestBody RegistDto registDto) {
        Result<String> result = new Result<>();
        Boolean check = accountService.isExistByName(registDto.getName());
        if (check) {
            // 存在这个名字
            result.setCode(HttpStatus.OK).setMessage("check_name").setData("fail");
        } else {
            result.setCode(HttpStatus.OK).setMessage("check_name").setData("success");
        }
        return result;
    }

    // 检查账号是否可用
    @ApiOperation("检查账号是否可用")
    @PostMapping("/check_username")
    public Result<String> chechUsername(@RequestBody RegistDto registDto) {
        Result<String> result = new Result<>();
        Boolean check = accountService.isExistByUsername(registDto.getUsername());
        if (check) {
            // 存在这个名字
            result.setCode(HttpStatus.OK).setMessage("check_name").setData("fail");
        } else {
            result.setCode(HttpStatus.OK).setMessage("check_name").setData("success");
        }
        return result;
    }

    @ApiOperation("获取登录的状态，返回用户")
    @GetMapping("/user_check_login")
    public Result<Object> checkLogin(HttpServletRequest request) {
        Result<Object> result = new Result<>();
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");
        if (username == null) {
            result.setCode(HttpStatus.OK).setMessage("fail").setData("not login");
        } else {
            Account account = accountService.findUserByUsername(username);
            StatusDto statusDto = new StatusDto();
            statusDto.setName(account.getName()).setUsername(account.getUsername()).setId(account.getId());
            result.setCode(HttpStatus.OK).setMessage("success").setData(statusDto);
        }
        return result;
    }

}