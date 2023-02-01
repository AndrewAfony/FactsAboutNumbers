package andrewafony.factsaboutnumbers.com.details.sl

import andrewafony.factsaboutnumbers.com.details.presentation.DetailsViewModel
import andrewafony.factsaboutnumbers.com.main.sl.Module
import andrewafony.factsaboutnumbers.com.main.sl.ProvideNumberDetails

class NumberDetailsModule(
    private val provideNumberDetails: ProvideNumberDetails
): Module<DetailsViewModel> {
    override fun viewModel(): DetailsViewModel {
        return DetailsViewModel(provideNumberDetails.provide())
    }
}