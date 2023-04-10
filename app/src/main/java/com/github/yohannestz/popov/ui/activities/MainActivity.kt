package com.github.yohannestz.popov.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.yohannestz.popov.ui.calls.CallScreen
import com.github.yohannestz.popov.ui.theme.PopovTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopovTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val requestedPermissions = rememberMultiplePermissionsState(
                        arrayListOf(
                            android.Manifest.permission.READ_SMS,
                            android.Manifest.permission.READ_CONTACTS,
                            android.Manifest.permission.WRITE_CALL_LOG,
                            android.Manifest.permission.READ_CALL_LOG
                        )
                    )

                    if (requestedPermissions.allPermissionsGranted) {
                        CallScreen()
                    } else {
                        Column(modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Text(text = "Please Grant all permissions!", style = MaterialTheme.typography.h6)
                            Button(
                                onClick = {
                                    requestedPermissions.launchMultiplePermissionRequest()
                                },
                                shape = RoundedCornerShape(22.dp),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = "Grant", style = MaterialTheme.typography.h6)
                            }
                        }
                    }
                }
            }
        }
    }
}
