package com.example.banking.ui.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.banking.R
import com.example.banking.data.viewmodel.UserViewModel
import com.example.banking.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private val KEY_LOGIN = "key_login"
    private val KEY_USER = "key_user"

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var userViewModel: UserViewModel

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()

        dataStore = requireContext().createDataStore(name = "login_status")

        // Check if user already connected
        checkLoggedIn()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Hide Action Bar
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Navigate to Register Screen
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Check Username if correct typing
        setUpUsernameField()

        // Login
        binding.login.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun checkLoggedIn() {
        lifecycleScope.launch {
            if (read("LOGIN_KEY") == true) {
                readUser("USER")?.let {
                    userViewModel.getUsername(it).observe(viewLifecycleOwner, Observer { user ->
                        val action = LoginFragmentDirections.actionLoginFragmentToAccountFragment(user)
                        findNavController().navigate(action)
                    })
                }
            }
        }
    }

    private fun login() {
        username = binding.username.text.toString().trim()
        password = binding.password.text.toString().trim()
        if (inputCheck(username, password)) {
            userViewModel.getUser(username, password).observe(viewLifecycleOwner, Observer { user ->
                if (user == null) {
                    Snackbar.make(requireView(), "Username or Password Incorrect !", Snackbar.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        save("LOGIN_KEY", true)
                        save("loading", true)
                        saveUser("USER", user.username)
                    }
                    val action = LoginFragmentDirections.actionLoginFragmentToAccountFragment(user)
                    findNavController().navigate(action)
                    Snackbar.make(requireView(), "Connected Successfully !", Snackbar.LENGTH_SHORT).show()
                }
            })
        } else {
            Snackbar.make(requireView(), "Please Fill all fields !", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setUpUsernameField() {
        binding.usernameLayout.isEndIconVisible = false
        binding.username.doAfterTextChanged {
            binding.usernameLayout.isEndIconVisible = binding.username.text?.length != 0
        }
    }

    private fun inputCheck(username: String, password: String) : Boolean {
        return !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
    }

    private suspend fun save(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun saveUser(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun readUser(key: String) : String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun read(key: String) : Boolean? {
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }


}

