package andrewafony.factsaboutnumbers.com.main.sl

import andrewafony.factsaboutnumbers.com.main.presentation.DetailsCommunication
import andrewafony.factsaboutnumbers.com.main.presentation.MainViewModel
import andrewafony.factsaboutnumbers.com.random.WorkManagerWrapper

class MainModule(private val provideNavigation: ProvideNavigation, private val workManager: WorkManagerWrapper): Module<MainViewModel> {

    override fun viewModel(): MainViewModel =
        MainViewModel(provideNavigation.provideNavigation(), DetailsCommunication.Base(), workManager)
}