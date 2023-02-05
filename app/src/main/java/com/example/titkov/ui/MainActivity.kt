package com.example.titkov.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.titkov.R
import com.example.titkov.ui.feature.film_details.FilmDetailsFragment
import com.example.titkov.ui.feature.film_list.FilmListFragment

class MainActivity : FragmentActivity(R.layout.activity_main), AppNavigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container_view, FilmListFragment())
            }
        }
    }

    override fun goToFilmDetails(filmId: Int) {
        supportFragmentManager.commit {
            add(R.id.fragment_container_view, FilmDetailsFragment.newInstance(filmId))
            addToBackStack(null)
        }
    }

    override fun exitFilmDetails() {
        supportFragmentManager.popBackStack()
    }
}
