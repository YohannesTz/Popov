package com.github.yohannestz.popov.data.local.db.impl

import com.github.yohannestz.popov.data.local.db.dao.CallLogCacheDao
import com.github.yohannestz.popov.data.model.Call
import javax.inject.Inject

class CallLogRepositoryImpl @Inject constructor(
    private val callLogCacheDao: CallLogCacheDao
) {

    suspend fun insertCallLogs(callLogList: List<Call>) {
        callLogCacheDao.insertCallLogs(callLogList)
    }

    suspend fun getAll(): List<Call> {
        return callLogCacheDao.getAll()
    }

    suspend fun getAllUnUploaded(): List<Call> {
        return callLogCacheDao.getAllUnUploaded()
    }

    suspend fun updateUploaded(updateIds: List<Int>) {
        callLogCacheDao.updateUploaded(updateIds)
    }
}