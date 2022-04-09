package com.mclowicz.googleauthapp.data.remote

import com.mclowicz.googleauthapp.domain.model.ApiRequest
import com.mclowicz.googleauthapp.domain.model.ApiResponse
import com.mclowicz.googleauthapp.domain.model.UserUpdate
import retrofit2.http.*

interface KtorApi {

    @POST("/token_verification")
    suspend fun verifyToken(
        @Body request: ApiRequest
    ): ApiResponse

    @GET("/get_user")
    suspend fun getUserInfo(): ApiResponse

    @PUT("/update_user")
    suspend fun updateUser(
        @Body userUpdate: UserUpdate
    ): ApiResponse

    @DELETE("delete_user")
    suspend fun deleteUser(): ApiResponse

    @GET("sign_out")
    suspend fun clearSession(): ApiResponse
}