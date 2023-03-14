package com.guerin.velovify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guerin.velovify.MainActivity
import com.guerin.velovify.R
import com.guerin.velovify.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonMap.setOnClickListener {
            Toast.makeText(context, "Map", Toast.LENGTH_SHORT).show()
            (requireActivity() as? MainActivity)?.also {
                it.changeFragment(R.id.navigation_map)
            }
        }

        binding.buttonList.setOnClickListener {
            Toast.makeText(context, "List", Toast.LENGTH_SHORT).show()
            (requireActivity() as? MainActivity)?.also {
                it.changeFragment(R.id.navigation_list)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}