package com.nazmul.kingfisher.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.nazmul.kingfisher.data.model.HistoryEntry;
import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT 100")
    List<HistoryEntry> getRecentHistory();

    @Insert
    void insertHistory(HistoryEntry entry);

    @Query("DELETE FROM history")
    void clearAll();
}