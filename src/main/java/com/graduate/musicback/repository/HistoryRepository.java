package com.graduate.musicback.repository;

import com.graduate.musicback.dto.history.HistorySongsDto;
import com.graduate.musicback.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,String> {

   History findHistoryByAccountIdAndSongsId(Long accountId,Long songsId);

   List<History> findHistoryBySongsId(Long songsId);

   @Query(value = "select new com.graduate.musicback.dto.history.HistorySongsDto" +
           "(songs.type, history.times ) " +
           "from History history, Songs songs ")
   List<HistorySongsDto> findSongsCharts();
}
