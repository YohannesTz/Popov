package com.github.yohannestz.popov.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.yohannestz.popov.data.model.Sms

@Dao
interface MessageLogCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageLogs(messageLogsList: List<Sms>)

    @Query("SELECT * FROM Sms")
    fun getAll(): List<Sms>

    @Query("SELECT * FROM Sms WHERE isUploaded = 0")
    fun getAllUnUploaded(): List<Sms>
}