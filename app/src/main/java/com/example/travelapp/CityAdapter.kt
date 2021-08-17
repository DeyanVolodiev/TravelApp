package com.example.travelapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.create_landmark_dialog.*
import kotlinx.android.synthetic.main.create_landmark_dialog.btnAddLandmark
import kotlinx.android.synthetic.main.create_landmark_dialog.btnAddLandmarkCancel
import kotlinx.android.synthetic.main.create_landmark_dialog.txtAddLandmarkName
import kotlinx.android.synthetic.main.edit_description_dialog.*
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter(
  private val context: Context,
  var cities: MutableList<City>,
  var landmarks: MutableList<Landmark>
) : RecyclerView.Adapter<CityAdapter.CitiesViewHolder>() {

  class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
    return CitiesViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.item_city,
        parent,
        false
      )
    )
  }

  companion object {
    const val CITY_DETAILED_TITLE = "title"
    const val CITY_ID = "id"
  }

  override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
    val currentCity = cities[position]
    val currentCityLandmarks =
      landmarks.filter { landmark -> landmark.cityId == currentCity.id } as MutableList<Landmark>

    holder.itemView.apply {
      tvCityName.text = currentCity.name
      tvCityDescription.text = currentCity.description

      /**
       * Takes user to another activity with the city's landmarks
       */
      btnToLandmarksActivity.setOnClickListener{
        val intent = Intent(
          context,
          CityDetailedActivity::class.java
        )
        intent.putExtra(CITY_DETAILED_TITLE, currentCity.name)
        intent.putExtra(CITY_ID,currentCity.id)

        context.startActivity(intent)
      }

      /***
       * Makes a popup that has input for editing the city's info
       */
      btnEditDescription.setOnClickListener {
        val updateDialog = Dialog(context)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.edit_description_dialog)

        updateDialog.btnAddLandmark.setOnClickListener {
          val name = updateDialog.txtEditCityName.text.toString()
          val description = updateDialog.txtEditCityDescription.text.toString()

          updateDialog.txtEditCityName.setText(currentCity.name)
          updateDialog.txtEditCityDescription.setText(currentCity.description)

          if (name.isNotEmpty() && description.isNotEmpty()) {
            val status =
              editCity(position, name, description, currentCity.areLandmarksVisible)
            if (status > -1) {
              Toast.makeText(context, "City Edited.", Toast.LENGTH_SHORT).show()

              updateDialog.dismiss() // Dialog will be dismissed
            }
          } else {
            updateDialog.txtEditCityNameLayout.error = "This field can not be blank"
            updateDialog.txtEditCityDescriptionLayout.error = "This field can not be blank"
          }
        }
        updateDialog.btnAddLandmarkCancel.setOnClickListener {
          updateDialog.dismiss()
        }
        updateDialog.show()
      }

      /***
       * Makes a popup that has inputs for creating a new landmark
       */
      btnAddLandmark.setOnClickListener {
        val updateDialog = Dialog(context)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.create_landmark_dialog)

        updateDialog.btnAddLandmark.setOnClickListener {
          val name = updateDialog.txtAddLandmarkName.text.toString()
          val description = updateDialog.txtAddLandmarkDescription.text.toString()

          if (name.isNotEmpty() && description.isNotEmpty()) {
            val status = addLandmark(name, description, currentCity.id)
            if (status > -1) {
              Toast.makeText(context, "Landmark Added.", Toast.LENGTH_SHORT).show()

              updateDialog.dismiss() // Dialog will be dismissed
            }
          } else {
            updateDialog.txtAddLandmarkNameLayout.error = "This field can not be blank"
            updateDialog.txtAddLandmarkDescription.error = "This field can not be blank"
          }
        }
        updateDialog.btnAddLandmarkCancel.setOnClickListener {
          updateDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        updateDialog.show()
      }

      /**
       * When clicked on city card - show landmarks list
       */
//      cityCard.setOnClickListener {
//        if (currentCityLandmarks.size > 0) {
//          if (!currentCity.areLandmarksVisible) {
//            txtLandMarksTitle.visibility = View.VISIBLE
//            rvLandmarks.visibility = View.VISIBLE
//
//            editCity(
//              position,
//              currentCity.name,
//              currentCity.description,
//              !currentCity.areLandmarksVisible
//            )
//          } else {
//            txtLandMarksTitle.visibility = View.GONE
//            rvLandmarks.visibility = View.GONE
//
//            editCity(
//              position,
//              currentCity.name,
//              currentCity.description,
//              !currentCity.areLandmarksVisible
//            )
//          }
//        }
//      }

      /**
       * Delete city (only if has no landmarks)
       */
      cityCard.setOnLongClickListener {
        if (currentCityLandmarks.size == 0) {
          MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.ConfirmDialogTitle))
            .setPositiveButton(resources.getString(R.string.PositiveButtonDelete)) { _, _ ->
              removeCity(currentCity, position)
            }
            .setNegativeButton(resources.getString(R.string.NegativeButton)) { dialog, _ ->
              dialog?.cancel()
            }
            .show()
        }
        true
      }
    }
  }

  override fun getItemCount(): Int {
    return cities.size
  }

  fun addCity(cityName: String): Long {
    val databaseHandler = DatabaseHandler(context)
    val status =
      databaseHandler.addCity(City(cities.lastIndex + 1, cityName))

    if (status > -1) {
      cities.add(City(cities.size, cityName))

      notifyItemInserted(cities.size - 1)
    }
    return status
  }

  private fun editCity(
    position: Int,
    newName: String,
    newDescription: String,
    newAreLandmarksVisible: Boolean
  ): Int {
    val databaseHandler = DatabaseHandler(context)
    val status =
      databaseHandler.updateCity(position, newName, newDescription)

    if (status > -1) {
      cities[position].name = newName
      cities[position].description = newDescription
      cities[position].areLandmarksVisible = newAreLandmarksVisible

      notifyItemChanged(position)
    }
    return status
  }

  private fun removeCity(cityToRemove: City, position: Int): Int {
    val databaseHandler = DatabaseHandler(context)
    val status =
      databaseHandler.deleteCity(cities[position])

    if (status > -1) {
      cities = cities.filter { city -> city.id != cityToRemove.id } as MutableList<City>

      notifyItemRemoved(cityToRemove.id)
    }
    return status
  }

  private fun addLandmark(name: String, description: String, cityId: Int): Long {
    val databaseHandler = DatabaseHandler(context)
    val status =
      databaseHandler.addLandmark(Landmark(landmarks.lastIndex + 1, name, description, cityId))

    if (status > -1) {
      landmarks.add(
        Landmark(landmarks.lastIndex + 1, name, description, cityId)
      )
    }
    return status
  }
}