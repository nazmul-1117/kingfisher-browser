package com.nazmul.kingfisher.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.nazmul.kingfisher.data.model.BookmarkEntry;
import java.util.List;

@Dao
public interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    List<BookmarkEntry> getAllBookmarks();

    @Insert
    void insertBookmark(BookmarkEntry entry);

    @Delete
    void deleteBookmark(BookmarkEntry entry);
}