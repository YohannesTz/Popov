package com.github.yohannestz.popov.data.remote

import com.github.yohannestz.popov.data.model.UploadResponse
import com.github.yohannestz.popov.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NetworkService {
    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part("image\"filename.3gpp\"") file: RequestBody): Response<UploadResponse>

    @Multipart
    @POST(Constants.PHP_UPLOAD_ROUTE)
    suspend fun uploadFile(@Part file: MultipartBody.Part, @Part("sampleFile") name: RequestBody): Response<UploadResponse>
}