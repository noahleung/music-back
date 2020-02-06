package com.graduate.musicback.repository;

import com.graduate.musicback.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,String> {

   History findHistoryByAccountIdAndSongsId(String accountId,String songsId);

}
