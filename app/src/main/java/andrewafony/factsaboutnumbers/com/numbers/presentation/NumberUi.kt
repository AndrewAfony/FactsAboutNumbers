package andrewafony.factsaboutnumbers.com.numbers.presentation

import android.widget.TextView

data class NumberUi(
    private val id: String,
    private val fact: String
) {

    fun map(title: TextView, subtitle: TextView) {
        title.text = id
        subtitle.text = fact
    }
}