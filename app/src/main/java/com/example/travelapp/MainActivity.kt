package com.example.travelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_city.*

class MainActivity : AppCompatActivity() {
  private lateinit var cityAdapter: CityAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    cityAdapter = CityAdapter(this, getCitiesList(), getLandmarksList())

    areCitiesInList()

    val bottomSheetFragment = BottomSheetFragment(cityAdapter,this)
    fabAddCity.setOnClickListener {
      bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
    }

    /**
     * Used for testing
     */
    alabala.setOnClickListener {
      alabala.text = "${cityAdapter.cities} ${cityAdapter.landmarks}"
    }
  }

  fun areCitiesInList(){
    if (getCitiesList().size > 0) {
      tvCityListIsEmpty.visibility = View.INVISIBLE

      rvCityItems.layoutManager = LinearLayoutManager(this)

      rvCityItems.adapter = cityAdapter
    } else {
      tvCityListIsEmpty.visibility = View.VISIBLE
    }
  }

  /**
   * Function is used to get the City List from the database table.
   */
  private fun getCitiesList(): ArrayList<City> {
    val databaseHandler: DatabaseHandler = DatabaseHandler(this)

    return databaseHandler.viewCities()
  }

  /**
   * Function is used to get the Landmark List from the database table.
   */
  private fun getLandmarksList(): ArrayList<Landmark> {
    val databaseHandler: DatabaseHandler = DatabaseHandler(this)

    return databaseHandler.viewLandmarks()
  }
}