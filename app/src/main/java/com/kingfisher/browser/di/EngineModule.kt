package com.kingfisher.browser.di

import android.content.Context
import com.kingfisher.browser.browser.engine.GeckoEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {
    @Provides
    @Singleton
    fun provideGeckoEngine(@ApplicationContext context: Context): GeckoEngine = GeckoEngine(context)
}