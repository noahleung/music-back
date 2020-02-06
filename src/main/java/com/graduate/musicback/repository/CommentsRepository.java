package com.graduate.musicback.repository;

import com.graduate.musicback.dto.comments.CommentsDto;
import com.graduate.musicback.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, String> {

    // 软删除一个评论
    @Modifying
    @Transactional
    @Query(value = "update t_comments set is_del = true where id = :#{#id}", nativeQuery = true)
    void delete(String id);

    Comments findCommentsById(String id);

    @Query(value = "select new com.graduate.musicback.dto.comments.CommentsDto (" +
            "comments.id,account.id, account.name, comments.content, comments.createAt) from " +
            "Comments comments, Account account " +
            "where comments.accountId = account.id and comments.objectId = :#{#objectId} and comments.isDel = false order by comments.createAt desc")
    Page<CommentsDto> findCommentsByObjectId(Pageable pageable, String objectId);

}
