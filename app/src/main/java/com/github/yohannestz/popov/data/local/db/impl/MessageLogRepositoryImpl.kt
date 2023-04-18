package com.github.yohannestz.popov.data.local.db.impl

import com.github.yohannestz.popov.data.local.db.dao.MessageLogCacheDao
import javax.inject.Inject
import com.github.yohannestz.popov.data.model.Sms

class MessageLogRepositoryImpl @Inject constructor(
    private val messageLogCacheDao: MessageLogCacheDao
) {

    suspend fun insertMessageLogs(messageLogsList: List<Sms>) {
        messageLogCacheDao.insertMessageLogs(messageLogsList)
    }

    suspend fun getAll(): List<Sms> {
        return messageLogCacheDao.getAll()
    }

    suspend fun getAllUnUploaded(): List<Sms> {
        return messageLogCacheDao.getAllUnUploaded()
    }

    suspend fun updateUploaded(updateIds: List<Int>) {
        messageLogCacheDao.updateUploaded(updateIds)
    }
}