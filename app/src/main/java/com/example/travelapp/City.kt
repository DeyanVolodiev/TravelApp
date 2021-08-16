package com.example.travelapp

data class City(
  val id: Int,
  var name: String,
  var description: String = "A new City!",
  var areLandmarksVisible: Boolean = false,
)
