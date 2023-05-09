package com.dmonster.dcash.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaDrm
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.dmonster.dcash.BuildConfig
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale
import java.util.UUID
import kotlin.experimental.and

class AppInfo(
    private val context: Context
) {

    val OS = "android"

    private var userAgent: String? = null

    fun getVersionName(): String {
        return try {
            context.packageManager.getPackageInfo(
                context.packageName, 0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("PackageManager.NameNotFoundException", "${e.message}")
            ""
        }
    }

    fun getOriginApplicationId(): String {
        val applicationIdSplit = BuildConfig.APPLICATION_ID.split('.').toMutableList()
        if (applicationIdSplit.last() == "debug") {
            applicationIdSplit.removeAt(applicationIdSplit.lastIndex)
        }

        return applicationIdSplit.joinToString(".")
    }

    fun getWebViewUserAgent(): String {
        userAgent?.let {
            return it
        } ?: kotlin.run {
            userAgent = String.format(
                Locale.US,
                "%s %s",
                WebView(context).settings.userAgentString,
                appendUserAgent()
            )

            return userAgent!!
        }
    }

    fun getUserAgent(): String = String.format(
        Locale.US,
        "%s/%s (Android %s; %s; %s %s; %s) %s %s",
        BuildConfig.APPLICATION_ID,
        BuildConfig.VERSION_NAME,
        Build.VERSION.RELEASE,
        Build.MODEL,
        Build.BRAND,
        Build.DEVICE,
        Locale.getDefault().language,
        appendUserAgent(),
        macAddress
    )

    private fun appendUserAgent(): String =
        "[AndroidApp;${getOriginApplicationId()}/${getVersionName()};${BuildConfig.STORE_TYPE};"

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        var deviceId: String? = null
        if (Build.VERSION.SDK_INT >= 29) {
            val wideVineUuid =
                UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
            try {
                val wvDrm = MediaDrm(wideVineUuid)
                val wideVineId =
                    wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
                deviceId = Base64.encodeToString(
                    wideVineId,
                    Base64.NO_PADDING
                )
            } catch (e: Exception) {
                Log.e("UUID", "${e.message}")
            }
        } else {
            try {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val manager =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    deviceId = if (Build.VERSION.SDK_INT >= 26) {
                        manager.imei
                    } else {
                        @Suppress("DEPRECATION")
                        manager.deviceId
                    }
                }
            } catch (e: SecurityException) {
                Log.e("SecurityException", "${e.message}")
            }
        }

        // ANDROID_ID (HARDWARE_ID)
        if (deviceId.isNullOrEmpty()) {
            deviceId = Settings.Secure.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
        }

        return Base64.encodeToString(deviceId?.toByteArray(), Base64.NO_PADDING).run {
            try {
                substring(0, length - 1)
            } catch (e: StringIndexOutOfBoundsException) {
                return "android_device"
            }
        }
    }

    private val macAddress: String
        get() {
            try {
                val all: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                    val macBytes = nif.hardwareAddress ?: return ""
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(Integer.toHexString((b and 0xFF.toByte()).toInt()) + ":")
                    }
                    if (res1.isNotEmpty()) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            } catch (ex: Exception) { //handle exception
            }
            return ""
        }
}