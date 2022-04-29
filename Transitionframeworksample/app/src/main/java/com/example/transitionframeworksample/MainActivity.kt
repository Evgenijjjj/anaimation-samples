package com.example.transitionframeworksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.example.transitionframeworksample.items.ItemsFragment

class MainActivity : AppCompatActivity(), NavigationActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showFragment(ItemsFragment(), "ItemsFragmentTag")
    }

    override fun showFragment(fragment: Fragment, tag: String) {
        if (!supportFragmentManager.popBackStackImmediate(tag, 0)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

    override fun showFragment(
        from: Fragment,
        to: Fragment,
        transitions: List<Pair<View, String>>,
        tag: String
    ) {
        if (!supportFragmentManager.popBackStackImmediate(tag, 0)) {
            from.sharedElementReturnTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.default_transition)
            from.exitTransition = TransitionInflater.from(this)
                .inflateTransition(android.R.transition.no_transition)

            to.sharedElementEnterTransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.default_transition)
            to.enterTransition = TransitionInflater.from(this)
                .inflateTransition(android.R.transition.no_transition)

            val transaction = supportFragmentManager.beginTransaction()
                .replace(R.id.container, to, tag)
                .addToBackStack(tag)

            for (transition in transitions) {
                transaction.addSharedElement(transition.first, transition.second)
            }

            transaction.commit()
        }
    }

    private fun handleBackClick() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        handleBackClick()
    }
}

interface NavigationActivity {

    fun showFragment(fragment: Fragment, tag: String)

    fun showFragment(
        from: Fragment,
        to: Fragment,
        transitions: List<Pair<View, String>>,
        tag: String
    )
}