package com.dmonster.domain.type

import com.dmonster.domain.model.NewsModel
import com.dmonster.domain.model.dialog.BasicDialogModel
import com.dmonster.domain.model.navi.BottomNavItem

sealed class NavigateType : EnumClassProguard {
    open val getItem: BottomNavItem? = null
    abstract val fragmentName: String

    class BasicDialog(
        private val model: BasicDialogModel
    ) : NavigateType() {
        override val fragmentName: String
            get() = "basicDialog"

        val getModel: BasicDialogModel
            get() = model
    }

    class Login : NavigateType() {
        override val fragmentName: String
            get() = "login"
    }

    class Home(private val bottomNavItem: BottomNavItem? = null) : NavigateType() {
        override val getItem: BottomNavItem?
            get() = bottomNavItem
        override val fragmentName: String
            get() = "home"
    }

    class News(private val bottomNavItem: BottomNavItem? = null) : NavigateType() {
        override val getItem: BottomNavItem?
            get() = bottomNavItem
        override val fragmentName: String
            get() = "news"
    }

    class Event(private val bottomNavItem: BottomNavItem? = null) : NavigateType() {
        override val getItem: BottomNavItem?
            get() = bottomNavItem
        override val fragmentName: String
            get() = "event"
    }

    class Point(private val bottomNavItem: BottomNavItem? = null) : NavigateType() {
        override val getItem: BottomNavItem?
            get() = bottomNavItem
        override val fragmentName: String
            get() = "point"
    }

    class MyPage(private val bottomNavItem: BottomNavItem? = null) : NavigateType() {
        override val getItem: BottomNavItem?
            get() = bottomNavItem
        override val fragmentName: String
            get() = "myPage"
    }

    class LockScreenPermission : NavigateType() {
        override val fragmentName: String
            get() = "lockScreenPermission"
    }

    class NewsDetailFromHome : NavigateType() {
        override val fragmentName: String
            get() = "newsDetailFromHome"
    }

    class NewsDetailFromNews(
        private val model: NewsModel
    ) : NavigateType() {
        override val fragmentName: String
            get() = "newsDetailFromNews"

        val getModel: NewsModel
            get() = model
    }
}