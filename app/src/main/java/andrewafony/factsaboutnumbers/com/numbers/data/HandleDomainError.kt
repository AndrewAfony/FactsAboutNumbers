package andrewafony.factsaboutnumbers.com.numbers.data

import andrewafony.factsaboutnumbers.com.numbers.domain.HandleError
import andrewafony.factsaboutnumbers.com.numbers.domain.NoConnectionException
import andrewafony.factsaboutnumbers.com.numbers.domain.ServiceUnavailableException
import java.net.UnknownHostException

class HandleDomainError : HandleError<Exception> {

    override fun handle(e: Exception): Exception = when (e) {
        is UnknownHostException -> NoConnectionException()
        else -> ServiceUnavailableException()
    }

}