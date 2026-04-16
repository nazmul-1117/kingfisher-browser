package com.nazmul.kingfisher.data;

import android.content.Context;
import com.nazmul.kingfisher.data.dao.BookmarkDao;
import com.nazmul.kingfisher.data.model.BookmarkEntry;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookmarkManager {
    private final BookmarkDao bookmarkDao;
    private final ExecutorService executor;

    public BookmarkManager(Context context) {
        this.bookmarkDao = AppDatabase.getInstance(context).bookmarkDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void addBookmark(String url, String title) {
        executor.execute(() -> bookmarkDao.insertBookmark(
                new BookmarkEntry(url, title, System.currentTimeMillis())));
    }

    public void removeBookmark(BookmarkEntry entry) {
        executor.execute(() -> bookmarkDao.deleteBookmark(entry));
    }

    public void getAllBookmarks(Callback<List<BookmarkEntry>> callback) {
        executor.execute(() -> callback.onResult(bookmarkDao.getAllBookmarks()));
    }

    public void shutdown() {
        executor.shutdown();
    }

    public interface Callback<T> {
        void onResult(T result);
    }
}