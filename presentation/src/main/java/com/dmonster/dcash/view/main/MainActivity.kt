package com.dmonster.dcash.view.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.dcash.NavigationDirections
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseActivity
import com.dmonster.dcash.databinding.ActivityMainBinding
import com.dmonster.dcash.utils.PermissionViewModel
import com.dmonster.dcash.utils.StaticData.tokenData
import com.dmonster.dcash.utils.hideSnackBar
import com.dmonster.dcash.utils.lockscreen.LockScreenService
import com.dmonster.dcash.utils.observeInLifecycleDestroy
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.dcash.utils.observeOnLifecycleDestroy
import com.dmonster.dcash.utils.observeOnLifecycleStop
import com.dmonster.dcash.utils.showSnackBar
import com.dmonster.dcash.view.dialog.basic.BasicDialog
import com.dmonster.dcash.view.dialog.basic.BasicDialogModel
import com.dmonster.dcash.view.event.EventFragment
import com.dmonster.dcash.view.home.HomeFragment
import com.dmonster.dcash.view.home.HomeFragmentDirections
import com.dmonster.dcash.view.intro.IntroFragment
import com.dmonster.dcash.view.login.LoginFragment
import com.dmonster.dcash.view.mypage.MyPageFragment
import com.dmonster.dcash.view.network.NetworkViewModel
import com.dmonster.dcash.view.news.NewsFragment
import com.dmonster.dcash.view.point.PointFragment
import com.dmonster.domain.model.Result
import com.dmonster.domain.type.NetworkState
import com.dmonster.domain.type.TopMenuType
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main
), NavController.OnDestinationChangedListener {

    override val viewModel: MainViewModel by viewModels()

    private val networkViewModel: NetworkViewModel by viewModels()

    private val permissionViewModel: PermissionViewModel by viewModels()

    private val topViewModel: TopViewModel by viewModels()

    @Inject
    lateinit var dataStore: DataStoreModule

    @Inject
    lateinit var errorCallback: ErrorCallback

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
                startLockScreenService()
                navController.popBackStack()

                return@registerForActivityResult
            }
        }

    private val settingDetails: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            permissionViewModel.onActivityResult()
        }

    override fun init() {
        binding.viewModel = viewModel
        binding.topView.viewModel = topViewModel

        navController.addOnDestinationChangedListener(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentNavigationFragment?.let {
                    // 홈을 제외한 루트 경로면 뒤로가기 시 홈 화면으로 이동
                    if (it is NewsFragment || it is EventFragment || it is PointFragment || it is MyPageFragment) {
                        navController.navigate(
                            NavigationDirections.actionGlobalHomeFragment()
                        )
                        return
                    }

                    // 홈 화면 혹은 로그인 화면일 경우 뒤로가기 2번 누르면 앱 종료
                    if (it is HomeFragment || it is LoginFragment) {
                        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                            backKeyPressedTime = System.currentTimeMillis()
                            showSnackBar(
                                this@MainActivity, getString(R.string.main_back_pressed)
                            )
                        } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                            finish()
                        }

                        return
                    }

                    // 인트로 화면 뒤로가기 시 즉시 종료
                    if (it is IntroFragment) {
                        finishAffinity()
                        return
                    }

                    navController.popBackStack()
                }
            }
        })

        networkViewModel.register(checkNetworkState = true)
    }

    override fun initViewModelCallback(): Unit = with(viewModel) {
        getUseLockScreen()

        isUseLockScreen.observeOnLifecycleStop(this@MainActivity) {
            val isServiceRunning = LockScreenService.isRunning(this@MainActivity)
            if (it && !isServiceRunning) {
                startLockScreenService()

                return@observeOnLifecycleStop
            }
        }

        onBottomMenuClickChannel.onEach {
            navController.navigate(
                when (it) {
                    1 -> NavigationDirections.actionGlobalNewsFragment()
                    2 -> NavigationDirections.actionGlobalEventFragment()
                    3 -> NavigationDirections.actionGlobalHomeFragment()
                    4 -> NavigationDirections.actionGlobalPointFragment()
                    5 -> NavigationDirections.actionGlobalMyPageFragment()
                    else -> return@onEach
                }
            )
        }.observeInLifecycleDestroy(this@MainActivity)

        setOverlayPermissionChannel.onEach {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + this@MainActivity.packageName)
            )

            overLayPermission.launch(intent)
        }.observeInLifecycleStop(this@MainActivity)

        checkPermissionChannel.onEach {
            when (it) {
                PermissionViewModel.TYPE_LOCK_SCREEN -> {
                    if (!permissionViewModel.canDrawOverlays()) {
                        navController.navigate(
                            NavigationDirections.actionGlobalBasicDialog(
                                BasicDialogModel(titleText = resources.getString(R.string.set_lock_screen),
                                    text = String.format(
                                        resources.getString(R.string.set_lock_screen_contents),
                                        resources.getString(
                                            R.string.app_name
                                        )
                                    ),
                                    setCancelable = false,
                                    setCheckBox = true to resources.getString(R.string.not_showing_week),
                                    setNegativeButton = true to resources.getString(R.string.cancel),
                                    setPositiveButton = true to resources.getString(R.string.set_use),
                                    buttonClickListener = object : BasicDialog.ButtonClickListener {
                                        override fun onPositiveButtonClick(
                                            view: View, dialog: BasicDialog
                                        ) {
                                            super.onPositiveButtonClick(view, dialog)

                                            if (!permissionViewModel.canDrawOverlays()) {
                                                navController.navigate(
                                                    HomeFragmentDirections.actionHomeFragmentToLockScreenPermissionFragment()
                                                )
                                            } else {
                                                startLockScreenService()
                                            }
                                        }
                                    }),
                            )
                        )
                    }
                }

                PermissionViewModel.TYPE_INTRO -> {
                    permissionViewModel.checkPermission(it)
                }
            }
        }.observeInLifecycleStop(this@MainActivity)

        goPermissionSettingChannel.onEach {
            try {
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${packageName}"))
                settingDetails.launch(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e("REQ_DETAILS_SETTINGS", "권한 error -> ${e.printStackTrace()}")
                val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                settingDetails.launch(intent)
            }
        }.observeInLifecycleStop(this@MainActivity)
    }

    override fun initNetworkViewModelCallback(): Unit = with(networkViewModel) {
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

    override fun initPermissionViewModelCallback() = with(permissionViewModel) {

    }

    override fun initTopViewModelCallback(): Unit = with(topViewModel) {
        onBackClickChannel.onEach {
            navController.popBackStack()
        }.observeInLifecycleStop(this@MainActivity)

        onRightMenuClickChannel.onEach {
            when (it) {
                TopMenuType.RightMenu.SEARCH_AND_NOTIFICATION -> {

                }
            }
        }.observeInLifecycleStop(this@MainActivity)

        onRightMenuSubClickChannel.onEach {
            when (it) {
                TopMenuType.RightMenu.SEARCH_AND_NOTIFICATION -> {

                }
            }
        }.observeInLifecycleStop(this@MainActivity)
    }

    override fun initErrorCallback(): Unit = with(errorCallback) {
        errorData.onEach {

        }.observeInLifecycleDestroy(this@MainActivity)

        tokenExpiration.onEach {

        }.observeInLifecycleDestroy(this@MainActivity)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestinationChanged(
        controller: NavController, destination: NavDestination, arguments: Bundle?
    ) {
        hideSnackBar()

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
                        rightMenu = TopMenuType.RightMenu.SEARCH_AND_NOTIFICATION
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

                // 백버튼, 아이콘2개
                R.id.newsDetailFragment -> {
                    isTopViewVisible.value = true
                    isBottomAppBarVisible.value = false

                    topViewModel.setTopMenu(
                        leftMenu = TopMenuType.LeftMenu.BACK,
                        rightMenu = TopMenuType.RightMenu.POINT_AND_SHARE,
                    )
                }
            }
        }
    }

    private fun navigateToCompose(directions: NavDirections?) {
        directions?.let {
            currentNavigationFragment?.apply {
                exitTransition = MaterialElevationScale(false).apply {
                    duration = 100
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = 100
                }
            }

            navController.navigate(it)
        }
    }

    private fun startLockScreenService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, LockScreenService::class.java))
        } else {
            startService(Intent(this, LockScreenService::class.java))
        }

        viewModel.setUseLockScreen(true)
    }

    override fun onDestroy() {
        networkViewModel.unRegister()
        super.onDestroy()
    }

    fun getNewAccessToken() {
        tokenData.value.refreshToken?.let {
            viewModel.getAccessToken(it).observeOnLifecycleStop(this) { result ->
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        result.data?.let { token ->
                            tokenData.value = token
                        }
                    }

                    is Result.Error -> {

                    }

                    is Result.NetworkError -> {

                    }
                }
            }
        }
    }
}