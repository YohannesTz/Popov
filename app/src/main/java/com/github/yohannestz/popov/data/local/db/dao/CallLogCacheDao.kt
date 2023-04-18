package com.github.yohannestz.popov.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.yohannestz.popov.data.model.Call

@Dao
interface CallLogCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLogs(callLogList: List<Call>)

    @Query("SELECT * FROM Call")
    suspend fun getAll(): List<Call>

    @Query("SELECT * FROM Call WHERE isUploaded = 0")
    suspend fun getAllUnUploaded(): List<Call>

    @Query("UPDATE Call SET isUploaded = 1 WHERE id IN (:updateIds)")
    suspend fun updateUploaded(updateIds: List<Int>)
}