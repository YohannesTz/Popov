package com.github.yohannestz.popov.data.local

import android.content.Context
import android.provider.CallLog
import android.util.Log
import com.github.yohannestz.popov.data.model.Call
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class CallRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun readAllCalls(): List<Call> = withContext(Dispatchers.Default) {
        val callList = arrayListOf<Call>()

        val numberCol = CallLog.Calls.NUMBER
        val nameCol = CallLog.Calls.CACHED_NAME
        val dateCol = CallLog.Calls.DATE
        val typeCol = CallLog.Calls.TYPE
        val isCallNewCol = CallLog.Calls.NEW
        val durationCol = CallLog.Calls.DURATION

        val projection = arrayOf(numberCol, nameCol, dateCol, typeCol, isCallNewCol, durationCol)

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection, null, null, null,
        )

        val numberColIndex = cursor!!.getColumnIndex(numberCol)
        val nameColIndex = cursor.getColumnIndex(nameCol)
        val dateColIndex = cursor.getColumnIndex(dateCol)
        val typeColIndex = cursor.getColumnIndex(typeCol)
        val isCallNewColIndex = cursor.getColumnIndex(isCallNewCol)
        val durationColIndex = cursor.getColumnIndex(durationCol)

        while (cursor.moveToNext()) {
            val num = cursor.getString(numberColIndex)
            val name = cursor.getString(nameColIndex)
            val date = cursor.getString(dateColIndex)
            val type = cursor.getString(typeColIndex)
            val isNew = cursor.getString(isCallNewColIndex)
            val duration = cursor.getString(durationColIndex)

            Log.e("details: ", "$num -- $name -- $date -- $type -- $isNew -- $duration")

            val callData = Call(num, name, date, type, isNew, duration)
            callList.add(callData)
        }

        cursor.close()
        return@withContext callList
    }

    suspend fun readCallsForToday(): List<Call> = withContext(Dispatchers.Default) {
        val callListForToday = arrayListOf<Call>()

        val numberCol = CallLog.Calls.NUMBER
        val nameCol = CallLog.Calls.CACHED_NAME
        val dateCol = CallLog.Calls.DATE
        val typeCol = CallLog.Calls.TYPE
        val isCallNewCol = CallLog.Calls.NEW
        val durationCol = CallLog.Calls.DURATION

        val projection = arrayOf(numberCol, nameCol, dateCol, typeCol, isCallNewCol, durationCol)

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val today = calendar.timeInMillis.toString()
        val selection = "${CallLog.Calls.DATE} >= ?"
        val selectionArgs = arrayOf(today)

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection, selection, selectionArgs, null,
        )

        val numberColIndex = cursor!!.getColumnIndex(numberCol)
        val nameColIndex = cursor.getColumnIndex(nameCol)
        val dateColIndex = cursor.getColumnIndex(dateCol)
        val typeColIndex = cursor.getColumnIndex(typeCol)
        val isCallNewColIndex = cursor.getColumnIndex(isCallNewCol)
        val durationColIndex = cursor.getColumnIndex(durationCol)

        while (cursor.moveToNext()) {
            val num = cursor.getString(numberColIndex)
            val name = cursor.getString(nameColIndex)
            val date = cursor.getString(dateColIndex)
            val type = cursor.getString(typeColIndex)
            val isNew = cursor.getString(isCallNewColIndex)
            val duration = cursor.getString(durationColIndex)

            Log.e("details: ", "$num -- $name -- $date -- $type -- $isNew -- $duration")

            val callData = Call(num, name, date, type, isNew, duration)
            callListForToday.add(callData)
        }

        cursor.close()

        return@withContext callListForToday
    }
}