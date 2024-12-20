package andrewafony.factsaboutnumbers.com.numbers.domain

sealed class NumbersResult {

    interface Mapper<T> {
        fun map(list: List<NumberFact>, errorMessage: String): T
    }

    abstract fun <T> map(mapper: Mapper<T>): T

    data class Success(private val list: List<NumberFact> = emptyList()) : NumbersResult() {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(list, "")
    }

    data class Failure(private val message: String) : NumbersResult() {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(emptyList(), message)
    }
}
