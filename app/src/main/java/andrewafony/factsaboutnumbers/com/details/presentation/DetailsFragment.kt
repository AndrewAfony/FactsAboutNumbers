package andrewafony.factsaboutnumbers.com.details.presentation

import andrewafony.factsaboutnumbers.com.databinding.FragmentDetailsBinding
import andrewafony.factsaboutnumbers.com.main.presentation.BaseFragment
import andrewafony.factsaboutnumbers.com.main.presentation.MainViewModel
import andrewafony.factsaboutnumbers.com.main.presentation.ObserveDetails
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailsFragment: BaseFragment<FragmentDetailsBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentDetailsBinding
        get() = FragmentDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.observeDetails(this) { details ->
            binding.detailsTextView.text = details
        }
    }
}