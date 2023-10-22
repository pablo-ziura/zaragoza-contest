package com.zaragoza.contest.ui.fragment.menu.game

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.zaragoza.contest.R
import com.zaragoza.contest.databinding.FragmentBonusQuestionMapBinding
import com.zaragoza.contest.ui.viewmodel.ScoreViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.math.max
import kotlin.math.roundToInt

class BonusQuestionMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentBonusQuestionMapBinding? = null
    private val binding get() = _binding!!

    private val scoreViewModel: ScoreViewModel by activityViewModel()

    private lateinit var bonusQuestionMap: MapView

    private var userMarker: Marker? = null
    private var secretMarker: Marker? = null

    private var finalScore: Int? = 0

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

        finalScore = arguments?.getInt("finalScore", 0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val secretPlace = LatLng(41.654094, -0.876687)
        val zaragozaCenter = LatLng(41.656335, -0.878906)

        initializeMap(googleMap, zaragozaCenter)

        showAlertDialog(
            "¡PREGUNTA BONUS!",
            "Solo hay un lugar de Zaragoza donde encontrarse con un guardia civil es sinónimo de alegría.\n¿Serías capaz de ubicarlo en el mapa?\nRecuerda, cuanto más te acerques, ¡mayor será tu puntuación!"
        )

        googleMap.setOnMapClickListener { latLng ->
            placeUserMarker(googleMap, latLng)
        }

        binding.btnBonusQuestionMapFragment.setOnClickListener {
            handleButtonClick(googleMap, secretPlace)
        }
    }

    private fun initializeMap(googleMap: GoogleMap, initialPosition: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 16f))
    }

    private fun placeUserMarker(googleMap: GoogleMap, position: LatLng) {
        userMarker?.remove()
        val markerOptions = MarkerOptions()
            .position(position)
            .draggable(true)
        userMarker = googleMap.addMarker(markerOptions)
    }

    private fun handleButtonClick(googleMap: GoogleMap, secretPlace: LatLng) {
        if (userMarker != null) {
            val distance = calculateDistance(secretPlace, userMarker!!.position)
            val score = calculateScoreByDistance(distance)
            scoreViewModel.updateCurrentUserScore(score)

            handleSuccessCase(googleMap, secretPlace, distance, score)

        } else {
            showAlertDialog(
                "Aviso",
                "Por favor, coloca un marcador en el mapa"
            )
        }
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Float {
        val distance = FloatArray(1)
        Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude, distance
        )
        return distance[0]
    }

    private fun handleSuccessCase(
        googleMap: GoogleMap,
        secretPlace: LatLng,
        distance: Float,
        score: Int
    ) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(secretPlace, 16f))

        if (secretMarker == null) {
            val secretMarkerOptions = MarkerOptions().position(secretPlace)
            secretMarker = googleMap.addMarker(secretMarkerOptions)
        }

        drawPolyline(googleMap, secretPlace, userMarker!!.position)

        showAlertDialog(
            "PUNTUACIÓN EXTRA",
            "Aunque un guardia civil puede encontrarse en muchas vitrinas de la ciudad, su origen se sitúa en el bar El Lince, situado en la plaza Santa Marta, donde lleva haciéndose desde hace más de 50 años.\nTe has equivocado por ${distance.roundToInt()} metros y la puntuación obtenida es de $score puntos."
        ) {
            findNavController().navigate(R.id.action_bonusQuestionMapFragment_to_finalScoreFragment)
        }
    }

    private fun drawPolyline(googleMap: GoogleMap, start: LatLng, end: LatLng) {
        val polylineOptions = PolylineOptions()
            .add(start, end)
            .width(5f)
            .color(Color.RED)

        googleMap.addPolyline(polylineOptions)
    }

    private fun showAlertDialog(
        title: String,
        message: String,
        onPositiveClick: (() -> Unit)? = null
    ) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                onPositiveClick?.invoke()
            }
            .create()

        alertDialog.show()
    }

    private fun calculateScoreByDistance(distance: Float): Int {
        val error = ((distance / 10).toDouble()).roundToInt()
        val penalty = error * 250             // Penalización de puntos por cada 10 metros de error
        return max(10000 - penalty, 0) // Máxima puntuación: 10.000 puntos
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