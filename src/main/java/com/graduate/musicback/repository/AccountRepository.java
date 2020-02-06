package com.graduate.musicback.repository;

import com.graduate.musicback.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface AccountRepository extends JpaRepository<Account,String> {

    @Query(value = "select * from t_account where username = :#{#username} and type = 'user' limit 1",nativeQuery = true)
    Account findUserByUsername (String username);

    @Query(value = "select * from t_account where username = :#{#username} and type = 'admin' limit 1",nativeQuery = true)
    Account findAdminByUsername (String username);

    // 通过账号查询账号是否可用
    Account findAccountByUsername(String username);

    // 检查名称是否可用
    Account findAccountByName(String name);

    // 通过id查找账号
    Account findAccountById(String id);

    // 查找所有被软删除的账号
    @Query(value = "select * from t_account where is_del = true",nativeQuery = true)
    Page<Account> findAllDeletedAccount(Pageable pageable);

    // 软删除一个账号
    @Modifying
    @Transactional
    @Query(value = "update t_account set is_del = true where id = :#{#id}",nativeQuery = true)
    void deleteAccountById(String id);




}
