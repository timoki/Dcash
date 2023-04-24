package kr.timoky.domain.model.navi

sealed class NavigateType {
    abstract val getItem: NavItem?
    abstract val fragmentName: String

    class Login(private val navItem: NavItem? = null) : NavigateType() {
        override val getItem: NavItem?
            get() = navItem
        override val fragmentName: String
            get() = "login"
    }

    class Home(private val navItem: NavItem? = null) : NavigateType() {
        override val getItem: NavItem?
            get() = navItem
        override val fragmentName: String
            get() = "home"
    }

    class News(private val navItem: NavItem? = null) : NavigateType() {
        override val getItem: NavItem?
            get() = navItem
        override val fragmentName: String
            get() = "news"
    }

    class Event(private val navItem: NavItem? = null) : NavigateType() {
        override val getItem: NavItem?
            get() = navItem
        override val fragmentName: String
            get() = "event"
    }

    class Point(private val navItem: NavItem? = null) : NavigateType() {
        override val getItem: NavItem?
            get() = navItem
        override val fragmentName: String
            get() = "point"
    }

    class MyPage(private val navItem: NavItem? = null) : NavigateType() {
        override val getItem: NavItem?
            get() = navItem
        override val fragmentName: String
            get() = "myPage"
    }
}