package com.dmonster.data.utils

import com.dmonster.data.remote.dto.base.BaseResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ErrorCallback {
    private val _errorData = Channel<BaseResponse<*>>(Channel.CONFLATED)
    val errorData = _errorData.receiveAsFlow()

    suspend fun postErrorData(errorData: BaseResponse<*>) {
        this._errorData.send(errorData)
    }

    private val _tokenExpiration = Channel<Unit>(Channel.CONFLATED)
    val tokenExpiration = _tokenExpiration.receiveAsFlow()

    suspend fun postTokenExpiration() {
        _tokenExpiration.send(Unit)
    }
}