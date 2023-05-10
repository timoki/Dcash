package com.dmonster.domain.model

import com.dmonster.domain.model.base.BaseModel

data class TokenModel(
    val accessToken: String? = null,
    val refreshToken: String? = null,
) : BaseModel {
    override val key: Int
        get() = 0
}