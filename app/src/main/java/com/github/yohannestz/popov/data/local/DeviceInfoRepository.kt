package com.github.yohannestz.popov.data.local

import android.os.Build
import com.github.yohannestz.popov.data.model.DeviceInfo

class DeviceInfoRepository {
    fun getDeviceInfo(separator: String): String {
        var mData = ""
        val mBuilder = StringBuilder()
        mBuilder.append("RELEASE " + Build.VERSION.RELEASE + separator)
        mBuilder.append("DEVICE " + Build.DEVICE + separator)
        mBuilder.append("MODEL " + Build.MODEL + separator)
        mBuilder.append("PRODUCT " + Build.PRODUCT + separator)
        mBuilder.append("BRAND " + Build.BRAND + separator)
        mBuilder.append("DISPLAY " + Build.DISPLAY + separator)
        mBuilder.append("CPU_ABI " + Build.SUPPORTED_ABIS[0] + separator)
        mBuilder.append("CPU_ABI2 " + Build.SUPPORTED_ABIS[1] + separator)
        mBuilder.append("UNKNOWN " + Build.UNKNOWN + separator)
        mBuilder.append("HARDWARE " + Build.HARDWARE + separator)
        mBuilder.append("ID " + Build.ID + separator)
        mBuilder.append("MANUFACTURER " + Build.MANUFACTURER + separator)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.append("NOT AVAILABLE")
        } else {
            @Suppress( "DEPRECATION")
            mBuilder.append("SERIAL " + Build.SERIAL)
        }
        mBuilder.append("USER " + Build.USER + separator)
        mBuilder.append("HOST " + Build.HOST + separator)
        mData = mBuilder.toString()
        return mData
    }

    fun getDeviceInformation(): DeviceInfo {
        return DeviceInfo(
            release = Build.VERSION.RELEASE,
            codeName = Build.VERSION.CODENAME,
            device = Build.DEVICE,
            model = Build.MODEL,
            product = Build.PRODUCT,
            brand = Build.BRAND,
            display = Build.DISPLAY,
            cpuAbi = Build.SUPPORTED_ABIS[0].ifEmpty { "NOT AVAILABLE" },
            cpuAbiTwo = Build.SUPPORTED_ABIS[1].ifEmpty { "NOT AVAILABLE" },
            unknown = Build.UNKNOWN,
            hardware = Build.HARDWARE,
            id = Build.ID,
            manufacture = Build.MANUFACTURER,
            serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                "NOT AVAILABLE"
            } else {
                @Suppress( "DEPRECATION")
                Build.SERIAL
            },
            user = Build.USER,
            host = Build.HOST
        )
    }
}