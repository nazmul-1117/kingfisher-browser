package com.kingfisher.browser.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.geckoview.GeckoView
import com.kingfisher.browser.browser.engine.GeckoEngine

@Composable
fun GeckoViewContainer(
    engine: GeckoEngine,
    modifier: Modifier = Modifier
) {

    AndroidView(
        factory = { context ->
            GeckoView(context).apply {
                engine.attach(this)
            }
        },
        modifier = modifier
    )
}