package com.graduate.musicback.repository;

import com.graduate.musicback.dto.album.AlbumDto;
import com.graduate.musicback.dto.search.SingerDto;
import com.graduate.musicback.entity.Album;
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
public interface AlbumRepository extends JpaRepository<Album,String> {

    // 通过关键字查找
    @Query(value = "select new com.graduate.musicback.dto.album.AlbumDto (" +
        "album.id," +
        "album.introduction," +
        "album.picture," +
        "album.name," +
        "singer.name," +
            "singer.id) " +
        "from Album album , Singer singer where album.singerId = singer.id and album.name like %:#{#keywords}% and album.isDel = false")
    Page<AlbumDto> findAllByKeywords(String keywords, Pageable pageable);

    // 查找所有被删除的
    @Query(value = "select new com.graduate.musicback.dto.album.AlbumDto (" +
            "album.id," +
            "album.introduction," +
            "album.picture," +
            "album.name," +
            "singer.name,singer.id) " +
            "from Album album , Singer singer where album.singerId = singer.id and album.name like %:#{#keywords}% and album.isDel = true")
    Page<AlbumDto> findAllDeleted(String keywords, Pageable pageable);

    // 通过歌手id找所有专辑Dto
    @Query(value="select new com.graduate.musicback.dto.album.AlbumDto (" +
            "album.id," +
            "album.introduction," +
            "album.picture," +
            "album.name," +
            "singer.name,singer.id)" +
            " from Album album,Singer singer where album.singerId = singer.id and singer.id = :#{#singerId} and album.isDel = false")
    Page<AlbumDto> findAlbumDtoBySingerId (String singerId,Pageable pageable);

    // 寻找所有没被删除的专辑
    @Query(value = "select new com.graduate.musicback.dto.search.AlbumDto (album.id,album.name) from Album album" +
            " where album.isDel = false")
    List<com.graduate.musicback.dto.search.AlbumDto> findSearchAlbumDto();

    // 级联删除使用
    List<Album> findAlbumBySingerId(String singerId);

    Album findAlbumById(String id);

    @Query(value = "select new com.graduate.musicback.dto.album.AlbumDto (album.id, album.introduction,album.picture, album.name,singer.name,singer.id) " +
            "from Album album , Singer singer where album.singerId = singer.id and album.id = :#{#id}")
    AlbumDto findAlbumDtoById(String id);
    // 从删除状态恢复
    @Transactional
    @Modifying
    @Query(value = "update t_album set is_del = false , update_at = :#{#updateAt} , update_by = :#{#updateBy} where id = :#{#id}", nativeQuery = true)
    void restore(String id, Date updateAt, String updateBy);

    // 根据歌手id恢复所有专辑
    @Transactional
    @Modifying
    @Query(value = "update t_album set is_del = false ,update_at = :#{#updateAt} , update_by = :#{#updateBy} where singer_id = :#{#singerId}",nativeQuery = true)
    void restoreBySingerId(String singerId, Date updateAt, String updateBy);

    // 专辑的软删除
    @Modifying
    @Transactional
    @Query(value = "update t_album set is_del = true  , update_at = :#{#updateAt} , update_by = :#{#updateBy} where id = :#{#id}", nativeQuery = true)
    void delete(String id, Date updateAt, String updateBy);

    // 根据歌手id软删除专辑
    @Modifying
    @Transactional
    @Query(value = "update t_album set is_del = true  , update_at = :#{#updateAt} , update_by = :#{#updateBy} where singer_id = :#{#singerId}", nativeQuery = true)
    void deleteBySingerId(String singerId, Date updateAt, String updateBy);

    @Query(value = "select album.id as albumId,album.introduction,album.picture,album.name as albumName,singer.name as singerName,singer.id as singerId " +
            "from t_album album,t_singer singer " +
            "where album.singer_id = singer.id and album.is_del = false " +
            "group by album.id order by count(*) desc limit 5",nativeQuery = true)
    List<Object[]> findAlbumByRandom();


}
