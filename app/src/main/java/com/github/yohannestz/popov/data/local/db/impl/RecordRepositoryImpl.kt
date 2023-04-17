package com.github.yohannestz.popov.data.local.db.impl

import com.github.yohannestz.popov.data.local.db.dao.RecordCacheDao
import com.github.yohannestz.popov.data.model.Record
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordCacheDao: RecordCacheDao
){
    suspend fun insertRecording(record: Record) {
        recordCacheDao.insertRecording(record)
    }

    suspend fun getAllRecording(): List<Record> {
        return recordCacheDao.getAll()
    }

    suspend fun getAllUnUploaded(): List<Record> {
        return recordCacheDao.getAllUnUploaded()
    }

    suspend fun markAsUploaded(id: Int) {
        recordCacheDao.markRecordAsUploaded(id)
    }
}