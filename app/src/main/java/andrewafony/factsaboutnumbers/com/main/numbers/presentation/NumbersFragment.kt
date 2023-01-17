package andrewafony.factsaboutnumbers.com.main.numbers.presentation

import andrewafony.factsaboutnumbers.com.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class NumbersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.progressBar).visibility = View.GONE

        view.findViewById<View>(R.id.buttonGetFact).setOnClickListener {

        }

    }
}