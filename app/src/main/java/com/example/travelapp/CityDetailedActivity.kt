package com.example.travelapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CityDetailedActivity() : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.acivity_city_landmarks)

    val title = intent.getStringExtra(CityAdapter.CITY_DETAILED_TITLE)
    supportActionBar?.title = title
  }
}