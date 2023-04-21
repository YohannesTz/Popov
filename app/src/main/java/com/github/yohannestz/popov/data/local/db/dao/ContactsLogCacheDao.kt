package com.github.yohannestz.popov.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.yohannestz.popov.data.model.ContactsFileLog

@Dao
interface ContactsLogCacheDao {

    @Query("SELECT * FROM ContactsFileLog")
    suspend fun getAll(): List<ContactsFileLog>

    @Insert
    suspend fun insertContactsFileLog(contactsFileLog: ContactsFileLog)

    @Query("SELECT * FROM ContactsFileLog WHERE isUploaded = 0")
    suspend fun getAllUnUploaded(): List<ContactsFileLog>

    @Query("UPDATE ContactsFileLog SET isUploaded = 1 WHERE id = :logId")
    suspend fun markLogAsUploaded(logId: Int)
}