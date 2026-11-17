package com.wherepaid.app

import android.content.Context
import androidx.core.content.edit

/** 첫 실행 온보딩(S-01)을 끝냈는지 기억한다. 앱을 지우거나 데이터를 지우면 다시 보인다. */
class OnboardingStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("app_state", Context.MODE_PRIVATE)

    val isDone: Boolean get() = prefs.getBoolean(KEY_DONE, false)

    fun markDone() = prefs.edit { putBoolean(KEY_DONE, true) }

    private companion object {
        const val KEY_DONE = "onboarding_done"
    }
}
