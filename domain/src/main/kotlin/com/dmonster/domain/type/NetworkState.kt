package com.dmonster.domain.type

enum class NetworkState : EnumClassProguard {
    CONNECT_ERROR,
    CONNECT_NETWORK,
    DISCONNECT_NETWORK,
    CONNECT_NETWORK_BUT_NOT_USE_MOBILE_DATA
}