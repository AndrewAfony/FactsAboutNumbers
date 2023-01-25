package andrewafony.factsaboutnumbers.com.numbers.domain

import andrewafony.factsaboutnumbers.com.R
import andrewafony.factsaboutnumbers.com.numbers.presentation.ManageResources

interface HandleError<T> {

    fun handle(e: Exception): T

    class Base(private val manageResources: ManageResources) : HandleError<String> {
        override fun handle(e: Exception): String = manageResources.string(
            when (e) {
                is NoConnectionException -> R.string.no_connection_exception
                else -> R.string.service_is_unavailable
            }
        )
    }
}
