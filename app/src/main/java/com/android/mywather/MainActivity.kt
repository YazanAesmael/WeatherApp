package com.android.mywather

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.mywather.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    companion object {
        const val log = "log420"
        const val myRequestCode = 2
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val api: String = "YOUR API ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(binding.root)
        supportActionBar?.hide()
        getLocation()
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun getLocation() {
        if (checkPermission()) {
            var latWeather: String
            var lonWeather: String
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    Log.d(log, "fusedLocationClient successful")
                    val location: Location? = task.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val list: List<Address> =
                        geocoder.getFromLocation(location!!.latitude, location.longitude, 1)

                    lonWeather = "${list[0].longitude}"
                    latWeather = "${list[0].latitude}"
                    Log.d(log, "$lonWeather, $latWeather")
                    executor(lonWeather, latWeather)
                }
            }
        }else {
            requestPermissions()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun executor(lonWeather: String, latWeather: String) {
        executor.execute {
            handler.post {
                binding.pbLoader.visibility = View.VISIBLE
                binding.mainContainer.visibility = View.GONE
                binding.tvError.visibility = View.GONE
            }
            try {
                val response: String? = try {
                    URL("https://api.openweathermap.org/data/2.5/weather?lat=${latWeather}&lon=${lonWeather}&appid=${api}&metric=true").readText(
                        Charsets.UTF_8
                    )
                }catch (e: Exception) {
                    null
                }
                handler.post {
                    val jsonObj = JSONObject(response!!)
                    val main = jsonObj.getJSONObject("main")
                    val sys = jsonObj.getJSONObject("sys")
                    val wind = jsonObj.getJSONObject("wind")
                    val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                    val updatedAt:Long = jsonObj.getLong("dt")
                    val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                    val temp = main.getString("temp")
                    val temperature = temp.substring(0,3)+" ℉"
                    val tempMin = main.getString("temp_min")
                    val tempMinTest = "Minimum Temp: "+tempMin.substring(0,3)+" ℉"
                    val tempMax = main.getString("temp_max")
                    val tempMaxTest = "Maximum Temp: "+tempMax.substring(0,3)+" ℉"
                    val pressure = main.getString("pressure")
                    val humidity = main.getString("humidity")

                    val sunrise:Long = sys.getLong("sunrise")
                    val sunset:Long = sys.getLong("sunset")
                    val windSpeed = wind.getString("speed")
                    val weatherDescription = weather.getString("description")

                    val address = jsonObj.getString("name")+", "+sys.getString("country")

                    binding.tvAddress.text = address
                    binding.tvUpdatedAt.text = updatedAtText
                    binding.tvStatus.text = weatherDescription
                    binding.tvTemp.text = temperature
                    binding.tvMaxTemp.text = tempMaxTest
                    binding.tvMinTemp.text = tempMinTest
                    binding.tvSunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                    binding.tvSunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                    binding.tvWind.text = "${windSpeed}KM/h"
                    binding.tvPressure.text = "$pressure Pa"
                    binding.tvHumidity.text = "${humidity}%"

                    binding.pbLoader.visibility = View.GONE
                    binding.mainContainer.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    binding.pbLoader.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
            ),
            myRequestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == myRequestCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
}












