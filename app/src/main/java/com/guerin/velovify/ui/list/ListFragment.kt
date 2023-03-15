package com.guerin.velovify.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.guerin.velovify.StationAdapter
import com.guerin.velovify.StationViewModel
import com.guerin.velovify.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private lateinit var viewModel: StationViewModel
    private lateinit var stationAdapter : StationAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        prepareRecyclerView()

        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            for (i in 1..2) {
                val searchText = text.toString()
                if (searchText != "") {
                    if (searchText.uppercase() == "FAV" || searchText.uppercase() == "FAVORITE") {
                        viewModel.getFavoriteStations()
                        Log.i("ListFragment", "FAVORITE")
                    } else {
                        viewModel.searchStations(searchText)
                    }
                } else {
                    viewModel.getAllStations()
                }
            }
        }


        viewModel = ViewModelProvider(this)[StationViewModel::class.java]
        viewModel.getAllStations()
        Log.i("MainActivity", "viewModel state : " + viewModel.observeStationsLiveData().value)
        viewModel.observeStationsLiveData().observe(this, Observer { stationList ->
            stationAdapter.setStationList(stationList)
        })

        return root
    }

    private fun prepareRecyclerView() {
        stationAdapter = StationAdapter()
        binding.rvVelov.apply {
            layoutManager = GridLayoutManager(this.context, 1)
            adapter = stationAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}