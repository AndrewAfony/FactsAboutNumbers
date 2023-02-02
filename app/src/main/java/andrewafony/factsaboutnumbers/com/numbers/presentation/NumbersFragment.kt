package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.databinding.FragmentNumbersBinding
import andrewafony.factsaboutnumbers.com.main.presentation.BaseFragment
import andrewafony.factsaboutnumbers.com.main.sl.ProvideViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View

class NumbersFragment : BaseFragment<FragmentNumbersBinding>() {

    private lateinit var viewModel: NumbersViewModel

    private val watcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) = viewModel.clearError()
    }

    override val bindingInflater: (LayoutInflater) -> FragmentNumbersBinding
        get() = FragmentNumbersBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (requireActivity() as ProvideViewModel).provideViewModel(
            NumbersViewModel.Base::class.java,
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NumbersAdapter(object : ClickListener {
            override fun click(item: NumberUi) {
                mainViewModel.showDetails(item)
            }
        })

        binding.run {
            rvHistory.adapter = adapter

            buttonGetFact.setOnClickListener {
                viewModel.fetchNumberFact(binding.editText.text.toString())
            }

            buttonGetRandomFact.setOnClickListener {
                viewModel.fetchRandomNumberFact()
            }
        }

        viewModel.observeState(this) {
            it.apply(binding.inputLayout, binding.editText)
        }

        viewModel.observeProgress(this) {
            binding.progressBar.visibility = it
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