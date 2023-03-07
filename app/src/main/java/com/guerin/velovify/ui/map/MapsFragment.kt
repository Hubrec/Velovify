package com.guerin.velovify.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.guerin.velovify.R
import com.guerin.velovify.StationViewModel
import com.guerin.velovify.databinding.FragmentMapBinding

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: StationViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapBinding.inflate(layoutInflater)
        val root: View = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        Log.i("MAP", "mapFragment : $mapFragment")
        mapFragment.getMapAsync(this)

        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val lyon = LatLng(45.7641443,4.8528621)
        val redMakrer = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        val blueMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        val greenMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12.5f))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lyon))

        viewModel = ViewModelProvider(this)[StationViewModel::class.java]
        viewModel.getAllStations()
        viewModel.observeStationsLiveData().observe(this, Observer { stationList ->
            stationList.forEach {
                val station = LatLng(it.position.latitude, it.position.longitude)
                val desc = it.name + " bikes : " + it.totalStands.availabilities.bikes + ", stands : " + it.totalStands.availabilities.stands
                if (it.totalStands.availabilities.bikes == 0) {
                    mMap.addMarker(MarkerOptions().position(station).title(desc).icon(redMakrer))
                } else if (it.totalStands.availabilities.stands == 0) {
                    mMap.addMarker(MarkerOptions().position(station).title(desc).icon(blueMarker))
                } else {
                    mMap.addMarker(MarkerOptions().position(station).title(desc).icon(greenMarker))
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}