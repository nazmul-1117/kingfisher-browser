package com.nazmul.kingfisher.ui.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.nazmul.kingfisher.R;
import com.nazmul.kingfisher.web.BrowserEngine;

public class MainActivity extends AppCompatActivity implements BrowserEngine.BrowserEngineCallback {

    private WebView webView;
    private EditText etAddressBar;
    private ProgressBar progressBar;
    private ImageButton btnBack, btnForward, btnRefresh;
    private BrowserEngine browserEngine;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initBrowserEngine();
        setupListeners();

        browserEngine.loadUrl(null);
    }

    private void initViews() {
        webView = findViewById(R.id.web_view);
        etAddressBar = findViewById(R.id.et_address_bar);
        progressBar = findViewById(R.id.progress_bar);
        btnBack = findViewById(R.id.btn_back);
        btnForward = findViewById(R.id.btn_forward);
        btnRefresh = findViewById(R.id.btn_refresh);
    }

    private void initBrowserEngine() {
        browserEngine = new BrowserEngine(this, webView);
        browserEngine.setCallback(this);
    }

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

    // 📥 Callback Implementations
    @Override public void onUrlChanged(String url) {
        if (!etAddressBar.hasFocus()) {
            etAddressBar.setText(formatUrlForDisplay(url));
        }
    }

    @Override public void onProgressChanged(int progress) {
        progressBar.setProgress(progress);
        progressBar.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
    }

    @Override public void onLoadingStateChanged(boolean loading) {
        this.isLoading = loading;
        // Toggle icon: refresh ↔ stop
        btnRefresh.setImageResource(loading ?
                android.R.drawable.ic_menu_close_clear_cancel :
                android.R.drawable.ic_popup_sync);
    }

    @Override public void onNavStateChanged(boolean canBack, boolean canForward) {
        btnBack.setEnabled(canBack);
        btnBack.setAlpha(canBack ? 1.0f : 0.5f);
        btnForward.setEnabled(canForward);
        btnForward.setAlpha(canForward ? 1.0f : 0.5f);
    }

    // Physical back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && browserEngine.canGoBack()) {
            browserEngine.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (browserEngine != null) browserEngine.destroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAddressBar.getWindowToken(), 0);
    }

    private String formatUrlForDisplay(String url) {
        if (url == null) return "";
        return url.replaceFirst("^https?://", "").replaceFirst("^www\\.", "");
    }
}