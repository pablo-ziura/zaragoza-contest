package com.zaragoza.contest.ui.fragment.menu.game

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.zaragoza.contest.databinding.FragmentBonusQuestionMapBinding

class BonusQuestionMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentBonusQuestionMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var bonusQuestionMap: MapView

    private var userMarker: Marker? = null
    private var secretMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBonusQuestionMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bonusQuestionMap = binding.bonusQuestionMapFragment
        bonusQuestionMap.onCreate(savedInstanceState)
        bonusQuestionMap.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val secretPlace = LatLng(41.654094, -0.876687)
        val zaragozaCenter = LatLng(41.656335, -0.878906)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragozaCenter, 16f))

        googleMap.setOnMapClickListener { latLng ->
            userMarker?.remove()
            val markerOptions = MarkerOptions()
                .position(latLng)
                .draggable(true)
            userMarker = googleMap.addMarker(markerOptions)
        }

        binding.btnBonusQuestionMapFragment.setOnClickListener {
            if (userMarker != null) {
                val userPosition = userMarker!!.position
                val distance = FloatArray(1)
                Location.distanceBetween(
                    secretPlace.latitude, secretPlace.longitude,
                    userPosition.latitude, userPosition.longitude, distance
                )

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(secretPlace, 16f))

                if (secretMarker == null) {
                    val secretMarkerOptions = MarkerOptions()
                        .position(secretPlace)
                        .title("Taberna El Lince")
                    secretMarker = googleMap.addMarker(secretMarkerOptions)
                }

                val polylineOptions = PolylineOptions()
                    .add(secretPlace, userPosition)
                    .width(5f)
                    .color(Color.RED)

                googleMap.addPolyline(polylineOptions)

                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Resultado")
                    .setMessage("Distancia al objetivo: ${distance[0]} metros")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            } else {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Aviso")
                    .setMessage("Por favor, coloca un marcador en el mapa")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bonusQuestionMapFragment.onResume()
    }

    override fun onPause() {
        binding.bonusQuestionMapFragment.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.bonusQuestionMapFragment.onDestroy()
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}