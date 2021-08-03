package com.summit.summitproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.summit.summitproject.R
import com.summit.summitproject.ui.screens.login.LoginFragment

/**
 * The first "screen" of our app. [MainActivity] will host a container view [R.id.main_container]
 * which will host our Fragments and composable screens within them.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Called the first time an Activity is created, but before any UI is shown to the user.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Define the layout for this activity
         */
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            /**
             * Immediately transition to the LoginFragment, since that is our default initial screen.
             * But make sure we only do this when there was no screen previously.
             */
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, LoginFragment())
                .commit()
        }
    }
}