package com.andresuryana.geoattendance.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.data.model.AttendanceType
import com.andresuryana.geoattendance.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    // TODO: Check location permission here first!
    // TODO: Check location state! Make sure the location is ON!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCircleButton()

        viewModel.currentDate.observe(viewLifecycleOwner, this::setCurrentDate)
        viewModel.currentTime.observe(viewLifecycleOwner, this::setCurrentTime)
        viewModel.actionType.observe(viewLifecycleOwner, this::observeActionType)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { observeError(it) }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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

    private fun observeError(messageRes: Int) {
        Toast.makeText(requireContext(), messageRes, Toast.LENGTH_SHORT).show()
    }
}