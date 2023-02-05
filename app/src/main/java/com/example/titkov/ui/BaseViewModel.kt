package com.example.titkov.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/**
 * Base [ViewModel] implementation with some MVI utilities
 */
abstract class BaseViewModel<State, Event> : ViewModel(), EventConsumer<Event> {
    abstract val initialState: State

    abstract fun interceptThrowable(throwable: Throwable)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        interceptThrowable(throwable)
    }

    private val viewStateMutex = Mutex()
    protected val _viewState by lazy { MutableStateFlow(initialState) }
    val viewState by lazy { _viewState.asStateFlow() }

    @MviDsl
    protected fun reduce(
        f: suspend (prevState: State) -> State
    ) = execute {
        try {
            viewStateMutex.lock()
            val newState = f(_viewState.value)
            _viewState.value = newState
        } catch (throwable: Throwable) {
            throw throwable
        } finally {
            viewStateMutex.unlock()
        }
    }

    @MviDsl
    protected fun execute(block: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            block()
        }
    }
}

interface EventConsumer<Event> {
    fun consume(event: Event)
}

@DslMarker
annotation class MviDsl

val <State> BaseViewModel<State, *>.state: State
    get() = viewState.value
