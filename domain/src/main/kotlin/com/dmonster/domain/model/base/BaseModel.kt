package com.dmonster.domain.model.base

import java.io.Serializable

interface BaseModel : Serializable {
    val key: Long
}