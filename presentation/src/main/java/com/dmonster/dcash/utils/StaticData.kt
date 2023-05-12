package com.dmonster.dcash.utils

import com.dmonster.domain.model.TokenModel
import kotlinx.coroutines.flow.MutableStateFlow

object StaticData {
    val tokenData = MutableStateFlow(TokenModel())
}