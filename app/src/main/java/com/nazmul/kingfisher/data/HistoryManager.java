package com.nazmul.kingfisher.data;

import android.content.Context;
import com.nazmul.kingfisher.data.dao.HistoryDao;
import com.nazmul.kingfisher.data.model.HistoryEntry;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Thread-safe manager for browsing history operations.
 * All database calls run on a background thread to avoid ANR.
 * Uses callbacks to notify UI when operations complete.
 */
public class HistoryManager {
    private final HistoryDao historyDao;
    private final ExecutorService executor;

    public HistoryManager(Context context) {
        // Get DAO from singleton Room database
        this.historyDao = AppDatabase.getInstance(context).historyDao();
        // Single background thread for all DB operations
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Saves a history entry asynchronously.
     * @param url The page URL
     * @param title The page title (or URL if title is null)
     * @param onComplete Optional callback to run after save completes
     */
    public void saveHistory(String url, String title, Runnable onComplete) {
        executor.execute(() -> {
            HistoryEntry entry = new HistoryEntry(url, title, System.currentTimeMillis());
            historyDao.insertHistory(entry);
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    /**
     * Fetches recent history entries asynchronously.
     * @param callback Receives the list of HistoryEntry objects on the calling thread
     */
    public void getRecentHistory(Callback<List<HistoryEntry>> callback) {
        executor.execute(() -> {
            List<HistoryEntry> result = historyDao.getRecentHistory();
            // Post result back to main thread via callback
            if (callback != null) {
                callback.onResult(result);
            }
        });
    }

    /**
     * Clears all history entries asynchronously.
     * @param onComplete Optional callback to run after clear completes
     */
    public void clearHistory(Runnable onComplete) {
        executor.execute(() -> {
            historyDao.clearAll();
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    /**
     * Call this in Activity.onDestroy() to prevent thread leaks.
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Generic callback interface for async operations.
     * Implement this to handle results on the main thread.
     */
    public interface Callback<T> {
        void onResult(T result);
    }
}