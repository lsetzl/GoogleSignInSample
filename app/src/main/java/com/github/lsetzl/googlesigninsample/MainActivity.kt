package com.github.lsetzl.googlesigninsample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.github.lsetzl.googlesigninsample.ui.theme.GoogleSignInSampleTheme
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleSignInSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        GoogleSignInButton()
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleSignInButton() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(onClick = {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(context.getString(R.string.oauth_web_client_id)) // Define the web client ID.
                .setNonce(context.getString(R.string.nonce)) // Define the nonce.
                .build()
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            val result = credentialManager.getCredential(context, request)
            handleSignIn(result)
            Toast.makeText(context, "You are signed in.", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text(text = "Signin with Google")
    }
}

fun handleSignIn(result: GetCredentialResponse) {
    val credential = result.credential
    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
    val googleIdToken = googleIdTokenCredential.idToken

    Log.i("Main", "googleIdToken = $googleIdToken")
}