package com.dmonster.dcash.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val application: Application
) : BaseViewModel() {

    private val permissions = mutableMapOf<String, Boolean>()

    private var permissionType = TYPE_INTRO

    private val _showPermissionPopup = Channel<Boolean>(Channel.CONFLATED)
    val showPermissionPopup = _showPermissionPopup.receiveAsFlow()

    private val _showSettingPopup = Channel<Boolean>(Channel.CONFLATED)
    val showSettingPopup = _showSettingPopup.receiveAsFlow()

    private val _isGrantedPermission = Channel<Boolean>(Channel.CONFLATED)
    val isGrantedPermission = _isGrantedPermission.receiveAsFlow()

    fun checkPermission(type: Int) = viewModelScope.launch {
        permissionType = type

        if (checkGrantedPermission()) {
            _isGrantedPermission.send(true)
        } else {
            onShowPermissionPopUp()
        }
    }

    private fun onShowPermissionPopUp() = viewModelScope.launch {
        _showPermissionPopup.send(true)
    }

    private fun onShowSettingPopUp() = viewModelScope.launch {
        _showSettingPopup.send(true)
    }

    fun onActivityResult() = viewModelScope.launch {
        if (checkGrantedPermission()) {
            _isGrantedPermission.send(true)
        } else {
            onShowSettingPopUp()
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        requestPermissions: Array<out String>,
        grantResults: IntArray
    ) = viewModelScope.launch {
        if (requestCode != REQ_PERMISSION) {
            return@launch
        }

        var grantedCnt = 0

        if (grantResults.isNotEmpty()) {
            grantedCnt = permissions.entries.filterIndexed { i, entry ->
                requestPermissions.contains(entry.key) &&
                        entry.value &&
                        grantResults[i] == PackageManager.PERMISSION_GRANTED
            }.size
        }

        if (grantedCnt == requestPermissions.size - permissions.filterValues { !it }.size) {
            _isGrantedPermission.send(true)
        } else {
            onShowSettingPopUp()
        }
    }

    private fun checkGrantedPermission(): Boolean {
        permissions.clear()

        when (permissionType) {
            TYPE_INTRO -> addIntroPermission()

            TYPE_LOCK_SCREEN -> addLockScreenPermission()

            TYPE_PUSH -> addPushPermission()
        }

        return permissions.filterValues { it }.isEmpty()
    }

    private fun addIntroPermission() {
        val permissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mapOf(
                Manifest.permission.READ_PHONE_STATE to true,
                Manifest.permission.POST_NOTIFICATIONS to false,
            )
        } else {
            mapOf(
                Manifest.permission.READ_PHONE_STATE to true,
            )
        }

        permissionsList.iterator().forEach {
            if (ContextCompat.checkSelfPermission(
                    application,
                    it.key
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions[it.key] = it.value
            }
        }
    }

    private fun addLockScreenPermission() {
        /*val permissionsList = mapOf(
            Manifest.permission.CAMERA to true,
            Manifest.permission.RECORD_AUDIO to true
        )

        permissionsList.iterator().forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it.key
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions[it.key] = it.value
            }
        }*/
    }

    private fun addPushPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    application,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions[Manifest.permission.POST_NOTIFICATIONS] = true
            }
        }
    }

    fun requestPermission(activity: Activity) {
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissions.keys.toTypedArray(),
                REQ_PERMISSION
            )
        }
    }

    // Check ACTION_MANAGE_OVERLAY_PERMISSION
    fun canDrawOverlays(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(application)
        } else true
    }

    companion object {
        const val REQ_PERMISSION = 100
        const val TYPE_INTRO = 0x01
        const val TYPE_LOCK_SCREEN = 0x02
        const val TYPE_PUSH = 0x03
    }
}