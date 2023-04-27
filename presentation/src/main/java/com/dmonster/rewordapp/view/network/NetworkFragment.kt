package com.dmonster.rewordapp.view.network

import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmonster.domain.type.NetworkState
import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.base.BaseDialogFragment
import com.dmonster.rewordapp.databinding.FragmentNetworkBinding
import com.dmonster.rewordapp.utils.observeInLifecycleDestroy
import com.dmonster.rewordapp.utils.observeOnLifecycleDestroy
import com.dmonster.rewordapp.utils.setTransparentStatusBar
import com.dmonster.rewordapp.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NetworkFragment : BaseDialogFragment<FragmentNetworkBinding, NetworkViewModel>() {

    private val args: NetworkFragmentArgs by navArgs()

    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.networkState.value = args.networkState

        isCancelable = false
    }

    override fun initViewModelCallback(): Unit = with(viewModel) {
        currentNetworkState.observeOnLifecycleDestroy(viewLifecycleOwner) {
            when (it) {
                NetworkState.CONNECT_NETWORK -> {
                    findNavController().popBackStack()
                }
                else -> {}
            }
        }

        networkAction.onEach {
            if (it) {
                findNavController().popBackStack()
            } else {
                showSnackBar(requireActivity(), getString(R.string.disabled_network))
            }
        }.observeInLifecycleDestroy(viewLifecycleOwner)
    }

    override fun onResume() {
        dialog?.window?.let { window ->
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // 기본 배경 제거
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = params
            setTransparentStatusBar(window)
        }
        super.onResume()
    }
}