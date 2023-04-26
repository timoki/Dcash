package com.dmonster.rewordapp.view.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.dmonster.domain.type.NavigateType
import com.dmonster.domain.type.NetworkState
import com.dmonster.domain.type.TopMenuType
import com.dmonster.rewordapp.NavigationDirections
import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.databinding.ActivityMainBinding
import com.dmonster.rewordapp.utils.*
import com.dmonster.rewordapp.view.dialog.BasicDialog
import com.dmonster.rewordapp.view.lockscreen.set.LockScreenPermissionFragment
import com.dmonster.rewordapp.view.network.NetworkViewModel
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private fun checkViewLifeCycleOwnerResumed() = lifecycle.currentState == Lifecycle.State.RESUMED

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val viewModel: MainViewModel by viewModels()

    private val networkViewModel: NetworkViewModel by viewModels()

    private val permissionViewModel: PermissionViewModel by viewModels()

    private val topViewModel: TopViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    private val currentNavigationFragment: Fragment?
        get() = navHostFragment.childFragmentManager.fragments.first()

    private var backKeyPressedTime: Long = 0

    private val overLayPermission: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (permissionViewModel.canDrawOverlays()) {
                //여기서 서비스 실행
                if (currentNavigationFragment == LockScreenPermissionFragment::class) {
                    navController.popBackStack()
                }

                return@registerForActivityResult
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        initViewModelCallback()
        initNetworkViewModelCallback()
        initPermissionViewModelCallback()
        initTopViewModelCallback()
    }

    private fun init() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.topView.viewModel = topViewModel

        navController.addOnDestinationChangedListener(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.previousBackStackEntry == null || !navController.popBackStack()) {
                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                        backKeyPressedTime = System.currentTimeMillis()
                        showSnackBar(
                            this@MainActivity, getString(R.string.main_back_pressed)
                        )
                    } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                        finish()
                    }
                }
            }
        })

        networkViewModel.register(checkNetworkState = true)
    }

    private fun initViewModelCallback() = with(viewModel) {
        isLoading.observeOnLifecycleDestroy(this@MainActivity) { show ->
            if (show) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }

        navigateToChannel.observeOnLifecycleStop(this@MainActivity) { pair ->
            pair?.let {
                navigateToCompose(
                    when (it.first) {
                        is NavigateType.Login -> {
                            NavigationDirections.actionGlobalLoginFragment()
                        }

                        is NavigateType.Home -> {
                            NavigationDirections.actionGlobalHomeFragment()
                        }

                        is NavigateType.News -> {
                            NavigationDirections.actionGlobalNewsFragment()
                        }

                        is NavigateType.Event -> {
                            NavigationDirections.actionGlobalEventFragment()
                        }

                        is NavigateType.Point -> {
                            NavigationDirections.actionGlobalPointFragment()
                        }

                        is NavigateType.MyPage -> {
                            NavigationDirections.actionGlobalMyPageFragment()
                        }

                        is NavigateType.LockScreenPermission -> {
                            NavigationDirections.actionGlobalLockScreenPermissionFragment()
                        }
                    }, it.second
                )
            }
        }

        setOverlayPermissionChannel.onEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this@MainActivity.packageName)
                )

                overLayPermission.launch(intent)
            }
        }.observeInLifecycleStop(this@MainActivity)

        checkPermissionChannel.onEach {
            if (!permissionViewModel.canDrawOverlays()) {
                BasicDialog(this@MainActivity, binding.root as ViewGroup?)
                    .setCancelable(false)
                    .setTitle(resources.getString(R.string.set_lock_screen))
                    .setText(String.format(resources.getString(R.string.set_lock_screen_contents), resources.getString(
                        R.string.app_name)))
                    .setCheckBox(true, resources.getString(R.string.not_showing_week))
                    .setNegativeButton(true, resources.getString(R.string.cancel))
                    .setPositiveButton(true, resources.getString(R.string.set_use)) { view, dialog ->
                        dialog?.let {
                            it.dismiss()

                            if (!permissionViewModel.canDrawOverlays()) {
                                fragmentNavigateTo(NavigateType.LockScreenPermission(), false)
                            }
                        }
                    }.show()
            }
        }.observeInLifecycleStop(this@MainActivity)
    }

    private fun initNetworkViewModelCallback() = with(networkViewModel) {
        currentNetworkState.observeOnLifecycleDestroy(this@MainActivity) {
            if (checkViewLifeCycleOwnerResumed()) {
                when (it) {
                    NetworkState.CONNECT_NETWORK -> {}
                    else -> {
                        navController.navigate(
                            NavigationDirections.actionGlobalNetworkFragment(it)
                        )
                    }
                }
            }
        }
    }

    private fun initPermissionViewModelCallback() = with(permissionViewModel) {

    }

    private fun initTopViewModelCallback() = with(topViewModel) {
        onBackClickChannel.onEach {
            navController.popBackStack()
        }.observeInLifecycleStop(this@MainActivity)

        onRightMenuClickChannel.onEach {

        }.observeInLifecycleStop(this@MainActivity)

        onRightMenuSubClickChannel.onEach {

        }.observeInLifecycleStop(this@MainActivity)
    }

    override fun onDestinationChanged(
        controller: NavController, destination: NavDestination, arguments: Bundle?
    ) {
        viewModel.apply {
            isTopViewVisible.value = true
            isBottomAppBarVisible.value = true

            when (destination.id) {
                // 탑, 바텀 없음
                R.id.introFragment, R.id.loginFragment -> {
                    isTopViewVisible.value = false
                    isBottomAppBarVisible.value = false
                }

                // 루트
                R.id.homeFragment, R.id.newsFragment, R.id.pointFragment, R.id.myPageFragment -> {
                    isTopViewVisible.value = true
                    isBottomAppBarVisible.value = true

                    topViewModel.setTopMenu(
                        leftMenu = TopMenuType.LeftMenu.LOGO,
                        middleMenu = TopMenuType.MiddleMenu.NONE,
                        rightMenu = TopMenuType.RightMenu.NOTIFICATION
                    )
                }

                // 루트(+출석)
                R.id.eventFragment -> {
                    isTopViewVisible.value = true
                    isBottomAppBarVisible.value = true

                    topViewModel.setTopMenu(
                        leftMenu = TopMenuType.LeftMenu.LOGO,
                        middleMenu = TopMenuType.MiddleMenu.NONE,
                        rightMenu = TopMenuType.RightMenu.ATTEND_AND_NOTIFICATION
                    )
                }

                // 백버튼, 타이틀, 바텀X
                R.id.lockScreenPermissionFragment -> {
                    isTopViewVisible.value = true
                    isBottomAppBarVisible.value = false

                    topViewModel.setTopMenu(
                        leftMenu = TopMenuType.LeftMenu.BACK,
                        middleMenu = TopMenuType.MiddleMenu.TITLE,
                        rightMenu = TopMenuType.RightMenu.NONE,
                        titleText = resources.getString(R.string.set_lock_screen)
                    )
                }
            }
        }
    }

    private fun navigateToCompose(directions: NavDirections?, isCloseCurrentStack: Boolean) {
        directions?.let {
            currentNavigationFragment?.apply {
                exitTransition = MaterialElevationScale(false).apply {
                    duration = 100
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = 100
                }
            }

            if (isCloseCurrentStack) {
                navController.popBackStack()
            }

            navController.navigate(it)
        }
    }
}