package com.dmonster.dcash.view.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.domain.type.NavigateType
import com.dmonster.domain.type.NetworkState
import com.dmonster.domain.type.TopMenuType
import com.dmonster.dcash.NavigationDirections
import com.dmonster.dcash.R
import com.dmonster.dcash.databinding.ActivityMainBinding
import com.dmonster.dcash.utils.*
import com.dmonster.dcash.utils.StaticData.tokenData
import com.dmonster.dcash.utils.lockscreen.LockScreenService
import com.dmonster.dcash.view.dialog.BasicDialog
import com.dmonster.dcash.view.event.EventFragment
import com.dmonster.dcash.view.home.HomeFragment
import com.dmonster.dcash.view.home.HomeFragmentDirections
import com.dmonster.dcash.view.intro.IntroFragment
import com.dmonster.dcash.view.login.LoginFragment
import com.dmonster.dcash.view.mypage.MyPageFragment
import com.dmonster.dcash.view.network.NetworkViewModel
import com.dmonster.dcash.view.news.NewsFragment
import com.dmonster.dcash.view.news.NewsFragmentDirections
import com.dmonster.dcash.view.point.PointFragment
import com.dmonster.domain.model.Result
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        initViewModelCallback()
        initNetworkViewModelCallback()
        initPermissionViewModelCallback()
        initTopViewModelCallback()
        initErrorCallback()
    }

    private fun init() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.topView.viewModel = topViewModel

        navController.addOnDestinationChangedListener(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentNavigationFragment?.let {
                    // 홈을 제외한 루트 경로면 뒤로가기 시 홈 화면으로 이동
                    if (it is NewsFragment || it is EventFragment || it is PointFragment || it is MyPageFragment) {
                        viewModel.fragmentNavigateTo(NavigateType.Home())
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

    private fun initViewModelCallback() = with(viewModel) {
        getUseLockScreen()

        isUseLockScreen.observeOnLifecycleStop(this@MainActivity) {
            val isServiceRunning = LockScreenService.isRunning(this@MainActivity)
            if (it && !isServiceRunning) {
                startLockScreenService()

                return@observeOnLifecycleStop
            }
        }

        isLoading.observeOnLifecycleDestroy(this@MainActivity) { show ->
            if (show) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }

        navigateToChannel.observeOnLifecycleDestroy(this@MainActivity) { item ->
            item?.let {
                navigateToCompose(
                    when (it) {
                        is NavigateType.Login -> {
                            NavigationDirections.actionGlobalLoginFragment()
                        }

                        is NavigateType.Home -> {
                            NavigationDirections.actionGlobalHomeGraph()
                        }

                        is NavigateType.News -> {
                            NavigationDirections.actionGlobalNewsGraph()
                        }

                        is NavigateType.Event -> {
                            NavigationDirections.actionGlobalEventGraph()
                        }

                        is NavigateType.Point -> {
                            NavigationDirections.actionGlobalPointGraph()
                        }

                        is NavigateType.MyPage -> {
                            NavigationDirections.actionGlobalMyPageGraph()
                        }

                        is NavigateType.LockScreenPermission -> {
                            HomeFragmentDirections.actionHomeFragmentToLockScreenPermissionFragment()
                        }

                        is NavigateType.NewsDetailFromHome -> {
                            HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment()
                        }

                        is NavigateType.NewsDetailFromNews -> {
                            NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment()
                        }
                    }
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
            when (it) {
                PermissionViewModel.TYPE_LOCK_SCREEN -> {
                    if (!permissionViewModel.canDrawOverlays()) {
                        BasicDialog(this@MainActivity, binding.root as ViewGroup?).setCancelable(false)
                            .setTitle(resources.getString(R.string.set_lock_screen)).setText(
                                String.format(
                                    resources.getString(R.string.set_lock_screen_contents),
                                    resources.getString(
                                        R.string.app_name
                                    )
                                )
                            ).setCheckBox(true, resources.getString(R.string.not_showing_week))
                            .setNegativeButton(true, resources.getString(R.string.cancel))
                            .setPositiveButton(
                                true, resources.getString(R.string.set_use)
                            ) { view, dialog ->
                                dialog?.let {
                                    it.dismiss()

                                    if (!permissionViewModel.canDrawOverlays()) {
                                        fragmentNavigateTo(NavigateType.LockScreenPermission())

                                        return@setPositiveButton
                                    }

                                    startLockScreenService()
                                }
                            }.show()
                    }
                }

                PermissionViewModel.TYPE_INTRO -> {
                    permissionViewModel.checkPermission(it)
                }
            }
        }.observeInLifecycleStop(this@MainActivity)

        goPermissionSettingChannel.onEach {
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:${packageName}"))
                settingDetails.launch(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e("REQ_DETAILS_SETTINGS","권한 error -> ${e.printStackTrace()}")
                val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                settingDetails.launch(intent)
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

    private fun initErrorCallback() = with(errorCallback) {
        errorData.onEach {

        }.observeInLifecycleDestroy(this@MainActivity)

        tokenExpiration.onEach {

        }.observeInLifecycleDestroy(this@MainActivity)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("아외안되", "$newConfig")
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