package com.dmonster.rewordapp.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.databinding.ActivityMainBinding
import com.dmonster.rewordapp.utils.observeInLifecycleStop
import com.dmonster.rewordapp.utils.observeOnLifecycleDestroy
import com.dmonster.rewordapp.utils.observeOnLifecycleStop
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val viewModel: MainViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    private val currentNavigationFragment: Fragment?
        get() = navHostFragment.childFragmentManager.fragments.first()

    private var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        initViewModelCallback()
    }

    private fun init() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navController.addOnDestinationChangedListener(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.previousBackStackEntry == null || !navController.popBackStack()) {
                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                        backKeyPressedTime = System.currentTimeMillis()
                        /*showSnackBar(
                            this@MainActivity,
                            getString(R.string.main_back_pressed)
                        )*/
                    } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                        finish()
                    }
                }
            }
        })
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
                when (it.first) {
                    else -> navigateToCompose(it.second)
                }
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }

    private fun navigateToCompose(directions: NavDirections) {
        currentNavigationFragment?.apply {
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 300
            }
        }

        navController.navigate(directions)
    }
}