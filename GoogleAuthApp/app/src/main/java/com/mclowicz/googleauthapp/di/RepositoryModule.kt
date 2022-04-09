package com.mclowicz.googleauthapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mclowicz.googleauthapp.data.remote.KtorApi
import com.mclowicz.googleauthapp.data.repository.DataStoreOperationsImpl
import com.mclowicz.googleauthapp.data.repository.RepositoryImpl
import com.mclowicz.googleauthapp.domain.repository.DataStoreOperations
import com.mclowicz.googleauthapp.domain.repository.Repository
import com.mclowicz.googleauthapp.util.Constants.PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStorePreferences(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreOperations(dataStore: DataStore<Preferences>): DataStoreOperations {
        return DataStoreOperationsImpl(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideRepository(
        dataStoreOperations: DataStoreOperations,
        ktorApi: KtorApi
    ): Repository {
        return RepositoryImpl(
            dataStore = dataStoreOperations,
            ktorApi = ktorApi
        )
    }
}