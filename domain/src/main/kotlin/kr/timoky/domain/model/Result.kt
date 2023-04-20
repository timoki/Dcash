package kr.timoky.domain.model

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : Result<T>()
    class Success<T>(data: T?) : Result<T>(data)
    class NetworkError<T>(message: String?, data: T? = null) : Result<T>(data, message)
    class Error<T>(message: String?, data: T? = null) : Result<T>(data, message)
}
