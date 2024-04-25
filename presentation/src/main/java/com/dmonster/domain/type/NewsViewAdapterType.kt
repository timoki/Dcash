package com.dmonster.domain.type

import java.io.Serializable

enum class NewsViewAdapterType : EnumClassProguard, Serializable {
    NEWS,
    BANNER,
    RECOMMEND_HORIZONTAL,
    RECOMMEND_VERTICAL,
    CUSTOM,
    CATEGORY,
}