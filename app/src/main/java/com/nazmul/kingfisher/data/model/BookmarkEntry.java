package com.nazmul.kingfisher.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class BookmarkEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String url;
    public String title;
    public long timestamp;

    public BookmarkEntry(String url, String title, long timestamp) {
        this.url = url;
        this.title = title;
        this.timestamp = timestamp;
    }
}