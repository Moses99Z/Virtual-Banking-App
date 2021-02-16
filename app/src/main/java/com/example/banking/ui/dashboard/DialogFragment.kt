package com.example.banking.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.banking.R
import com.example.banking.data.viewmodel.UserViewModel
import com.example.banking.databinding.FragmentDialogBinding
import com.google.android.material.snackbar.Snackbar


class DialogFragment : DialogFragment() {

    private var _binding : FragmentDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    override fun onStart() {
        super.onStart()
        val dialog : Dialog? = dialog
        if (dialog != null) {
            val width : Int = ViewGroup.LayoutParams.WRAP_CONTENT
            val height : Int = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AlertDialogTheme)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDialogBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val currentUsername : String? = arguments?.getString("User")

        binding.toolbar.apply {
            inflateMenu(R.menu.top_bar)
            title = "Change Password"
            setTitleTextAppearance(context, R.style.BodyTextStyle)
            setNavigationOnClickListener { dismiss() }
            setOnMenuItemClickListener {
                val newPassword = binding.newPassword.text.toString()
                if (it.itemId == R.id.save) {
                    if (currentUsername != null) {
                        userViewModel.changePassword(currentUsername, newPassword)
                        dismiss()
                    }
                }
                true
            }
        }
        return binding.root
    }


}