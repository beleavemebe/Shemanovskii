package com.example.titkov.di

import android.content.Context
import com.example.titkov.di.trash.ViewModelsModule
import com.example.titkov.ui.feature.film_details.FilmDetailsFragment
import com.example.titkov.ui.feature.film_list.FilmListFragment
import dagger.*
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {
    fun inject(filmListFragment: FilmListFragment)
    fun inject(filmDetailsFragment: FilmDetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
