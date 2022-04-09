package com.mclowicz.googleauthapp.data.repository

import com.mclowicz.googleauthapp.data.remote.KtorApi
import com.mclowicz.googleauthapp.domain.model.ApiRequest
import com.mclowicz.googleauthapp.domain.model.ApiResponse
import com.mclowicz.googleauthapp.domain.model.UserUpdate
import com.mclowicz.googleauthapp.domain.repository.DataStoreOperations
import com.mclowicz.googleauthapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataStore: DataStoreOperations,
    private val ktorApi: KtorApi
) : Repository {

    override suspend fun saveSignedInState(signedIn: Boolean) {
        dataStore.savedSignedInState(signedIn = signedIn)
    }

    override fun readSignedInState(): Flow<Boolean> {
        return dataStore.readSignedInState()
    }

    override suspend fun verifyTokenOnBackend(request: ApiRequest): ApiResponse {
        return try {
            ktorApi.verifyToken(request = request)
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = e
            )
        }
    }

    override suspend fun getUserInfo(): ApiResponse {
        return try {
            ktorApi.getUserInfo()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = e
            )
        }
    }

    override suspend fun updateUser(userUpdate: UserUpdate): ApiResponse {
        return try {
            ktorApi.updateUser(userUpdate = userUpdate)
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = e
            )
        }
    }

    override suspend fun deleteUser(): ApiResponse {
        return try {
            ktorApi.deleteUser()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = e
            )
        }
    }

    override suspend fun clearSession(): ApiResponse {
        return try {
            ktorApi.clearSession()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                error = e
            )
        }
    }
}