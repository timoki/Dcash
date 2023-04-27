package com.dmonster.rewordapp.view.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.viewModelScope
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.domain.type.NetworkState
import com.dmonster.rewordapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val application: Application,
    private val dataStore: DataStoreModule
) : BaseViewModel() {
    private var manager: ConnectivityManager? = null

    private var isNetworkConn = false

    private var isWifiConn = false

    private var isCellularConn = false

    private val _currentNetworkState = MutableStateFlow<NetworkState>(NetworkState.CONNECT_NETWORK)
    val currentNetworkState = _currentNetworkState.asStateFlow()

    val networkState = MutableStateFlow(NetworkState.CONNECT_ERROR)

    private var checkMobileData = false

    private val _networkAction = Channel<Boolean>(Channel.CONFLATED)
    val networkAction = _networkAction.receiveAsFlow()

    private val wifiNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isWifiConn = true
            changeNetworkState()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isWifiConn = false
            changeNetworkState()
        }
    }

    private val cellularNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isCellularConn = true
            changeNetworkState()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isCellularConn = false
            changeNetworkState()
        }
    }

    private val defaultNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isNetworkConn = true
            changeNetworkState()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isNetworkConn = false
            changeNetworkState()
        }
    }

    fun changeNetworkState() = viewModelScope.launch {
        val isDataConn = dataStore.getUserMobileData.first()
        networkState.value = when {
            isNetworkConn && (isWifiConn || !checkMobileData || (isDataConn && isCellularConn)) -> {
                NetworkState.CONNECT_NETWORK
            }
            isNetworkConn && !isDataConn && isCellularConn -> {
                NetworkState.CONNECT_NETWORK_BUT_NOT_USE_MOBILE_DATA
            }
            else -> {
                NetworkState.DISCONNECT_NETWORK
            }
        }

        if (networkState.value != currentNetworkState.value) {
            _currentNetworkState.value = networkState.value
        }
    }

    fun register(checkMobileData: Boolean = true, checkNetworkState: Boolean = false) {
        this.checkMobileData = checkMobileData
        manager =
            application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerDefaultNetwork()
        } else {
            isNetworkConn = true
        }
        registerWifi()
        registerCellular()

        if (checkNetworkState) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkActiveNetwork()
            } else {
                checkActiveNetworkInfo()
            }
            changeNetworkState()
        }
    }

    fun unRegister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unRegisterDefaultNetwork()
        }
        unRegisterWifi()
        unRegisterCellular()
        manager = null
    }

    private fun checkActiveNetwork() {
        manager?.activeNetwork?.let {
            isNetworkConn = true
            isWifiConn = hasTransport(it, NetworkCapabilities.TRANSPORT_WIFI)
            isCellularConn = hasTransport(it, NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    private fun hasTransport(network: Network, transport: Int): Boolean =
        manager?.getNetworkCapabilities(network)?.hasTransport(transport) ?: false

    @Suppress("DEPRECATION")
    private fun checkActiveNetworkInfo() {
        manager?.activeNetworkInfo?.let {
            if (it.isConnectedOrConnecting) {
                isNetworkConn = true
                isWifiConn = it.type == ConnectivityManager.TYPE_WIFI
                isCellularConn = it.type == ConnectivityManager.TYPE_MOBILE
            }
        }
    }

    private fun registerWifi() {
        manager?.registerNetworkCallback(
            createNetworkRequest(NetworkCapabilities.TRANSPORT_WIFI),
            wifiNetworkCallback
        )
    }

    private fun unRegisterWifi() {
        manager?.unregisterNetworkCallback(wifiNetworkCallback)
    }

    private fun registerCellular() {
        manager?.registerNetworkCallback(
            createNetworkRequest(NetworkCapabilities.TRANSPORT_CELLULAR),
            cellularNetworkCallback
        )
    }

    private fun unRegisterCellular() {
        manager?.unregisterNetworkCallback(cellularNetworkCallback)
    }

    private fun registerDefaultNetwork() {
        manager?.registerDefaultNetworkCallback(defaultNetworkCallback)
    }

    private fun unRegisterDefaultNetwork() {
        manager?.unregisterNetworkCallback(defaultNetworkCallback)
    }

    private fun createNetworkRequest(capability: Int): NetworkRequest {
        return NetworkRequest.Builder()
            .addTransportType(capability)
            .build()
    }

    private fun changeUseMobileDataState() = viewModelScope.launch {
        val useMobileData = dataStore.getUserMobileData.first()
        dataStore.putUseMobileData(!useMobileData)
        changeNetworkState()
    }

    fun onReTryClick() = viewModelScope.launch {
        when (networkState.value) {
            NetworkState.DISCONNECT_NETWORK,
            NetworkState.CONNECT_ERROR -> {
                _networkAction.send(false)
            }

            NetworkState.CONNECT_NETWORK_BUT_NOT_USE_MOBILE_DATA -> {
                changeUseMobileDataState()
            }

            NetworkState.CONNECT_NETWORK -> {
                _networkAction.send(true)
            }
        }
    }
}