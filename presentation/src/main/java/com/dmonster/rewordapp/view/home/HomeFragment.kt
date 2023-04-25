package com.dmonster.rewordapp.view.home

import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.base.BaseFragment
import com.dmonster.rewordapp.databinding.FragmentHomeBinding
import com.dmonster.rewordapp.service.LockScreenService
import com.dmonster.rewordapp.view.dialog.BasicDialog

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        BasicDialog(requireContext(), binding.root as ViewGroup?)
            .setCancelable(false)
            .setTitle(resources.getString(R.string.set_lock_screen))
            .setText(resources.getString(R.string.set_lock_screen_contents))
            .setCheckBox(true, resources.getString(R.string.not_showing_week))
            .setNegativeButton(true, resources.getString(R.string.cancel))
            .setPositiveButton(true, resources.getString(R.string.set_use)) { view, dialog ->
                dialog?.let {
                    it.dismiss()

                    checkPermission()
                }
            }.show()
    }

    override fun initViewModelCallback() {

    }

    fun checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(requireContext())) {
                val uri = Uri.fromParts("package", PackageInfo().packageName, null)
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
                startActivityForResult(intent, 0)
            } else {
                val intent = Intent(requireContext(), LockScreenService::class.java)
                startForegroundService(intent)
            }
        }
    }
}