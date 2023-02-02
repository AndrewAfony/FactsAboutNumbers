package andrewafony.factsaboutnumbers.com.main.presentation

import andrewafony.factsaboutnumbers.com.R
import andrewafony.factsaboutnumbers.com.main.sl.ProvideViewModel
import andrewafony.factsaboutnumbers.com.numbers.presentation.NumbersFragment
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner

class MainActivity : AppCompatActivity(), ProvideViewModel {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = provideViewModel(MainViewModel::class.java, this)

        viewModel.observe(this) { strategy ->
            strategy.navigate(supportFragmentManager)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T =
        (application as ProvideViewModel).provideViewModel(clazz, owner)
}

