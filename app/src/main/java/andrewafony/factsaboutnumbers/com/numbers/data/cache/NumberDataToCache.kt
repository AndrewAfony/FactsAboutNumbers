package andrewafony.factsaboutnumbers.com.numbers.data.cache

import andrewafony.factsaboutnumbers.com.numbers.data.NumberData

class NumberDataToCache: NumberData.Mapper<NumberCache> {

    override fun map(id: String, fact: String): NumberCache = NumberCache(id, fact, System.currentTimeMillis())

}