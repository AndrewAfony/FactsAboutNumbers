package andrewafony.factsaboutnumbers.com.main.sl

import andrewafony.factsaboutnumbers.com.main.presentation.DetailsCommunication
import andrewafony.factsaboutnumbers.com.main.presentation.MainViewModel

class MainModule(private val provideNavigation: ProvideNavigation): Module<MainViewModel> {

    override fun viewModel(): MainViewModel =
        MainViewModel(provideNavigation.provideNavigation(), DetailsCommunication.Base())
}