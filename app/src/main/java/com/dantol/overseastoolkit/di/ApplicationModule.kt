package com.dantol.overseastoolkit.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.dantol.overseastoolkit.AppDatabase
import com.dantol.overseastoolkit.OverseasApplication.Companion.SHARED_PREFERENCE_KEY
import com.dantol.overseastoolkit.main.currency.database.ExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(
	SingletonComponent::class
)
object ApplicationModule {

	//Android framework
	@Provides
	fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
		return context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
	}

	//Library utils
	@Provides
	@Singleton
	fun provideOkHttpClient() = OkHttpClient()

	//Room Database
	@Provides
	@Singleton
	fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
		return Room.databaseBuilder(context, AppDatabase::class.java, "trophies")
			.fallbackToDestructiveMigration()
			.build()
	}

	@Provides
	fun provideExchangeRateDao(appDatabase: AppDatabase): ExchangeRateDao = appDatabase.exchangeRateDao
}