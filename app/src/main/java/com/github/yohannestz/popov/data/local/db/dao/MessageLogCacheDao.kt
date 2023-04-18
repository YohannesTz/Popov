package com.github.yohannestz.popov.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.yohannestz.popov.data.model.Sms

@Dao
interface MessageLogCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageLogs(messageLogsList: List<Sms>)

    @Query("SELECT * FROM Sms")
    suspend fun getAll(): List<Sms>

    @Query("SELECT * FROM Sms WHERE isUploaded = 0")
    suspend fun getAllUnUploaded(): List<Sms>

    @Query("UPDATE Sms SET isUploaded = 1 WHERE id IN (:updateIds)")
    suspend fun updateUploaded(updateIds: List<Int>)
}