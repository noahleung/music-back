package com.graduate.musicback.repository;

import com.graduate.musicback.dto.search.SingerDto;
import com.graduate.musicback.entity.Singer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public interface SingerRepository extends JpaRepository<Singer, String> {

    // 通过关键字查找
    @Query(value = "select * from t_singer where name like %:#{#keywords}% and is_del = false", nativeQuery = true)
    Page<Singer> findAllByKeywords(String keywords, Pageable pageable);

    // 查找所有被删除的
    @Query(value = "select * from t_singer where name like %:#{#keywords}% and is_del = true", nativeQuery = true)
    Page<Singer> findAllDeleted(String keywords, Pageable pageable);

    Singer findSingerById(String id);

    @Query(value = "select new com.graduate.musicback.dto.search.SingerDto (singer.id,singer.name) from Singer singer" +
            " where singer.isDel = false")
    List<SingerDto> findSearchSingerDto();

    // 从删除状态恢复
    @Transactional
    @Modifying
    @Query(value = "update t_singer set is_del = false , update_at = :#{#updateAt} , update_by = :#{#updateBy} where id = :#{#id}", nativeQuery = true)
    void restore(String id, Date updateAt, String updateBy);

    // 歌手的软删除
    @Modifying
    @Transactional
    @Query(value = "update t_singer set is_del = true , update_at = :#{#updateAt} , update_by = :#{#updateBy} where id = :#{#id}", nativeQuery = true)
    void delete(String id, Date updateAt, String updateBy);




}
