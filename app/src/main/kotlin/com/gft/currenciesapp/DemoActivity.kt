package com.gft.currenciesapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.navigation.fragment.NavHostFragment
import com.gft.currencies.ui.common.containers.CurrencyListDataHolder
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewModel
import com.gft.currenciesapp.databinding.ActivityDemoBinding
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : AppCompatActivity() {

    private val viewModel: DemoOptionsViewModel by viewModel()

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDemoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupDataDeliveryForCurrencyListDataHolder()
    }

    private fun setupDataDeliveryForCurrencyListDataHolder() {
        val navHost = supportFragmentManager.findFragmentById(binding.mainNavigationNavHost.id) as NavHostFragment
        val navHostFragmentManager = navHost.childFragmentManager

        navHostFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentLifecycleCallbacks() {

                override fun onFragmentViewCreated(fm: FragmentManager, fragment: Fragment, v: View, savedInstanceState: Bundle?) {
                    if (fragment is CurrencyListDataHolder) {
                        fragment.setCurrencyListDataSource(viewModel.viewStates.map { viewState -> viewState.currenciesList.orEmpty() })
                    }
                }
            },
            false,
        )
    }
}
