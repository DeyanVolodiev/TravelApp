package com.example.travelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_fragment.*

class BottomSheetFragment(Adapter: CityAdapter) : BottomSheetDialogFragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
  }

  private val adapter = Adapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    btnAddCity.setOnClickListener {
      val cityName = txtAddCityNameLayout.editText?.text.toString()

      if (cityName.isEmpty()) {
        txtAddCityNameLayout.error = getString(R.string.addCityError)
      } else {
        val status = adapter.addCity(cityName)

        if (status > -1) {
          Toast.makeText(context, "City Added To List", Toast.LENGTH_SHORT).show()
        }
        txtAddCityNameLayout.error = null
        txtAddCityName.text?.clear()

        dismiss()
      }
    }
  }
}