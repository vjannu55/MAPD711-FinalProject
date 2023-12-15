package com.codescafe.doctorappointment.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.models.UserModel
import com.codescafe.doctorappointment.utils.Utils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import com.skyfishjy.library.RippleBackground
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class SelectLocation : AppCompatActivity(), OnMapReadyCallback {

    var mMap: GoogleMap? = null
    var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var placesClient: PlacesClient? = null
    var predictionList: List<AutocompletePrediction>? = null
    val PROXIMITY_RADIUS = 10000
    var mLastKnownLocation: Location? = null
    var locationCallback: LocationCallback? = null
    var materialSearchBar: MaterialSearchBar? = null
    var mapView: View? = null
    var btnFind: Button? = null
    var rippleBg: RippleBackground? = null
    var Selected_Add: TextView? = null
    val DEFAULT_ZOOM = 15f
    val select = ""
    var password: EditText? = null
    val gas_station = "mosque"
    var state = ""

    var activity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)
        activity = this


        password = findViewById<EditText?>(R.id.password)
        materialSearchBar = findViewById<MaterialSearchBar?>(R.id.searchBar)
        btnFind = findViewById<android.widget.Button?>(R.id.btn_find)
        Selected_Add = findViewById<TextView?>(R.id.location)
        rippleBg = findViewById<RippleBackground?>(R.id.ripple_bg)

        password!!.setOnEditorActionListener(TextView.OnEditorActionListener({ v: TextView?, actionId: Int, event: KeyEvent? ->
            state = password!!.getText().toString().trim({ it <= ' ' })
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //okhhtp(gas_station);
            }
            false
        }))

        var mapFragment =
            getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapView = mapFragment.getView()

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(activity, getString(R.string.google_maps_api))
        placesClient = Places.createClient(this)
        val token = AutocompleteSessionToken.newInstance()
        materialSearchBar!!.setOnSearchActionListener(object : OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {}
            override fun onSearchConfirmed(text: CharSequence) {
                startSearch(text.toString(), true, null, true)
            }

            override fun onButtonClicked(buttonCode: Int) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar!!.disableSearch()
                }
            }
        })
        materialSearchBar!!.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val predictionsRequest = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(s.toString())
                    .build()
                placesClient!!.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(
                    OnCompleteListener<FindAutocompletePredictionsResponse?> { task ->
                        if (task.isSuccessful) {
                            Log.e("mytag", "successful")
                            val predictionsResponse = task.result
                            if (predictionsResponse != null) {
                                Log.e("mytag", "no null")
                                predictionList = predictionsResponse.autocompletePredictions
                                val suggestionsList: MutableList<String> = ArrayList()
                                for (i in (predictionList as MutableList<AutocompletePrediction>).indices) {
                                    Log.e("mytag", "id  " + (predictionList as MutableList<AutocompletePrediction>).get(i).getPlaceId())
                                    val prediction: AutocompletePrediction = (predictionList as MutableList<AutocompletePrediction>).get(i)
                                    // if (predictionList.get(i).getPlaceTypes().equals(gas_station)){
                                    suggestionsList.add(prediction.getFullText(null).toString())
                                    // }
                                }
                                materialSearchBar!!.updateLastSuggestions(suggestionsList)
                                if (!materialSearchBar!!.isSuggestionsVisible()) {
                                    materialSearchBar!!.showSuggestionsList()
                                }
                            }
                        } else {
                            Log.e("mytag", "prediction fetching task unsuccessful")
                        }
                    }).addOnFailureListener(OnFailureListener { e ->
                    Log.e(
                        "mytag",
                        "Exception   $e"
                    )
                })
            }

            override fun afterTextChanged(s: Editable) {}
        })
        materialSearchBar!!.setSuggstionsClickListener(object :
            SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemClickListener(position: Int, v: View) {
                if (position >= predictionList!!.size) {
                    return
                }
                val selectedPrediction: AutocompletePrediction = predictionList!!.get(position)
                val suggestion: String =
                    materialSearchBar!!.getLastSuggestions().get(position).toString()
                materialSearchBar!!.setText(suggestion)
                Handler().postDelayed({ materialSearchBar!!.clearSuggestions() }, 1000)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(
                    materialSearchBar!!.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
                val placeId = selectedPrediction.placeId
                val placeFields = listOf(Place.Field.LAT_LNG)
                val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
                placesClient!!.fetchPlace(fetchPlaceRequest)
                    .addOnSuccessListener(OnSuccessListener<FetchPlaceResponse> { fetchPlaceResponse ->
                        val place = fetchPlaceResponse.place
                        Log.i("mytag", "Place found: " + place.name)
                        val latLngOfPlace = place.latLng
                        if (latLngOfPlace != null) {
                            mMap!!.addMarker(
                                MarkerOptions().position(latLngOfPlace).title(suggestion)
                            )
                            mMap!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLngOfPlace,
                                    DEFAULT_ZOOM
                                )
                            )
                            val builder = AlertDialog.Builder(activity as SelectLocation)
                            val geoCoder = Geocoder(getBaseContext(), Locale.getDefault())
                            try {
                                val addresses = geoCoder.getFromLocation(
                                    latLngOfPlace.latitude,
                                    latLngOfPlace.longitude,
                                    1
                                )
                                var add = ""
                                if (addresses!!.size > 0) {
                                    for (address in addresses) {
                                        add += address.getAddressLine(address.maxAddressLineIndex) + "\n"
                                        add += address.countryName + "\n"
                                        add += address.locality + "\n"
                                        add += address.subLocality + "\n\n"
                                        val l = address.locality
                                        if (l != null) {
                                            val address_list = ArrayList<String>()
                                            address_list.add(address.getAddressLine(address.maxAddressLineIndex))
                                            address_list.add(address.countryName)
                                            address_list.add(address.locality)
                                            address_list.add(address.subLocality)
                                            address_list.add("" + latLngOfPlace.latitude)
                                            address_list.add("" + latLngOfPlace.longitude)
                                            Utils.Submit_Address = address_list
                                            // Get_AllNearByFuelPoints(latLngOfPlace.latitude, latLngOfPlace.longitude, address.getLocality());
                                        }
                                    }
                                    builder.setMessage(
                                        """
                                $suggestion
                                
                                $add
                                """.trimIndent()
                                    )
                                    //     builder.show();
                                }
                            } catch (e1: IOException) {
                                e1.printStackTrace()
                                builder.setMessage(e1.toString())
                            }
                            //builder.show();
                        }
                    })
                    .addOnFailureListener(OnFailureListener { e ->
                        if (e is ApiException) {
                            val apiException = e
                            apiException.printStackTrace()
                            val statusCode = apiException.statusCode
                            Log.e("mytag", "place not found: " + e.message)
                            Log.e("mytag", "status code: $statusCode")
                            Utils.setToast(activity, "Place Not Found, Try Another")
                        }
                    })
            }

            override fun OnItemDeleteListener(position: Int, v: View) {}
        })
        btnFind!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (Utils.Submit_Address != null) {
                    (activity as SelectLocation).onBackPressed()
                    finish()
                } else {
                    (activity as SelectLocation).onBackPressed()
                    Utils.setToast(activity, "You No Select Any Location")
                }
            }
        })
    }

    override fun onBackPressed() {
        if (Utils.Submit_Address != null) {
            finish()
        } else {
            Utils.Submit_Address = null
            Utils.setToast(activity, "You Select Any Location")
            super.onBackPressed()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.setMyLocationEnabled(true)
        mMap!!.getUiSettings().setMyLocationButtonEnabled(true)
        mMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap!!.setOnMapClickListener(OnMapClickListener { latLng ->
            val geoCoder = Geocoder(getBaseContext(), Locale.getDefault())
            try {
                val addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                var add = ""
                if (addresses!!.size > 0) {
                    for (address in addresses) {
                        add += address.getAddressLine(address.maxAddressLineIndex) + "\n"
                        val address_list = ArrayList<String>()
                        address_list.add(address.getAddressLine(address.maxAddressLineIndex))
                        address_list.add("" + latLng.latitude)
                        address_list.add("" + latLng.longitude)
                        Utils.Submit_Address = address_list
                        Selected_Add!!.setText(add)
                    }
                }
                // Utils.setToast(activity,add+"\n"+marker.getTitle());
            } catch (e: IOException) {
                e.printStackTrace()
            }
            // Utils.setToast(activity,latLng.toString());
        })
        mMap!!.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            val latLng = marker.position
            marker.title
            val geoCoder = Geocoder(getBaseContext(), Locale.getDefault())
            try {
                val addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                var add = ""
                if (addresses!!.size > 0) {
                    for (address in addresses) {
                        add += address.getAddressLine(address.maxAddressLineIndex) + "\n"
                        add += "" + latLng.latitude + "\n"
                        add += "" + latLng.longitude + "\n"
                        val address_list = ArrayList<String?>()
                        address_list.add(address.getAddressLine(address.maxAddressLineIndex))
                        address_list.add("" + latLng.latitude)
                        address_list.add("" + latLng.longitude)
                        address_list.add(marker.title)
                        Utils.Submit_Address = address_list
                        Selected_Add!!.setText(marker.title)
                    }
                }
                // Utils.setToast(activity,add+"\n"+marker.getTitle());
            } catch (e: IOException) {
                e.printStackTrace()
            }
            false
        })
        mMap!!.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {}
            override fun onMarkerDragStart(marker: Marker) {}
        })
        if (mapView != null && mapView!!.findViewById<View>("1".toInt()) != null) {
            Log.e("mytag", "mapnonull ")
            val locationButton = (mapView!!.findViewById<View>("1".toInt())
                .getParent() as View).findViewById<View>("2".toInt())
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 40, 180)
        }
        val locationRequest = LocationRequest.create()
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener(this,
            OnSuccessListener<LocationSettingsResponse?> { getDeviceLocation() })
        task.addOnFailureListener(this, OnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, 51)
                } catch (e1: SendIntentException) {
                    e1.printStackTrace()
                }
            }
        })
        mMap!!.setOnMyLocationButtonClickListener(OnMyLocationButtonClickListener {
            if (materialSearchBar!!.isSuggestionsVisible()) materialSearchBar!!.clearSuggestions()
            if (materialSearchBar!!.isSearchEnabled()) materialSearchBar!!.disableSearch()
            false
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 51) {
            if (resultCode == Activity.RESULT_OK) {
                getDeviceLocation()
            }
        }
    }

    var list: ArrayList<UserModel> = ArrayList<UserModel>()

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        mFusedLocationProviderClient!!.getLastLocation()
            .addOnCompleteListener(OnCompleteListener<Location> { task ->
                if (task.isSuccessful) {
                    Log.e("mytag", "getDeviceLocation ")
                    mLastKnownLocation = task.result
                    if (mLastKnownLocation != null) {
                        Log.e("mytag", "mLastKnownLocation ")
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mLastKnownLocation!!.getLatitude(),
                                    mLastKnownLocation!!.getLongitude()
                                ), DEFAULT_ZOOM
                            )
                        )
                        val latLng =
                            LatLng(
                                mLastKnownLocation!!.getLatitude(),
                                mLastKnownLocation!!.getLongitude()
                            )
                        mMap!!.addMarker(
                            MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        )
                        currentLat = "" + mLastKnownLocation!!.getLatitude()
                        currentLong = "" + mLastKnownLocation!!.getLongitude()

                        Log.e(
                            "mytag",
                            "value " + mLastKnownLocation!!.getLatitude() + mLastKnownLocation!!.getLongitude()
                        )
                        val builder = AlertDialog.Builder(this)
                        val geoCoder = Geocoder(getBaseContext(), Locale.getDefault())
                        try {
                            val addresses =
                                geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                            //   List<Address> addresses = geoCoder.getFromLocationName(suggestion, 1);
                            var add = ""
                            if (addresses!!.size > 0) {
                                for (address in addresses) {
                                    add += address.getAddressLine(address.maxAddressLineIndex) + "\n"
                                    materialSearchBar!!.setText(add)
                                    materialSearchBar!!.enableSearch()
                                    add += address.countryName + "\n"
                                    add += address.locality + "\n"
                                    add += address.subLocality + "\n\n"
                                    //Get_AllNearByFuelPoints(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), state);
                                    val address_list = ArrayList<String>()
                                    address_list.add(address.getAddressLine(address.maxAddressLineIndex))
                                    address_list.add(address.countryName)
                                    address_list.add(address.locality)
                                    address_list.add(address.subLocality)
                                    address_list.add("" + latLng.latitude)
                                    address_list.add("" + latLng.longitude)
                                    Utils.Submit_Address = address_list
                                }
                            }
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                            builder.setMessage(e1.toString())
                        }
                    } else {
                        Log.e("mytag", "else ")
                        val locationRequest = LocationRequest.create()
                        locationRequest.setInterval(10000)
                        locationRequest.setFastestInterval(5000)
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)
                                if (locationResult == null) {
                                    Log.e("mytag", "locationResult null ")
                                    return
                                }
                                mLastKnownLocation = locationResult.lastLocation
                                val latLng = LatLng(
                                    mLastKnownLocation!!.getLatitude(),
                                    mLastKnownLocation!!.getLongitude()
                                )
                                mMap!!.addMarker(MarkerOptions().position(latLng))
                                mMap!!.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            mLastKnownLocation!!.getLatitude(),
                                            mLastKnownLocation!!.getLongitude()
                                        ), DEFAULT_ZOOM
                                    )
                                )
                                locationCallback?.let {
                                    mFusedLocationProviderClient!!.removeLocationUpdates(
                                        it
                                    )
                                }

                            }
                        }
                        mFusedLocationProviderClient!!.requestLocationUpdates(
                            locationRequest,
                            locationCallback as LocationCallback,
                            null
                        )
                    }
                } else {
                    Toast.makeText(activity, "unable to get Shop", Toast.LENGTH_SHORT).show()
                }
            })
    }


    var responses: String? = null
    var currentLat: String? = null
    var currentLong:kotlin.String? = null

    private fun okhhtp(gas_station: String) {
        val url =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location=" + currentLat + "," + currentLong +
                    "&radius=" + PROXIMITY_RADIUS + "&type=" +
                    gas_station + "&sensor=true" + "&key=" + getResources().getString(R.string.google_maps_api)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        Log.e("mytag", "okhttp  ")
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("mytag", "IOException  $e")
                //    Toast.makeText(getlicationContext(),"eror"+e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread(Runnable {
                        try {
                            responses = response.body()!!.string()
                            Log.e("val", "onResponse: $responses")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        //Log.e("mytag","isSuccessful  "+responses);
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = JSONObject(responses)
                            val ss = jsonObject.getJSONArray("results").toString()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })
                } else {
                    Log.e("mytag", "noSuccessful  $response")
                }
            }
        })
    }

    private fun getUrl(latitude: Double, longitude: Double, nearbyPlace: String): String {
        val googlePlaceUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlaceUrl.append("location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=$PROXIMITY_RADIUS")
        googlePlaceUrl.append("&type=$nearbyPlace")
        googlePlaceUrl.append("&sensor=true")
        googlePlaceUrl.append("&key=" + getString(R.string.google_maps_api))
        Log.d("MapsActivity", "url = $googlePlaceUrl")
        return googlePlaceUrl.toString()
    }
}