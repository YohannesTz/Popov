package com.github.yohannestz.popov.data.local.db.impl

import com.github.yohannestz.popov.data.local.db.dao.ContactsLogCacheDao
import com.github.yohannestz.popov.data.model.ContactsFileLog
import javax.inject.Inject

class ContactsFileLogRepositoryImpl @Inject constructor(
    private val contactsFileLogCacheDao: ContactsLogCacheDao
) {

    suspend fun getAll(): List<ContactsFileLog> {
        return contactsFileLogCacheDao.getAll()
    }

    suspend fun insertContactsFileLog(contactsFileLog: ContactsFileLog) {
        contactsFileLogCacheDao.insertContactsFileLog(contactsFileLog)
    }

    suspend fun getAllUnUploaded(): List<ContactsFileLog> {
        return contactsFileLogCacheDao.getAllUnUploaded()
    }

    suspend fun markLogAsUnUploaded(logId: Int) {
        contactsFileLogCacheDao.markLogAsUploaded(logId)
    }
}