package com.dmonster.rewordapp.view.intro

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.databinding.ActivityIntroBinding
import com.dmonster.rewordapp.utils.observeInLifecycleStop
import com.dmonster.rewordapp.view.main.MainActivity
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class IntroActivity :
    AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val binding: ActivityIntroBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_intro)
    }

    private val viewModel: IntroViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.introNavHostFragmentContainer) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    private val currentNavigationFragment: Fragment?
        get() = navHostFragment.childFragmentManager.fragments.first()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        init()
        initViewModelCallback()

        Log.d("아외안되", "onCreate")
    }

    private fun init() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navController.addOnDestinationChangedListener(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun initViewModelCallback(): Unit = with(viewModel) {
        startActivityChannel.onEach {
            startActivity(
                Intent(
                    this@IntroActivity,
                    MainActivity::class.java
                )
            )

            finish()
        }.observeInLifecycleStop(this@IntroActivity)
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