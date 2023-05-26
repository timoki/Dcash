package com.dmonster.domain.type

import com.dmonster.domain.model.base.BaseModel
import com.dmonster.domain.model.paging.news.NewsListModel

sealed class NavigateType : EnumClassProguard {
    abstract val fragmentName: String

    class BasicDialog(
        private val model: BaseModel
    ) : NavigateType() {
        override val fragmentName: String
            get() = "basicDialog"

        val getModel: BaseModel
            get() = model
    }

    class Login : NavigateType() {
        override val fragmentName: String
            get() = "login"
    }

    class Home : NavigateType() {
        override val fragmentName: String
            get() = "home"
    }

    class News : NavigateType() {
        override val fragmentName: String
            get() = "news"
    }

    class Event : NavigateType() {
        override val fragmentName: String
            get() = "event"
    }

    class Point : NavigateType() {
        override val fragmentName: String
            get() = "point"
    }

    class MyPage : NavigateType() {
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
        private val model: NewsListModel
    ) : NavigateType() {
        override val fragmentName: String
            get() = "newsDetailFromNews"

        val getModel: NewsListModel
            get() = model
    }
}