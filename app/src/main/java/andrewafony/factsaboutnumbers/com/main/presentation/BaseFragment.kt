package andrewafony.factsaboutnumbers.com.main.presentation

import andrewafony.factsaboutnumbers.com.main.sl.ProvideViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    protected lateinit var binding: VB

    protected val mainViewModel by activityViewModels<MainViewModel>()

//    protected val mainViewModel by lazy {
//        (activity as ProvideViewModel).provideViewModel(MainViewModel::class.java, requireActivity())
//    }

    abstract val bindingInflater : (LayoutInflater) -> VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindingInflater(inflater)
        return binding.root
    }
}