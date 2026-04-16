package com.nazmul.kingfisher.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.nazmul.kingfisher.data.dao.BookmarkDao;
import com.nazmul.kingfisher.data.dao.HistoryDao;
import com.nazmul.kingfisher.data.model.BookmarkEntry;
import com.nazmul.kingfisher.data.model.HistoryEntry;

@Database(
        entities = {HistoryEntry.class, BookmarkEntry.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract HistoryDao historyDao();
    public abstract BookmarkDao bookmarkDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "kingfisher_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}