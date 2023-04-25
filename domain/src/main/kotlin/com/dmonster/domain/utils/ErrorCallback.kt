package com.dmonster.domain.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import com.dmonster.domain.model.base.BaseModel

class ErrorCallback {
    private val _errorData = Channel<BaseModel>(Channel.CONFLATED)
    val errorData = _errorData.receiveAsFlow()

    suspend fun postErrorData(errorData: BaseModel) {
        this._errorData.send(errorData)
    }
}