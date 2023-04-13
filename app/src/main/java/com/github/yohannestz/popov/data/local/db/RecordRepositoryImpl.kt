package com.github.yohannestz.popov.data.local.db

import com.github.yohannestz.popov.data.model.Record
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordCacheDao: RecordCacheDao
) {
    suspend fun insertRecording(record: Record) {
        recordCacheDao.insertRecording(record)
    }

    suspend fun getAllRecording(): List<Record> {
        return recordCacheDao.getAll()
    }
}