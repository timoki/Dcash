package com.dmonster.dcash.utils

import com.dmonster.dcash.utils.StaticData.tokenData
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.usecase.GetAccessTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokenManager {
    @Inject
    lateinit var getAccessTokenUseCase: GetAccessTokenUseCase

    private fun getAccessToken(
        refreshToken: String,
        scope: CoroutineScope
    ): StateFlow<Result<TokenModel>> =
        getAccessTokenUseCase.invoke(refreshToken)
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading()
            )

    fun getAccessToken() {
        tokenData.value.refreshToken?.let {
            CoroutineScope(Dispatchers.IO).launch {
                getAccessToken(it, this).collect { result ->
                    when (result) {
                        is Result.Loading -> {

                        }

                        is Result.Success -> {
                            result.data?.let {
                                tokenData.value = it
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
}