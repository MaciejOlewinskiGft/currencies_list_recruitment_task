package com.gft.common.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <ViewState> Fragment.handleViewStateUpdates(
    viewStatesFlow: Flow<ViewState>,
    viewStateHandler: (ViewState) -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewStatesFlow.collect { viewState -> viewStateHandler(viewState) }
        }
    }
}

fun <ViewEffect> Fragment.handleViewEffects(
    viewEffectsFlow: Flow<OneTimeEvent<ViewEffect>?>,
    viewEffectHandler: (ViewEffect) -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewEffectsFlow.collect { viewEffectOneTimeEvent ->
                viewEffectOneTimeEvent?.getContent()?.let { viewEffect -> viewEffectHandler(viewEffect) }
            }
        }
    }
}

fun <NavigationEffect> Fragment.handleNavigationEffects(
    navigationEffectsFlow: Flow<OneTimeEvent<NavigationEffect>?>,
    navigationEffectHandler: (NavigationEffect) -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            navigationEffectsFlow.collect { navigationEffectOneTimeEvent ->
                navigationEffectOneTimeEvent?.getContent()?.let { navigationEffect -> navigationEffectHandler(navigationEffect) }
            }
        }
    }
}
