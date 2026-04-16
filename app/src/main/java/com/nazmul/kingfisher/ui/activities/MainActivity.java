package com.nazmul.kingfisher.ui.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import com.nazmul.kingfisher.R;
import com.nazmul.kingfisher.web.BrowserEngine;

/**
 * Entry point. Delegates all browser logic to BrowserEngine.
 * Will be extended with AddressBar, Tabs, and Menu in upcoming steps.
 */
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private BrowserEngine browserEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.web_view);
        browserEngine = new BrowserEngine(this, webView);

        // Load home page on first launch
        browserEngine.loadUrl(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Intercept back button for in-browser navigation
        if (keyCode == KeyEvent.KEYCODE_BACK && browserEngine.canGoBack()) {
            browserEngine.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (browserEngine != null) {
            browserEngine.destroy(); // Prevents memory leaks
        }
    }
}