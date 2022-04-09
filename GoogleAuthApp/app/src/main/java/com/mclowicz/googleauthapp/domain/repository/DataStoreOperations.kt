package com.mclowicz.googleauthapp.domain.repository

import kotlinx.coroutines.flow.Flow


interface DataStoreOperations {
    suspend fun savedSignedInState(signedIn: Boolean)
    fun readSignedInState(): Flow<Boolean>
}