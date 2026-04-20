package com.nazmul.kingfisher.ui.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;
import com.nazmul.kingfisher.R;
import com.nazmul.kingfisher.data.BookmarkManager;
import com.nazmul.kingfisher.data.HistoryManager;
import com.nazmul.kingfisher.security.PrivacyManager;
import com.nazmul.kingfisher.web.BrowserEngine;

public class MainActivity extends AppCompatActivity implements BrowserEngine.BrowserEngineCallback {

    private WebView webView;
    private EditText etAddressBar;
    private ProgressBar progressBar;
    private ImageButton btnBack, btnForward, btnRefresh;

    private BrowserEngine browserEngine;
    private HistoryManager historyManager;
    private BookmarkManager bookmarkManager;
    private PrivacyManager privacyManager;
    private Toolbar toolbar;

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // 🔹 STEP 1: Initialize views FIRST (so webView isn't null)
        initViews();


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide app title if desired
        }

        // 🔹 STEP 2: Initialize managers (no dependencies on views)
        privacyManager = new PrivacyManager(this);
        historyManager = new HistoryManager(this);
        bookmarkManager = new BookmarkManager(this);

        // 🔹 STEP 3: Initialize browser engine WITH all dependencies
        browserEngine = new BrowserEngine(this, webView, privacyManager, historyManager);
        browserEngine.setCallback(this);

        // 🔹 STEP 4: Setup UI listeners (now that engine is ready)
        setupListeners();

        // 🔹 STEP 5: Apply initial UI state
        updateIncognitoUI(privacyManager.isIncognito());

        // 🔹 STEP 6: Load home page LAST (everything is ready)
        browserEngine.loadUrl(null);
    }

    /**
     * Bind UI components from layout. Must run before any WebView operations.
     */
    private void initViews() {
        webView = findViewById(R.id.web_view);
        etAddressBar = findViewById(R.id.et_address_bar);
        progressBar = findViewById(R.id.progress_bar);
        btnBack = findViewById(R.id.btn_back);
        btnForward = findViewById(R.id.btn_forward);
        btnRefresh = findViewById(R.id.btn_refresh);
        toolbar = findViewById(R.id.toolbar);
    }

    /**
     * Wire up all user interactions. Called after engine is initialized.
     */
    private void setupListeners() {
        // Address bar "Go" action
        etAddressBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                String input = etAddressBar.getText().toString().trim();
                if (!input.isEmpty()) {
                    browserEngine.loadUrl(input);
                    etAddressBar.clearFocus();
                    hideKeyboard();
                }
                return true;
            }
            return false;
        });

        // Focus behavior: show full URL when editing, clean URL otherwise
        etAddressBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etAddressBar.setText(browserEngine.getCurrentUrl());
                etAddressBar.selectAll();
            } else {
                etAddressBar.setText(formatUrlForDisplay(browserEngine.getCurrentUrl()));
            }
        });

        // Navigation buttons
        btnBack.setOnClickListener(v -> browserEngine.goBack());
        btnForward.setOnClickListener(v -> browserEngine.goForward());
        btnRefresh.setOnClickListener(v -> {
            if (isLoading) browserEngine.stopLoading();
            else browserEngine.reload();
        });
    }

    // 📥 BrowserEngine Callback Implementations
    @Override
    public void onUrlChanged(String url) {
        if (!etAddressBar.hasFocus()) {
            etAddressBar.setText(formatUrlForDisplay(url));
        }
    }

    @Override
    public void onProgressChanged(int progress) {
        progressBar.setProgress(progress);
        progressBar.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onLoadingStateChanged(boolean loading) {
        this.isLoading = loading;
        btnRefresh.setImageResource(loading ?
                android.R.drawable.ic_menu_close_clear_cancel :
                android.R.drawable.ic_popup_sync);
    }

    @Override
    public void onNavStateChanged(boolean canBack, boolean canForward) {
        btnBack.setEnabled(canBack);
        btnBack.setAlpha(canBack ? 1.0f : 0.5f);
        btnForward.setEnabled(canForward);
        btnForward.setAlpha(canForward ? 1.0f : 0.5f);
    }

    // 🔙 Physical back button handling
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && browserEngine.canGoBack()) {
            browserEngine.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 🧹 Cleanup to prevent memory leaks
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (browserEngine != null) browserEngine.destroy();
        if (historyManager != null) historyManager.shutdown();
        if (bookmarkManager != null) bookmarkManager.shutdown();
    }

    // ⌨️ Hide soft keyboard after URL submission
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAddressBar.getWindowToken(), 0);
    }

    // 🌐 Clean URL display: "google.com/search" instead of "https://www.google.com/search"
    private String formatUrlForDisplay(String url) {
        if (url == null) return "";
        return url.replaceFirst("^https?://", "").replaceFirst("^www\\.", "");
    }

    // 🎨 Visual feedback for incognito mode
//    private void updateIncognitoUI(boolean incognito) {
//        findViewById(R.id.card_address_bar).setCardBackgroundColor(
//                incognito ? 0xFF2D2D2D : 0xFFFFFFFF
//        );
//    }

    private void updateIncognitoUI(boolean incognito) {
        MaterialCardView card = findViewById(R.id.card_address_bar);

        if (card != null) {
            int bgColor = incognito ? 0xFF2D2D2D : 0xFFFFFFFF;
            int textColor = incognito ? 0xFFFFFFFF : 0xFF040404;

            etAddressBar.setTextColor(textColor);

            ColorStateList tint = ColorStateList.valueOf(textColor);
            btnForward.setImageTintList(tint);
            btnBack.setImageTintList(tint);
            btnRefresh.setImageTintList(tint);

            card.setCardBackgroundColor(bgColor);
        }
    }

    // 📋 Options menu: privacy toggle + clear history
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu, menu);
        menu.findItem(R.id.menu_privacy).setChecked(privacyManager.isIncognito());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_privacy) {
            boolean newState = !item.isChecked();
            item.setChecked(newState);
            privacyManager.setIncognito(newState);
            updateIncognitoUI(newState);
            return true;
        }
        if (id == R.id.menu_clear_history) {
            historyManager.clearHistory(() -> {
                // Optional: show toast "History cleared"
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}