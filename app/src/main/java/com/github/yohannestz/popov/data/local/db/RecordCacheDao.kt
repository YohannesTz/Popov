package com.github.yohannestz.popov.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.yohannestz.popov.data.model.Record

@Dao
interface RecordCacheDao {

    @Query("SELECT * FROM Record")
    suspend fun getAll(): List<Record>

    @Insert
    suspend fun insertRecording(record: Record)
}