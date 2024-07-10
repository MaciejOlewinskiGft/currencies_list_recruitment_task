package com.gft.currencies.ui.screens.demooptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gft.common.ui.handleNavigationEffects
import com.gft.common.ui.handleViewEffects
import com.gft.common.ui.handleViewStateUpdates
import com.gft.currencies.ui.databinding.FragmentDemoOptionsBinding
import com.gft.currencies.ui.screens.demooptions.DemoOptionsFragmentDirections.Companion.toCurrencyListFragment
import com.gft.currencies.ui.screens.demooptions.DemoOptionsNavigationEffect.NavigateToCurrencyList
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEffect.ShowToast
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnClearDataButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnInsertDataButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnShowAllCurrenciesButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnShowCryptoCurrenciesButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnShowFiatCurrenciesButtonClicked
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DemoOptionsFragment : Fragment() {

    private val viewModel: DemoOptionsViewModel by activityViewModel()

    private lateinit var binding: FragmentDemoOptionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDemoOptionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservingViewStates()
        setupObservingViewEffects()
        setupObservingNavigationEffects()

        binding.run {
            clearDataButton.setOnClickListener { viewModel.onEvent(OnClearDataButtonClicked) }
            insertDataButton.setOnClickListener { viewModel.onEvent(OnInsertDataButtonClicked) }
            showCryptoCurrenciesButton.setOnClickListener { viewModel.onEvent(OnShowCryptoCurrenciesButtonClicked) }
            showFiatCurrencies.setOnClickListener { viewModel.onEvent(OnShowFiatCurrenciesButtonClicked) }
            showAllCurrenciesButton.setOnClickListener { viewModel.onEvent(OnShowAllCurrenciesButtonClicked) }
        }
    }

    private fun setupObservingViewStates() = handleViewStateUpdates(viewModel.viewStates) { viewState ->
        binding.run {
            progressIndicator.visibility = if (viewState.isLoadingIndicatorVisible) VISIBLE else GONE

            clearDataButton.isEnabled = viewState.areButtonsEnabled
            insertDataButton.isEnabled = viewState.areButtonsEnabled
            showCryptoCurrenciesButton.isEnabled = viewState.areButtonsEnabled
            showFiatCurrencies.isEnabled = viewState.areButtonsEnabled
            showAllCurrenciesButton.isEnabled = viewState.areButtonsEnabled
        }
    }

    private fun setupObservingViewEffects() = handleViewEffects(viewModel.viewEffects) { viewEffect ->
        when (viewEffect) {
            is ShowToast -> Toast.makeText(requireActivity(), viewEffect.messageResId, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupObservingNavigationEffects() = handleNavigationEffects(viewModel.navigationEffects) { navigationEffect ->
        when (navigationEffect) {
            NavigateToCurrencyList -> findNavController().navigate(toCurrencyListFragment())
        }
    }
}
