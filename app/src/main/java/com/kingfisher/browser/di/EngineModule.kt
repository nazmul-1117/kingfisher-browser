package com.kingfisher.browser.di

import android.content.Context
import com.kingfisher.browser.browser.engine.GeckoEngine
import com.kingfisher.browser.browser.privacy.PrivacyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {

    @Provides
    @Singleton
    fun provideGeckoEngine(
        @ApplicationContext context: Context,
        privacyManager: PrivacyManager
    ): GeckoEngine {
        return GeckoEngine(context, privacyManager)
    }
}