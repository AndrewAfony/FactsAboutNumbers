package andrewafony.factsaboutnumbers.com.numbers.data

data class NumberData(
    private val id: String,
    private val fact: String
) {

    interface Mapper<T> {
        fun map(id: String, fact: String): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, fact)

    fun matches(number: String) = number == id
}
