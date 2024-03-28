package com.andresuryana.geoattendance.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.databinding.FragmentSettingBinding
import com.andresuryana.geoattendance.util.MapsExt.enableMyLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SettingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingViewModel>()

    private var gmap: GoogleMap? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) getMyLocation()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupMapView()
        setupButton()

        viewModel.location.observe(viewLifecycleOwner, this::setMarker)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onMapReady(map: GoogleMap) {
        gmap = map

        // Google Maps settings
        gmap?.uiSettings?.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        // Setup map click listener
        gmap?.setOnMapClickListener { pos ->
            // Set data into view model
            viewModel.setCurrentLocation(pos)
            setMarker(pos)
        }

        // Enable google maps location
        getMyLocation()
    }

    private fun setupTopBar() {
        binding.topBar.titleTopBar.setText(R.string.title_history)
    }

    private fun setupMapView() {
        val mapView = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapView.getMapAsync(this)
    }

    private fun setupButton() {
        binding.btnSubmit.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.title_warning)
                .setMessage(R.string.dialog_confirm_update_master_location)
                .setPositiveButton(R.string.btn_positive) { _, _ ->
                    viewModel.updateMasterLocation(this::onMasterLocationUpdated)
                }
                .setNegativeButton(R.string.btn_negative) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun getMyLocation() {
        gmap?.enableMyLocation(requireActivity(), requestPermissionLauncher)
    }

    private fun setMarker(pos: LatLng) {
        val marker = MarkerOptions()
        marker.title(getString(R.string.title_master_location))
            .snippet("${pos.latitude}, ${pos.longitude}")
            .position(pos)
            .draggable(true)

        // Clear marker first to prevent duplicate markers
        gmap?.clear()
        gmap?.addMarker(marker)
    }

    private fun onMasterLocationUpdated(error: String?) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    error ?: "Master location updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}