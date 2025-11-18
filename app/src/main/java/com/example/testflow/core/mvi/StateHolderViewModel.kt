package com.example.testflow.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class StateHolderViewModel<S : Any, T : Any>(initial: S) : ViewModel() {

    private val _trigger by lazy { MutableSharedFlow<T>() }

    private var _state = initial
    val state: StateFlow<S> = flow {
        onDataDemanded()
        merge(_trigger, dataFlow).collect { data ->
            handleDataUpdates(data)
            emit(_state)
        }
    }.distinctUntilChanged().stateIn(
        viewModelScope,
        // Flow will become active when the first subscriber appear.
        // Will keep Flow active for 5 seconds when all subscribers (usually single subscriber) unsubscribe.
        // Google recommends to use 5 seconds which enough for Activity recreation.
        SharingStarted.WhileSubscribed(5_000),
        _state,
    )

    protected abstract val dataFlow: Flow<Any>

    /**
     * Handles triggers and data from use cases
     */
    protected abstract fun handleDataUpdates(data: Any)

    protected open suspend fun onDataDemanded() {
        // no-op
    }

    protected fun sendTrigger(trigger: T) {
        viewModelScope.launch {
            _trigger.emit(trigger)
        }
    }

    protected fun updateState(transform: (S) -> S) {
        _state = transform(_state)
    }
}
