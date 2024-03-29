package com.andresuryana.geoattendance.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andresuryana.geoattendance.R
import com.andresuryana.geoattendance.data.model.Attendance
import com.andresuryana.geoattendance.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter

    private val viewModel by viewModels<HistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Initialize components
        adapter = HistoryAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupRecyclerView()
        setupRefreshLayout()

        viewModel.histories.observe(viewLifecycleOwner, this::setAttendanceHistories)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupTopBar() {
        binding.topBar.titleTopBar.setText(R.string.title_history)
    }

    private fun setupRecyclerView() {
        binding.rvHistory.adapter = adapter
    }

    private fun setupRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getHistories()
        }
    }

    private fun setAttendanceHistories(attendances: List<Attendance>) {
        // Stop refresh layout
        binding.swipeRefresh.isRefreshing = false

        // Show result
        if (attendances.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.emptyLayout.visibility = View.VISIBLE
        } else {
            adapter.setList(attendances)
            binding.rvHistory.visibility = View.VISIBLE
            binding.emptyLayout.visibility = View.GONE
        }
    }
}