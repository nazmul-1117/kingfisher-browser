package com.kingfisher.browser

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KingfisherApplication : Application() {
    // This acts as the root dependency injection container for your browser
}