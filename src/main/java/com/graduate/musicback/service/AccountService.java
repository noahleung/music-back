package com.graduate.musicback.service;

import com.graduate.musicback.dto.LoginDto;
import com.graduate.musicback.entity.Account;
import com.graduate.musicback.repository.AccountRepository;
import com.graduate.musicback.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class AccountService {

    @Autowired
    private HttpSession session;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private AccountRepository accountRepository;

    public Account findUserByUsername(String username) {

        return accountRepository.findUserByUsername(username);
    }

    public Account findAdminByUsername (String username) {

        return accountRepository.findAdminByUsername(username);
    }

    public Account findAccountByUsername(String username) {

        return accountRepository.findAccountByUsername(username);
    }

    public Account findAccountById (String id)
    {
        return accountRepository.findAccountById(id);
    }

    public void add(Account account) {
        account.setId(snowflakeIdWorker.nextId());
        account.setCreateAt(new Date());
        account.setUpdateAt(new Date());
        account.setCreateBy("admin");
        account.setUpdateBy("admin");
        account.setType("user");
        account.setIsDel(false);
        accountRepository.save(account);
    }

    // 更改名称
    public void updateName(String name) {
        Account account = (Account) session.getAttribute("account");
        Account account1 = accountRepository.findAccountById(account.getId());
        account1.setName(name);
        account1.setUpdateAt(new Date());
        account1.setUpdateBy(((Account) session.getAttribute("account")).getId());
        System.out.println("name2:"+account1.getName());
        accountRepository.saveAndFlush(account1);
    }

    public void updatePassword(String accountId,String afterPassword){
        Account account = accountRepository.findAccountById(accountId);
        account.setPassword(afterPassword);
        account.setUpdateAt(new Date());
        account.setUpdateBy(account.getId());
        accountRepository.saveAndFlush(account);
    }

    // 登录判断该用户是否存在
    public boolean isUserExistByUsername (String username) {
        Account account =accountRepository.findUserByUsername(username);
        if(account == null) {
            return false;
        }else
        {
            return  true;
        }
    }

    // 登录判断该管理员是否存在
    public boolean isAdminExistByUsername (String username) {
        Account account =accountRepository.findAdminByUsername(username);
        if(account == null) {
            return false;
        }else
        {
            return  true;
        }
    }

    // 检查注册时账号是否可用
    public boolean isExistByUsername (String username) {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            // 不存在
            return false;
        } else {
            // 存在
            return true;
        }
    }

    // 检查注册时名字是否可用
    public boolean isExistByName (String name) {
        Account account = accountRepository.findAccountByName(name);
        if (account == null) {
            // 不存在
            return false;
        } else {
            // 存在
            return true;
        }
    }

}
