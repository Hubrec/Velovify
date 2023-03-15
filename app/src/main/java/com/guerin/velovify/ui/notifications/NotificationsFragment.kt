package com.guerin.velovify.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.guerin.velovify.MainActivity
import com.guerin.velovify.R
import com.guerin.velovify.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val button7: Button = binding.toggleButton7h
        val button8: Button = binding.toggleButton8h
        val button10: Button = binding.toggleButton10h
        val button12: Button = binding.toggleButton12h
        val button14: Button = binding.toggleButton14h
        val button18: Button = binding.toggleButton18h

        button7.setOnClickListener {
            funcTrigger(it)
        }

        button8.setOnClickListener {
            funcTrigger(it)
        }

        button10.setOnClickListener {
            funcTrigger(it)
        }

        button12.setOnClickListener {
            funcTrigger(it)
        }

        button14.setOnClickListener {
            funcTrigger(it)

        }

        button18.setOnClickListener {
            funcTrigger(it)
        }

        return root
    }

    fun funcTrigger(view: View) {
        Log.i("NotificationsFragment", "Button is activated")
        (requireActivity() as? MainActivity)?.also {
            it.askPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}