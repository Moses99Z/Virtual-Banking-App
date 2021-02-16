package com.example.banking.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.banking.R
import com.example.banking.data.model.User
import com.example.banking.data.viewmodel.UserViewModel
import com.example.banking.databinding.FragmentAccountBinding
import com.example.banking.databinding.FragmentLoginBinding
import com.example.banking.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ProfileFragmentArgs>()

    private lateinit var userViewModel: UserViewModel

    private lateinit var currentUser: User

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        dataStore = requireContext().createDataStore(name = "login_status")

        // Setting the Action Bar
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Profile"
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        currentUser = args.currentUser

        binding.profileUsername.text = currentUser.username
        binding.profileEmail.text = currentUser.email

        binding.delete.setOnClickListener {
            delete()
        }

        binding.reset.setOnClickListener {
            reset()
        }

        binding.addBalance.setOnClickListener {
            addMoney()
        }


        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun reset() {
        val dialogFragment = DialogFragment()
        val bundle: Bundle = Bundle()
        bundle.putString("User", currentUser.username)
        dialogFragment.arguments = bundle
        dialogFragment.show(requireActivity().supportFragmentManager, "reset")
    }

    private fun addMoney() {
        val dialogFragment = AddMoneyDialogFragment()
        val bundle: Bundle = Bundle()
        bundle.putString("User", currentUser.username)
        bundle.putDouble("Balance", currentUser.balance)
        dialogFragment.arguments = bundle
        dialogFragment.show(requireActivity().supportFragmentManager, "add")
    }

    private fun delete() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setMessage("Are you sure to delete your Account ?")
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Yes") { _, _ ->
                userViewModel.deleteAccount(currentUser)
                lifecycleScope.launch {
                    save("LOGIN_KEY", false)
                }
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                Snackbar.make(requireView(), "Account deleted Successfully !", Snackbar.LENGTH_SHORT).show()
            }
            .show()
    }

    private suspend fun save(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String) : Boolean? {
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }


}