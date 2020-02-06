package com.graduate.musicback.repository;

import com.graduate.musicback.dto.songs.SongsPlayDto;
import com.graduate.musicback.dto.songs.SongsDto;
import com.graduate.musicback.entity.Songs;
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
public interface SongsRepository extends JpaRepository<Songs, String> {


    // 通过关键字查找
    @Query(value = "select new com.graduate.musicback.dto.songs.SongsDto" +
            " (songs.id,songs.name,album.name,singer.name,songs.picture,album.id,singer.id,songs.type) " +
            "from Album album , Singer singer , Songs songs" +
            " where singer.id = album.singerId and songs.albumId = album.id and songs.name like %:#{#keywords}% and songs.isDel = false")
    Page<SongsDto> findAllByKeywords(String keywords, Pageable pageable);


    @Query(value = "select new com.graduate.musicback.dto.songs.SongsDto" +
            " (songs.id,songs.name,album.name,singer.name,songs.picture,album.id,singer.id,songs.type) " +
            "from Album album , Singer singer , Songs songs" +
            " where singer.id = album.singerId and songs.albumId = album.id and singer.id = :#{#singerId} and songs.isDel = false")
    Page<SongsDto> findSongsDtoBySingerId(String singerId,Pageable pageable);
    // 找某歌曲dto
    @Query(value = "select new com.graduate.musicback.dto.songs.SongsDto" +
            " (songs.id,songs.name,album.name,singer.name,songs.picture,album.id,singer.id,songs.type) " +
            "from Album album , Singer singer , Songs songs" +
            " where singer.id = album.singerId and songs.albumId = album.id and songs.id = :#{#id}")
    SongsDto findSongsDtoById(String id);

    // 查找所有被删除的
    @Query(value = "select new com.graduate.musicback.dto.songs.SongsDto" +
            " (songs.id,songs.name,album.name,singer.name,songs.picture,album.id,singer.id,songs.type) " +
            "from Album album , Singer singer , Songs songs" +
            " where singer.id = album.singerId and songs.albumId = album.id and songs.name like %:#{#keywords}% and songs.isDel = true")
    Page<SongsDto> findAllDeleted(String keywords, Pageable pageable);

    // 播放歌曲
    @Query(value = "select new com.graduate.musicback.dto.songs.SongsPlayDto " +
            "(songs.id, songs.name, songs.url, songs.picture, songs.lyrics, singer.name, album.name,songs.type) " +
            "from Album album,Singer singer, Songs songs " +
            "where singer.id = album.singerId and songs.albumId = album.id and songs.id = :#{#id}")
    SongsPlayDto playSongs(String id);

    // 随机推荐10首没被软删除的歌
    @Query(value = "select songs.id, songs.name, album.name as albumName,singer.name as singerName,songs.picture,album.id as albumId,singer.id as singerId,songs.type as type " +
          "from t_album album , t_singer singer , t_songs songs" +
          " where singer.id = album.singer_id and songs.album_id = album.id and songs.is_del = false order by rand() limit 10", nativeQuery = true)
    List<Object[]> find_songs_by_random();

    @Query(value = "select songs.id,songs.name,album.name as albumName,singer.name as singerName,songs.picture,album.id as albumId,singer.id as singerId,songs.type as type " +
            "from t_songs songs,t_album album,t_history history,t_singer singer " +
            "where history.create_at between :#{#oldDate} and :#{#newDate} " +
            "and history.songs_id = songs.id and songs.album_id = album.id and album.singer_id = singer.id " +
            "group by songs.id order by count(*) desc limit 10",nativeQuery = true)
    List<Object[]> find_top_ten_songs(Date oldDate, Date newDate);

    // 找属于某张专辑的所有歌曲
    List<Songs> findSongsByAlbumId(String albumId);

    Songs findSongsById(String id);

    // 从删除状态恢复
    @Transactional
    @Modifying
    @Query(value = "update t_songs set is_del = false , update_at = :#{#updateAt} , update_by = :#{#updateBy} where id = :#{#id}", nativeQuery = true)
    void restore(String id, Date updateAt, String updateBy);

    // 根据专辑id恢复
    @Transactional
    @Modifying
    @Query(value = "update t_songs set is_del = false , update_at = :#{#updateAt} , update_by = :#{#updateBy} where album_id = :#{#albumId}", nativeQuery = true)
    void restoreByAlbumId(String albumId, Date updateAt, String updateBy);

    // 歌曲的软删除
    @Modifying
    @Transactional
    @Query(value = "update t_songs set is_del = true  , update_at = :#{#updateAt} , update_by = :#{#updateBy} where id = :#{#id}", nativeQuery = true)
    void delete(String id, Date updateAt, String updateBy);

    // 根据专辑id软删除所有歌曲
    @Modifying
    @Transactional
    @Query(value = "update t_songs set is_del = true  , update_at = :#{#updateAt} , update_by = :#{#updateBy} where album_id = :#{#albumId}", nativeQuery = true)
    void deleteByAlbumId(String albumId, Date updateAt, String updateBy);



}
