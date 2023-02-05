package com.example.titkov.di.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.titkov.ui.feature.film_list.FilmListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

//@Module
interface ViewModelsModule {
//    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

//    @[Binds IntoMap ViewModelKey(FilmListViewModel::class)]
    fun bindFilmListViewModel(vm: FilmListViewModel): ViewModel
}
