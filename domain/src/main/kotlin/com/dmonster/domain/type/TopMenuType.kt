package com.dmonster.domain.type

sealed class TopMenuType {
    class LeftMenu() : TopMenuType() {
        companion object {
            const val NONE = 0
            const val BACK = 1
            const val LOGO = 2
        }
    }

    class MiddleMenu() : TopMenuType() {
        companion object {
            const val NONE = 0
            const val TITLE = 1
        }
    }

    class RightMenu() : TopMenuType() {
        companion object {
            const val NONE = 0
            const val POINT = 1
            const val SHARE = 2
            const val NOTIFICATION = 3
            const val REMOVE = 4
            const val POINT_AND_SHARE = 5
            const val ATTEND_AND_NOTIFICATION = 6
            const val SEARCH_AND_NOTIFICATION = 7
        }
    }
}
