package com.andresuryana.geoattendance.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentDate.observe(viewLifecycleOwner, this::setCurrentDate)
        viewModel.currentTime.observe(viewLifecycleOwner, this::setCurrentTime)
    }

    private fun setCurrentDate(date: String) {
        binding.tvDate.text = getString(R.string.text_style_date, date)
    }

    private fun setCurrentTime(time: String) {
        binding.tvTime.text = getString(R.string.text_style_time, time)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}