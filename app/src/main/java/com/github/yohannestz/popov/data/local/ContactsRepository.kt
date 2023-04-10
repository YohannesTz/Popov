package com.github.yohannestz.popov.data.local

import android.content.Context
import android.provider.ContactsContract
import com.github.yohannestz.popov.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val context: Context
) {
    suspend fun getAllContacts(): List<Contact> = withContext(Dispatchers.Default) {
        val contacts = arrayListOf<Contact>()

        val contactIdCol = ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID
        val contactNameCol = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val contactPhoneNumberCol = ContactsContract.CommonDataKinds.Phone.NUMBER

        val projections = arrayOf(contactIdCol, contactNameCol, contactPhoneNumberCol)

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projections, null, null, null
        )

        val contactIdColIndex = cursor!!.getColumnIndex(contactIdCol)
        val contactNameColIndex = cursor.getColumnIndex(contactNameCol)
        val contactPhoneNumberIndex = cursor.getColumnIndex(contactPhoneNumberCol)

        while (cursor.moveToNext()) {
            val id = cursor.getString(contactIdColIndex)
            val name = cursor.getString(contactNameColIndex)
            val phoneNumber = cursor.getString(contactPhoneNumberIndex)

            val contactData = Contact(id, name, phoneNumber)
            contacts.add(contactData)
        }

        cursor.close()
        return@withContext contacts
    }
}