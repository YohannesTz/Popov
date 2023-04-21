package com.github.yohannestz.popov.ui.activities

import android.R
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.AlphabeticIndex.Record
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import com.aykuttasil.callrecord.CallRecord
import com.github.yohannestz.popov.services.NotificationService
import com.github.yohannestz.popov.services.PhoneCallStateReceiver
import com.github.yohannestz.popov.services.RecorderService
import com.github.yohannestz.popov.ui.calls.CallScreen
import com.github.yohannestz.popov.ui.notifications.NotificationScreen
import com.github.yohannestz.popov.ui.theme.PopovTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

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
                    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    } else {
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }
                    val requestedPermissions = rememberMultiplePermissionsState(
                        arrayListOf(
                            android.Manifest.permission.READ_SMS,
                            android.Manifest.permission.READ_CONTACTS,
                            android.Manifest.permission.WRITE_CALL_LOG,
                            android.Manifest.permission.READ_CALL_LOG,
                            android.Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        )
                    )

                    if (requestedPermissions.allPermissionsGranted) {
                        CallScreen()
                    } else {
                        requestedPermissions.revokedPermissions.map { permissionState ->
                            Log.e("permission", permissionState.permission)
                        }
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Please Grant all permissions!",
                                style = MaterialTheme.typography.h6
                            )
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

/*        callRecord = CallRecord.Builder(this)
            .setLogEnable(true)
            .setRecordFileName("call_rec_")
            .setRecordDirName("callRec")
            .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
            .setShowSeed(true)
            .build()

        callRecord.startCallReceiver()*/

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        Log.e("asked_before", sharedPref.getBoolean("asked_before", false).toString())
        if (!isNotificationServiceEnabled() && !sharedPref.getBoolean("asked_before", false)) {
            buildNotificationServiceAlertDialog().show()
            with(sharedPref.edit()) {
                putBoolean("asked_before", true)
                apply()
            }
        }

        Log.e("asked_before", sharedPref.getBoolean("asked_before", false).toString())

        if (!NotificationService.isServiceRunning()) {
            val notificationServiceIntent = Intent(this, NotificationService::class.java)
            startService(notificationServiceIntent)
        }

        if (!RecorderService.isRunning()) {
            val recorderIntent = Intent(this, RecorderService::class.java)
            startService(recorderIntent)
        }
    }

    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Notification Listener Service")
        alertDialogBuilder.setMessage("Allow us to read notifications!")
        alertDialogBuilder.setPositiveButton(
            "YES"
        ) { _, _ -> startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        alertDialogBuilder.setNegativeButton(
            "NO"
        ) { _, _ ->
            // If you choose to not enable the notification listener
            // the app. will not work as expected
        }
        return alertDialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun isNotificationServiceEnabled(): Boolean {
        /*val flat: String = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false*/
        return NotificationManagerCompat.getEnabledListenerPackages(applicationContext).contains(packageName)
    }

}
