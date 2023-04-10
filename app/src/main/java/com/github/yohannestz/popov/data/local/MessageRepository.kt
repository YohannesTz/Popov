package com.github.yohannestz.popov.data.local

import android.content.Context
import android.provider.Telephony
import com.github.yohannestz.popov.data.model.Sms
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MessageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun readAllSms(): List<Sms> {
        val smsList = arrayListOf<Sms>()
        val number = Telephony.TextBasedSmsColumns.ADDRESS
        val textBody = Telephony.TextBasedSmsColumns.BODY
        val type = Telephony.TextBasedSmsColumns.TYPE
        val date = Telephony.TextBasedSmsColumns.DATE

        val projection = arrayOf(number, textBody, type, date)

        val cursor = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val numberColumnIndex = cursor!!.getColumnIndex(number)
        val textBodyColumnIndex = cursor.getColumnIndex(textBody)
        val typeColumnIndex = cursor.getColumnIndex(type)
        val dateColumnIndex = cursor.getColumnIndex(date)

        while (cursor.moveToNext()) {
            val num = cursor.getString(numberColumnIndex)
            val text = cursor.getString(textBodyColumnIndex)
            val textType = cursor.getString(typeColumnIndex)
            val textDate = cursor.getString(dateColumnIndex)

            val textData = Sms(num, text, textType, textDate)
            smsList.add(textData)
        }

        cursor.close()
        return smsList
    }
}