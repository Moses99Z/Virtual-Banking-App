package com.example.banking.ui.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.banking.R
import com.example.banking.data.model.History
import com.example.banking.data.model.User
import com.example.banking.data.viewmodel.UserViewModel
import com.example.banking.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()

        // Hide Action Bar in The login/Register Screen
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Check if username & password are correct
        setUpUsernameField()
        setUpEmailField()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Navigate to Login Screen
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.register.setOnClickListener {
            register()
        }

        return binding.root
    }

    private fun register() {
        username = binding.username.text.toString().trim()
        password = binding.password.text.toString().trim()
        email = binding.email.text.toString().trim()
        val userCreated = User(0, username, password, email)
        if (inputCheck(username, password, email)) {
            userViewModel.getUsername(username).observe(viewLifecycleOwner, Observer { user ->
                if (user == null) {
                    userViewModel.insertUser(userCreated)
                    Snackbar.make(requireView(), "Account Created Successfully !", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    Snackbar.make(requireView(), "Username already exist !", Snackbar.LENGTH_SHORT).show()
                }
            })
        } else {
            Snackbar.make(requireView(), "Please Fill all Fields !", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setUpUsernameField() {
        binding.usernameLayout.isEndIconVisible = false
        binding.username.doAfterTextChanged {
            binding.usernameLayout.isEndIconVisible = binding.username.text?.length != 0
        }
    }

    private fun setUpEmailField() {
        binding.emailLayout.isEndIconVisible = false
        binding.email.doAfterTextChanged {
            binding.emailLayout.isEndIconVisible = binding.email.text?.length != 0
        }
    }

    private fun inputCheck(username: String, password: String, email: String) : Boolean {
        return !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email))
    }
}