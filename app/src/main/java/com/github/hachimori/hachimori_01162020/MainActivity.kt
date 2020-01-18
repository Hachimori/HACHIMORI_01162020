package com.github.hachimori.hachimori_01162020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.hachimori.hachimori_01162020.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
