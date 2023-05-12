package com.dmonster.domain.type

sealed class TokenErrorType : EnumClassProguard {
    abstract val value: Int

    object TypeUnprocessed : TokenErrorType() {
        override val value: Int
            get() = 400
    }
    object TypeExpired : TokenErrorType() {
        override val value: Int
            get() = 401
    }

    object TypeRequestForbidden : TokenErrorType() {
        override val value: Int
            get() = 403
    }

    object TypeRequestMethodNotAllowed : TokenErrorType() {
        override val value: Int
            get() = 405
    }
}