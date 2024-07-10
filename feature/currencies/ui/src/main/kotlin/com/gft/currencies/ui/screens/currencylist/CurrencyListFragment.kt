package com.gft.currencies.ui.screens.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.gft.common.ui.handleViewStateUpdates
import com.gft.currencies.ui.R
import com.gft.currencies.ui.common.containers.CurrencyListDataHolder
import com.gft.currencies.ui.common.models.CurrencyInfo
import com.gft.currencies.ui.databinding.FragmentCurrencyListBinding
import com.gft.currencies.ui.screens.currencylist.CurrencyListViewEvent.OnNewCurrencyListDataSource
import com.gft.currencies.ui.screens.currencylist.CurrencyListViewEvent.OnSearchQueryUpdated
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class CurrencyListFragment : Fragment(), CurrencyListDataHolder {

    private inner class CurrencyListFragmentMenuProvider : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.currency_list_menu, menu)

            with(menu.findItem(R.id.search_item).actionView as SearchView) {
                queryHint = getString(R.string.currency_list_screen_search_menu_item_hint)

                setIconifiedByDefault(true)
                setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.onEvent(OnSearchQueryUpdated(newText.orEmpty()))

                        return true
                    }

                    override fun onQueryTextSubmit(query: String?) = true
                })
            }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }

    private val viewModel by viewModel<CurrencyListViewModel>()
    private val currencyListAdapter = CurrencyListAdapter()

    private lateinit var binding: FragmentCurrencyListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCurrencyListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.currenciesList.adapter = currencyListAdapter

        setupOptionsMenu()
        setupObservingViewStates()
    }

    override fun setCurrencyListDataSource(currencyListDataSource: Flow<List<CurrencyInfo>>) {
        viewModel.onEvent(OnNewCurrencyListDataSource(currencyListDataSource))
    }

    private fun setupOptionsMenu() {
        requireActivity().run {
            addMenuProvider(CurrencyListFragmentMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)
            invalidateMenu()
        }
    }

    private fun setupObservingViewStates() = handleViewStateUpdates(viewModel.viewStates) { viewState ->
        binding.run {
            currenciesList.visibility = if (viewState.isCurrenciesListVisible) VISIBLE else GONE
            noDataPlaceholder.visibility = if (viewState.isNoDataPlaceholderVisible) VISIBLE else GONE
            noSearchResultsPlaceholder.visibility = if (viewState.isNoSearchResultsPlaceholderVisible) VISIBLE else GONE
        }

        currencyListAdapter.submitList(viewState.currencyInfos)
    }
}
