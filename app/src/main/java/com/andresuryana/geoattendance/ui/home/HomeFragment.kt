package com.andresuryana.geoattendance.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.data.model.AttendanceType
import com.andresuryana.geoattendance.databinding.FragmentHomeBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var locationManager: LocationManager

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { (permission, isGranted) ->
                if (!isGranted) {
                    Toast.makeText(
                        requireContext(),
                        "Permission $permission is denied!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@registerForActivityResult
                }
            }

            // Continue get location
            getCurrentLocation()
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForLocationPermission()

        setupCircleButton()

        viewModel.currentDate.observe(viewLifecycleOwner, this::setCurrentDate)
        viewModel.currentTime.observe(viewLifecycleOwner, this::setCurrentTime)
        viewModel.actionType.observe(viewLifecycleOwner, this::observeActionType)
        viewModel.getUsername().observe(viewLifecycleOwner, this::setGreeting)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect { messageRes ->
                    messageRes?.let { observeError(it) }
                    viewModel.clearError()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, proceed to get the location
            getCurrentLocation()
        } else {
            // Request location permission
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is enabled, proceed to get location
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (gpsLocation != null) {
                val latitude = gpsLocation.latitude
                val longitude = gpsLocation.longitude
                Log.d(
                    "HomeFragment",
                    "getCurrentLocation: Using GPS! lat=$latitude, lng=$longitude"
                )
                viewModel.setLocation(LatLng(latitude, longitude))
            } else {
                LocationServices.getFusedLocationProviderClient(requireContext())
                    .lastLocation.addOnSuccessListener { gmsLocation ->
                        gmsLocation?.let {
                            val latitude = gmsLocation.latitude
                            val longitude = gmsLocation.longitude
                            Log.d(
                                "HomeFragment",
                                "getCurrentLocation: Using GMS Location! lat=$latitude, lng=$longitude"
                            )
                            viewModel.setLocation(LatLng(latitude, longitude))
                        }
                    }
            }
        } else {
            // Request to enable GPS
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun setupCircleButton() {
        // Configure animation
        val pulseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse_animation)
        binding.pulseEffect.startAnimation(pulseAnim)

        // Click listener
        binding.btnCheck.setOnClickListener {
            if (viewModel.isCheckIn()) {
                Log.d("HomeFragment", "setupCircleButton: Doing check in process...")
                viewModel.checkIn()
            } else {
                Log.d("HomeFragment", "setupCircleButton: Doing check out process...")
                viewModel.checkOut()
            }
        }
    }

    private fun setCheckInButton() {
        val bgColor = ContextCompat.getColorStateList(requireContext(), R.color.green)
        binding.btnCheck.setText(R.string.btn_check_in)
        binding.btnCheck.backgroundTintList = bgColor
        binding.pulseEffect.backgroundTintList = bgColor
    }

    private fun setCheckOutButton() {
        val bgColor = ContextCompat.getColorStateList(requireContext(), R.color.red)
        binding.btnCheck.setText(R.string.btn_check_out)
        binding.btnCheck.backgroundTintList = bgColor
        binding.pulseEffect.backgroundTintList = bgColor
    }

    private fun setCurrentDate(date: String) {
        binding.tvDate.text = getString(R.string.text_style_date, date)
    }

    private fun setCurrentTime(time: String) {
        binding.tvTime.text = getString(R.string.text_style_time, time)
    }

    private fun observeActionType(type: AttendanceType) {
        // Update circle button (check in or check out)
        if (type == AttendanceType.CHECK_OUT) setCheckOutButton()
        else setCheckInButton()
    }

    private fun observeError(messageRes: Int?) {
        messageRes?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setGreeting(username: String?) {
        binding.tvGreeting.text = getString(R.string.title_greeting, username ?: "User")
    }
}