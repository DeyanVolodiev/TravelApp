package com.example.travelapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.acivity_city_landmarks.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.tvCityListIsEmpty

class CityDetailedActivity() : AppCompatActivity() {
  private lateinit var landmarksAdapter: LandmarksAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.acivity_city_landmarks)

    val title = intent.getStringExtra(CityAdapter.CITY_DETAILED_TITLE)
    supportActionBar?.title = title

    val cityId = intent.getIntExtra(CityAdapter.CITY_ID, 0)

    val landmarks = getCityLandmarksList(cityId)

    landmarksAdapter = LandmarksAdapter(this, landmarks)

    if (landmarks.size > 0) {
      tvNoLandmarksYet.visibility = View.INVISIBLE

      rvLandmarksList.layoutManager = LinearLayoutManager(this)

      rvLandmarksList.adapter = landmarksAdapter
    } else {
      tvNoLandmarksYet.visibility = View.VISIBLE
    }

    Log.e("id", "$cityId")
    Log.e("lands", "$landmarks")
  }

  private fun getCityLandmarksList(id: Int): MutableList<Landmark> {
    val databaseHandler: DatabaseHandler = DatabaseHandler(this)

    val allLandmarks = databaseHandler.viewLandmarks()
    return allLandmarks.filter { landmark -> landmark.cityId == id } as MutableList<Landmark>
  }
}