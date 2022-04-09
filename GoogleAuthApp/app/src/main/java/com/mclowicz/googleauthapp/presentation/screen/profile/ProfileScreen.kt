package com.mclowicz.googleauthapp.presentation.screen.profile

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.android.gms.auth.api.identity.Identity
import com.mclowicz.googleauthapp.domain.model.ApiRequest
import com.mclowicz.googleauthapp.domain.model.ApiResponse
import com.mclowicz.googleauthapp.navigation.Screen
import com.mclowicz.googleauthapp.presentation.screen.common.StartActivityForResult
import com.mclowicz.googleauthapp.presentation.screen.common.signIn
import com.mclowicz.googleauthapp.util.RequestState
import retrofit2.HttpException

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val apiResponse by profileViewModel.apiResponse
    val clearSessionResponse by profileViewModel.clearSessionResponse
    val messageBarState by profileViewModel.messageBarState

    val user by profileViewModel.user
    val firstName by profileViewModel.firstName
    val lastName by profileViewModel.lastName

    Scaffold(
        topBar = {
            ProfileTopBar(
                onSave = {
                    profileViewModel.updateUserInfo()
                },
                onDeleteAllConfirmed = {
                    profileViewModel.deleteUser()
                }
            )
        },
        content = {
            ProfileContent(
                apiResponse = apiResponse,
                messageBarState = messageBarState,
                firstName = firstName,
                onFirstNameChanged = {
                    profileViewModel.updateFirstName(it)
                },
                lastName = lastName,
                onLastNameChanged = {
                    profileViewModel.updateLastName(it)
                },
                emailAddress = user?.emailAddress,
                profilePhoto = user?.profilePhoto,
                onSignOutClicked = {
                    profileViewModel.clearSession()
                }
            )
        }
    )

    val activity = LocalContext.current as Activity

    StartActivityForResult(
        key = apiResponse,
        onResultReceived = { tokenId ->
            profileViewModel.verifyTokenOnBackend(request = ApiRequest(tokenId = tokenId))
        },
        onDialogDismissed = {
            profileViewModel.saveSignedInState(signedIn = false)
            navigateToLoginScreen(navController = navController)
        },
        launcher =  { activityLauncher ->
            if (apiResponse is RequestState.Success) {
                val response = (apiResponse as RequestState.Success<ApiResponse>).data
                if (response.error is HttpException && response.error.code() == 401) {
                    signIn(
                        activity = activity,
                        accountNotFound = {
                            profileViewModel.saveSignedInState(signedIn = false)
                            navigateToLoginScreen(navController = navController)
                        },
                        launchActivityResult = {
                            activityLauncher.launch(it)
                        }
                    )
                }
            }
        }
    )

    LaunchedEffect(key1 = clearSessionResponse, block = {
        if (clearSessionResponse is RequestState.Success &&
            (clearSessionResponse as RequestState.Success<ApiResponse>).data.success
        ) {
            val onTapClient = Identity.getSignInClient(activity)
            onTapClient.signOut()
            profileViewModel.saveSignedInState(signedIn = false)
            navigateToLoginScreen(navController = navController)
        }
    })
}

private fun navigateToLoginScreen(
    navController: NavHostController
) {
    navController.navigate(route = Screen.Login.route) {
        popUpTo(route = Screen.Profile.route) {
            inclusive = true
        }
    }
}