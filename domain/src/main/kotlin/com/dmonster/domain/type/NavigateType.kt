package com.dmonster.domain.type

import com.dmonster.domain.model.navi.BottomNavItem

sealed class NavigateType : EnumClassProguard {
    abstract val getItem: BottomNavItem?
    abstract val fragmentName: String

    class Login(private val bottomNavItem: BottomNavItem? = null) : NavigateType() {
        override val getItem: BottomNavItem?
            get() = bottomNavItem
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

    class LockScreenPermission(private val bottomNavItem: BottomNavItem? = null) : NavigateType() {
        override val getItem: BottomNavItem?
            get() = bottomNavItem
        override val fragmentName: String
            get() = "lockScreenPermission"
    }
}