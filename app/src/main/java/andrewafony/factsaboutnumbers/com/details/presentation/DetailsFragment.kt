package andrewafony.factsaboutnumbers.com.details.presentation

import andrewafony.factsaboutnumbers.com.databinding.FragmentDetailsBinding
import andrewafony.factsaboutnumbers.com.main.presentation.MainViewModel
import andrewafony.factsaboutnumbers.com.main.presentation.ObserveDetails
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailsFragment: Fragment() {

    private val viewModel: ObserveDetails by activityViewModels<MainViewModel>()

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeDetails(this) { details ->
            binding.detailsTextView.text = details
        }
    }
}