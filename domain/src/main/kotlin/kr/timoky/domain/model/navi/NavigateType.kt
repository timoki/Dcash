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
}