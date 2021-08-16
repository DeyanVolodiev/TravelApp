package com.example.travelapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.item_city.view.*
import kotlinx.android.synthetic.main.item_landmark.view.*


class LandmarksAdapter(
  private var parentAdapter: CityAdapter,
  private var landmarks: MutableList<Landmark>
) : RecyclerView.Adapter<LandmarksAdapter.LandmarksViewHolder>() {

  class LandmarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarksViewHolder {
    return LandmarksViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.item_landmark,
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: LandmarksViewHolder, position: Int) {
    val currentLandmark = landmarks[position]

    holder.itemView.apply {
      txtLandmarkName.text = currentLandmark.name
      txtLandmarkDescription.text = currentLandmark.description

      /**
       * Delete landmark
       */
      landmarkItem.setOnLongClickListener {
        MaterialAlertDialogBuilder(context)
          .setTitle(resources.getString(R.string.ConfirmDialogTitle))
          .setPositiveButton(resources.getString(R.string.PositiveButtonDelete)) { dialog, which ->
            landmarks =
              landmarks.filter { landmark -> landmark != currentLandmark } as MutableList<Landmark>
            parentAdapter.removeLandmark(currentLandmark)
          }
          .setNegativeButton(resources.getString(R.string.NegativeButton)) { dialog, which ->
            dialog?.cancel()
          }
          .show()
        true
      }
    }
  }

  override fun getItemCount(): Int {
    return landmarks.size
  }
}