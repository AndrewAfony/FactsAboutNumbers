package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.R
import andrewafony.factsaboutnumbers.com.databinding.FragmentNumbersBinding
import andrewafony.factsaboutnumbers.com.main.presentation.MainViewModel
import andrewafony.factsaboutnumbers.com.main.presentation.ShowDetails
import andrewafony.factsaboutnumbers.com.main.sl.ProvideViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout

class NumbersFragment : Fragment() {

    private lateinit var viewModel: NumbersViewModel
    private val mainViewModel: ShowDetails by activityViewModels<MainViewModel> ()

    private lateinit var binding: FragmentNumbersBinding

    private val watcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) = viewModel.clearError()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (requireActivity() as ProvideViewModel).provideViewModel(
            NumbersViewModel.Base::class.java,
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val factButton = view.findViewById<Button>(R.id.buttonGetFact)
        val randomFactButton = view.findViewById<Button>(R.id.buttonGetRandomFact)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_history)
        val inputLayout = view.findViewById<TextInputLayout>(R.id.inputLayout)

        val adapter = NumbersAdapter(object : ClickListener {
            override fun click(item: NumberUi) {
                mainViewModel.showDetails(item)
            }
        })

        recyclerView.adapter = adapter

        factButton.setOnClickListener {
            viewModel.fetchNumberFact(binding.editText.text.toString())
        }

        randomFactButton.setOnClickListener {
            viewModel.fetchRandomNumberFact()
        }

        viewModel.observeState(this) {
            it.apply(inputLayout, binding.editText)
        }

        viewModel.observeProgress(this) {
            progressBar.visibility = it
        }

        viewModel.observeList(this) {
            adapter.map(it)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        binding.editText.addTextChangedListener(watcher)
    }

    override fun onPause() {
        super.onPause()
        binding.editText.removeTextChangedListener(watcher)
    }
}

abstract class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: Editable?) = Unit
}