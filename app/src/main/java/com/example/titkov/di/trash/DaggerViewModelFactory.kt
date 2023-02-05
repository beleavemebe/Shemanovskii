package com.example.titkov.di.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * [ViewModelProvider.Factory] implementation that constructs the needed [ViewModel] instance
 * via Dagger-generated [Provider]
 */
class DaggerViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers.getValue(modelClass as Class<out ViewModel>).get() as T
    }
}
