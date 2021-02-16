package com.example.banking.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banking.R
import com.example.banking.data.model.User
import com.example.banking.data.viewmodel.UserViewModel
import com.example.banking.databinding.FragmentAccountBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class AccountFragment : Fragment() {

    private val args by navArgs<AccountFragmentArgs>()

    private var _binding : FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    private lateinit var currentUser: User

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        dataStore = requireContext().createDataStore(name = "login_status")

        // Setting the Action Bar
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Account"
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Get Current Connected User
        currentUser = args.currentUser
        userViewModel.getUser(args.currentUser.username, args.currentUser.password).observe(viewLifecycleOwner, Observer { user ->
            currentUser = user
        })

        // Balance
        initBalance()

        // Setting the RecyclerView for transactions History
        initRecyclerView(currentUser)

        // Send Money Action
        binding.send.setOnClickListener {
            sendMoney()
        }

        binding.progressBar.visibility = View.GONE

        return binding.root
    }

    private fun initBalance() {
        userViewModel.getUser(currentUser.username, currentUser.password).observe(viewLifecycleOwner, Observer { user ->
            val txt : String = user.balance.toString() + " $"
            binding.balance.text = txt
        })
    }

    private fun sendMoney() {
        val action = AccountFragmentDirections.actionAccountFragmentToSendFragment(currentUser)
        findNavController().navigate(action)
    }

    private fun initRecyclerView(connectedUser: User) {
        binding.noTransactions.visibility = View.GONE
        val adapter = HistoryAdapter()
        val recyclerView = binding.historyRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        userViewModel.getFullHistory(connectedUser.username).observe(viewLifecycleOwner, Observer { history ->
            adapter.setHistory(history)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            logout()
        } else if(item.itemId == R.id.profile) {
            val action = AccountFragmentDirections.actionAccountFragmentToProfileFragment(currentUser)
            findNavController().navigate(action)
        } else if(item.itemId == R.id.settings) {
            findNavController().navigate(R.id.action_accountFragment_to_settingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun pageLoad() {
        binding.layout.visibility = View.GONE
        lifecycleScope.launch {
            delay(300)
            binding.progressBar.visibility = View.GONE
            binding.layout.visibility = View.VISIBLE
        }
        save("loading", false)

    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setMessage("Are you sure ?")
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    save("LOGIN_KEY", false)
                    save("loading", true)
                }
                findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
                Snackbar.make(requireView(), "Disconnected !", Snackbar.LENGTH_SHORT).show()
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