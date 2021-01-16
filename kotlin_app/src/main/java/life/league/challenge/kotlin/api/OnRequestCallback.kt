package life.league.challenge.kotlin.api

sealed class Either<out E, out V> {
    data class Failure<out E>(val error: E) : Either<E, Nothing>()
    data class Success<out V>(val value: V) : Either<Nothing, V>()

    companion object {
        fun <E> failure(error: E): Either<E, Nothing> = Failure(error)
        fun <V> success(value: V): Either<Nothing, V> = Success(value)
    }

}
