package com.example.travelapp

import android.annotation.SuppressLint
import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
  SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
  companion object {
    private const val DATABASE_VERSION = 17
    private const val DATABASE_NAME = "TravelAppDatabase"

    private const val TABLE_CITIES = "CITIES"

    private const val C_KEY_ID = "_id"
    private const val C_KEY_NAME = "name"
    private const val C_KEY_DESCRIPTION = "description"
    private const val C_KEY_ARE_LANDMARKS_VISIBLE = "are_landmarks_visible"

    private const val TABLE_LANDMARKS = "LANDMARKS"

    private const val L_KEY_ID = "_id"
    private const val L_KEY_NAME = "name"
    private const val L_KEY_DESCRIPTION = "description"
    private const val L_KEY_CITY_ID = "city_id"
  }

  override fun onCreate(db: SQLiteDatabase?) {
    val createCitiesTable = "CREATE TABLE $TABLE_CITIES(" +
        "$C_KEY_ID INTEGER PRIMARY KEY," +
        "$C_KEY_NAME TEXT," +
        "$C_KEY_DESCRIPTION TEXT," +
        "$C_KEY_ARE_LANDMARKS_VISIBLE INTEGER" +
        ");"
    val createLandmarksTable = "CREATE TABLE $TABLE_LANDMARKS(" +
        "$L_KEY_ID INTEGER PRIMARY KEY," +
        "$L_KEY_NAME TEXT," +
        "$L_KEY_DESCRIPTION TEXT," +
        "$L_KEY_CITY_ID INTEGER," +
        "FOREIGN KEY($L_KEY_CITY_ID) REFERENCES $TABLE_CITIES($C_KEY_ID))"

    db?.execSQL(createCitiesTable)
    db?.execSQL(createLandmarksTable)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CITIES")
    db.execSQL("DROP TABLE IF EXISTS $TABLE_LANDMARKS")
    onCreate(db)
  }

  /**
   * Function to insert city record
   */
  fun addCity(city: City): Long {
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(C_KEY_NAME, city.name)
    contentValues.put(C_KEY_DESCRIPTION, city.description)
    contentValues.put(C_KEY_ARE_LANDMARKS_VISIBLE, 0)


    val result = db.insert(TABLE_CITIES, null, contentValues)

    db.close()
    return result
  }

  /**
   * Method to read the records from cities table in form of ArrayList
   */
  @SuppressLint("Range")
  fun viewCities(): ArrayList<City> {
    val cityList: ArrayList<City> = ArrayList<City>()

    // Query to select all the records from the table.
    val selectQuery = "SELECT * FROM $TABLE_CITIES"
    val db = this.readableDatabase

    var cursor: Cursor? = null
    try {
      cursor = db.rawQuery(selectQuery, null)
    } catch (e: SQLiteException) {
      db.execSQL(selectQuery)
      return ArrayList()
    }

    var id: Int
    var name: String
    var description: String

    if (cursor.moveToFirst()) {
      do {
        id = cursor.getInt(cursor.getColumnIndex(C_KEY_ID))
        name = cursor.getString(cursor.getColumnIndex(C_KEY_NAME))
        description = cursor.getString(cursor.getColumnIndex(C_KEY_DESCRIPTION))

        val city = City(id, name, description)
        cityList.add(city)
      } while (cursor.moveToNext())
    }
    return cityList
  }

  /**
   * Function to update city record
   */
  fun updateCity(id: Int, newCityName: String, newCityDescription: String, newAreLandmarksVisible: Int = 0): Int {
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(C_KEY_NAME, newCityName)
    contentValues.put(C_KEY_DESCRIPTION, newCityDescription)
    contentValues.put(C_KEY_ARE_LANDMARKS_VISIBLE, newAreLandmarksVisible)

    // Updating Row
    val result = db.update(TABLE_CITIES, contentValues, "$C_KEY_ID=${id}", null)

    db.close()
    return result
  }

  /**
   * Function to delete city record
   */
  fun deleteCity(citiToDelete: City): Int {
    val db = this.writableDatabase
    val contentValues = ContentValues()
    contentValues.put(C_KEY_ID, citiToDelete.id)

    // Deleting Row
    val result = db.delete(TABLE_CITIES, "$C_KEY_ID=${citiToDelete.id}", null)

    db.close()
    return result
  }

  /**
   * Function to insert landmark record
   */
  fun addLandmark(landmark: Landmark): Long {
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(L_KEY_NAME, landmark.name)
    contentValues.put(L_KEY_DESCRIPTION, landmark.description)
    contentValues.put(L_KEY_CITY_ID, landmark.cityId)

    val result = db.insert(TABLE_LANDMARKS, null, contentValues)

    db.close()
    return result
  }


  /**
   * Method to read the records from landmarks table in form of ArrayList
   */
  @SuppressLint("Range")
  fun viewLandmarks(): ArrayList<Landmark> {
    val landmarkList: ArrayList<Landmark> = ArrayList<Landmark>()

    // Query to select all the records from the table.
    val selectQuery = "SELECT * FROM $TABLE_LANDMARKS";
    val db = this.readableDatabase

    var cursor: Cursor? = null
    try {
      cursor = db.rawQuery(selectQuery, null)
    } catch (e: SQLiteException) {
      db.execSQL(selectQuery)
      return ArrayList()
    }

    var id: Int
    var name: String
    var description: String
    var cityId: Int

    if (cursor.moveToFirst()) {
      do {
        id = cursor.getInt(cursor.getColumnIndex(L_KEY_ID))
        name = cursor.getString(cursor.getColumnIndex(L_KEY_NAME))
        description = cursor.getString(cursor.getColumnIndex(L_KEY_DESCRIPTION))
        cityId = cursor.getInt(cursor.getColumnIndex(L_KEY_CITY_ID))

        val landmark = Landmark(id, name, description, cityId)

        landmarkList.add(landmark)
      } while (cursor.moveToNext())
    }
    return landmarkList
  }

  /**
   * Function to delete landmark record
   */
  fun deleteLandmark(landmarkToDelete: Landmark): Int {
    val db = this.writableDatabase
    val contentValues = ContentValues()
    contentValues.put(L_KEY_ID, landmarkToDelete.id)

    // Deleting Row
    val result = db.delete(TABLE_LANDMARKS, "$L_KEY_ID=${landmarkToDelete.id}", null)

    db.close()
    return result
  }
}