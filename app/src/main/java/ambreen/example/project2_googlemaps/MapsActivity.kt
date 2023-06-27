package ambreen.example.project2_googlemaps

import ambreen.example.project2_googlemaps.databinding.ActivityMapsBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var dataModelView : DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        dataModelView= ViewModelProvider(this)[DataViewModel::class.java]
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            val position = marker.position
            loadDataToDisplay(position.latitude, position.longitude)
         //   Toast.makeText(
         //       this,
         //       "Lat " + position.latitude + " "
         //               + "Long " + position.longitude,
         //       Toast.LENGTH_LONG
         //   ).show()
            false
        })
        val zoomLevel = 15f
        //1- world
        //5- landmass/ continent
        //10- city
        //15- street
        //20- buildings
        // Add a marker in London and move the camera
        val myLocation = LatLng(43.034725, -81.218443)
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.5f, Long: %2$.5f",
            myLocation.latitude,
            myLocation.longitude
        )
        loadDataToDisplay(myLocation.latitude,myLocation.longitude)

        map.addMarker(MarkerOptions().position(myLocation).title("Marker in Dylan St. London Ontario").snippet(snippet))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,zoomLevel))
        setMapLongClick(map)
        setPoiClick(map)
        val overlaySize = 100f
        val androidOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
            .position(myLocation, overlaySize)
        map.addGroundOverlay(androidOverlay)

        addRestaurantMarker(map)
    }

    // add marker for restaurant named formaggios pie shack
    private fun addRestaurantMarker(map: GoogleMap)
    {
        val zoomLevel = 15f

        val myLocation = LatLng(43.034568, -81.221892)
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.5f, Long: %2$.5f",
            myLocation.latitude,
            myLocation.longitude
        )
        loadDataToDisplay(myLocation.latitude,myLocation.longitude)

        map.addMarker(MarkerOptions().position(myLocation).title("Marker at Formaggios Pie Shack Restaurant").snippet(snippet))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,zoomLevel))
        val overlaySize = 100f
        val androidOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.restaurantimg))
            .position(myLocation, overlaySize)
        map.addGroundOverlay(androidOverlay)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setMapLongClick(map:GoogleMap){
        map.setOnMapLongClickListener { latlng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latlng.latitude,
                latlng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            println("Lat : ${latlng.latitude}")
            loadDataToDisplay(latlng.latitude,latlng.longitude)
        }
    }
    //point of interest
    private fun setPoiClick(map: GoogleMap){
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker?.showInfoWindow()
            println("Lat : ${poi.latLng.latitude}")
            loadDataToDisplay(poi.latLng.latitude,poi.latLng.longitude)
        }
    }

    private fun isPermissionGranted() : Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation (){
        if (isPermissionGranted()){
            map.isMyLocationEnabled = true
        }else
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }
    // get to enable your location
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                enableMyLocation()
            }
        }
    }

    // display latitude and longitude
    fun loadDataToDisplay(lat: Double, long: Double){
        // Declaring fragment manager from making data
        // transactions using the custom fragment

        val mFragmentManager: FragmentManager = supportFragmentManager
        val mFragmentTransaction : FragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = MapDetailFragment()
        val mBundle = Bundle()
        mBundle.putDouble("latitude",lat)
        mBundle.putDouble("longitude",long)
        mFragment.arguments = mBundle
        mFragmentTransaction.add(R.id.frameLayout, mFragment).commit()
        //dataModelView.setData(lat, long)
    }
}