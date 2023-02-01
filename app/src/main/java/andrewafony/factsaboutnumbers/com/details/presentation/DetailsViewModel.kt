package andrewafony.factsaboutnumbers.com.details.presentation

import andrewafony.factsaboutnumbers.com.details.data.NumberFactDetails
import androidx.lifecycle.ViewModel

class DetailsViewModel(
    private val data: NumberFactDetails.Read
): ViewModel(), NumberFactDetails.Read {

    override fun read(): String = data.read()
}