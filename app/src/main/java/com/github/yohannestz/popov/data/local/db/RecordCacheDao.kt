package com.github.yohannestz.popov.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.yohannestz.popov.data.model.Record

@Dao
interface RecordCacheDao {

    @Query("SELECT * FROM Record")
    suspend fun getAll(): List<Record>

    @Insert
    suspend fun insertRecording(record: Record)

    @Query("SELECT * FROM Record WHERE isUploaded = 0")
    suspend fun getAllUnUploaded():List<Record>

    @Query("UPDATE Record SET isUploaded = 1 WHERE id = :recordId")
    suspend fun markRecordAsUploaded(recordId: Int)
}