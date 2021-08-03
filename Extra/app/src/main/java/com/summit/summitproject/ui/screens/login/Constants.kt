package com.summit.summitproject.ui.screens.login

import android.content.SharedPreferences
import com.summit.summitproject.prebuilt.model.AccountInfo

// Name to used specify our preference file.
const val SHARED_PREFERENCES_NAME = "app_shared_preference"

/**
 * The keys under which each field of the [AccountInfo] will be stored in the [SharedPreferences].
 *
 * @see AccountInfo
 */

const val PREF_NAME = "name"

const val PREF_CARD_LAST_FOUR = "card_last_4"

const val PREF_TRANSACTIONS = "transactions"