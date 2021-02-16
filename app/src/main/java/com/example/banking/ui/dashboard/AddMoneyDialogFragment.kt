package com.example.banking.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.banking.R
import com.example.banking.data.viewmodel.UserViewModel
import com.example.banking.databinding.FragmentAddMoneyDialogBinding


class AddMoneyDialogFragment : DialogFragment() {

    private var _binding : FragmentAddMoneyDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private var currentBalance :Double = 0.0

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

        _binding = FragmentAddMoneyDialogBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val currentUsername : String? = arguments?.getString("User")
        if (currentUsername != null) {
            userViewModel.getUsername(currentUsername).observe(viewLifecycleOwner, { user ->
                currentBalance = user.balance
            })
        }

        binding.toolbar.apply {
            inflateMenu(R.menu.top_bar)
            title = "Add Credit"
            setTitleTextAppearance(context, R.style.BodyTextStyle)
            setNavigationOnClickListener { dismiss() }
            setOnMenuItemClickListener {
                if (it.itemId == R.id.save) {
                    if (currentUsername != null) {
                        val newBalance = currentBalance + binding.addAmount.text.toString().toDouble()
                        userViewModel.updateBalance(currentUsername, newBalance)
                        dismiss()
                    }
                }
                true
            }
        }

        return binding.root
    }
}