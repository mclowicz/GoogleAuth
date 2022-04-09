package com.mclowicz.googleauthapp.presentation.screen.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.mclowicz.googleauthapp.R
import com.mclowicz.googleauthapp.component.GoogleButton
import com.mclowicz.googleauthapp.component.MessageBar
import com.mclowicz.googleauthapp.domain.model.ApiResponse
import com.mclowicz.googleauthapp.domain.model.MessageBarState
import com.mclowicz.googleauthapp.util.RequestState

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun ProfileContent(
    apiResponse: RequestState<ApiResponse>,
    messageBarState: MessageBarState,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    emailAddress: String?,
    profilePhoto: String?,
    onSignOutClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            if (apiResponse is RequestState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Blue
                )
            } else {
                MessageBar(messageBarState = messageBarState)
            }
        }
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth(0.7f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CentralContent(
                firstName = firstName,
                onFirstNameChanged = onFirstNameChanged,
                lastName = lastName,
                onLastNameChanged = onLastNameChanged,
                emailAddress = emailAddress,
                profilePhoto = profilePhoto,
                onSignOutClicked = onSignOutClicked
            )
        }
    }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun CentralContent(
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    emailAddress: String?,
    profilePhoto: String?,
    onSignOutClicked: () -> Unit
) {
    val painter = rememberImagePainter(data = profilePhoto) {
        crossfade(1000)
        placeholder(R.drawable.ic_vertical_menu)
    }
    Image(
        modifier = Modifier
            .padding(bottom = 40.dp)
            .size(150.dp)
            .clip(CircleShape),
        painter = painter,
        contentDescription = "Profile Photo"
    )
    OutlinedTextField(
        value = firstName,
        onValueChange = { onFirstNameChanged(it) },
        label = { Text(text = "First Name") },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,
    )
    OutlinedTextField(
        value = lastName,
        onValueChange = { onLastNameChanged(it) },
        label = { Text(text = "Last Name") },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,
    )
    OutlinedTextField(
        value = emailAddress.toString(),
        onValueChange = {  },
        label = { Text(text = "Email Address") },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,
        enabled = false
    )
    GoogleButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        primaryText = "Sign Out",
        secondaryText = "Sign Out",
        onClick = onSignOutClicked
    )
}