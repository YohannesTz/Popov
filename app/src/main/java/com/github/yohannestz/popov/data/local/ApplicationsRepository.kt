package com.github.yohannestz.popov.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import com.github.yohannestz.popov.data.model.App
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApplicationsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @SuppressLint("QueryPermissionsNeeded")
    suspend fun getAllApps(): List<App> = withContext(Dispatchers.Default) {
        val appsList = arrayListOf<App>()
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (packageInfo in packages) {
            appsList.add(App(packageInfo.loadLabel(packageManager) as String, packageInfo.packageName))
        }

        return@withContext appsList
    }
}