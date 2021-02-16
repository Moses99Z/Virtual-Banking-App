package com.example.banking.ui.dashboard

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.banking.data.model.History
import com.example.banking.data.model.User
import com.example.banking.data.viewmodel.UserViewModel
import com.example.banking.databinding.FragmentSendBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough


class SendFragment : Fragment() {

    private val args by navArgs<SendFragmentArgs>()
    private lateinit var currentUser: User

    private var _binding: FragmentSendBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    private lateinit var receiver: String
    private lateinit var balance: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSendBinding.inflate(inflater, container, false)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        currentUser = args.currentUser
        Log.i("DEBUG : ", currentUser.toString())

        binding.send.setOnClickListener {
            send()
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Send Money"
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun send() {
        receiver = binding.receiver.text.toString().trim()
        balance = binding.balance.text.toString().trim()
        if (inputCheck(receiver, balance)) {
            userViewModel.getUsername(receiver).observe(viewLifecycleOwner, Observer { user ->
                if (user == null) {
                    Snackbar.make(requireView(), "Target Not Found !", Snackbar.LENGTH_SHORT).show()
                } else {
                    val transaction = History(0, currentUser.username, receiver, balance.toDouble())
                    Log.i("DEBUG : ", currentUser.toString())
                    if(currentUser.balance - balance.toDouble() >= 0) {
                        userViewModel.updateBalance(currentUser.username, currentUser.balance - balance.toDouble())
                        userViewModel.updateBalance(receiver, user.balance + balance.toDouble())
                        userViewModel.insertHistory(transaction)
                        Snackbar.make(requireView(), "Operation Successful !", Snackbar.LENGTH_SHORT).show()
                        val action = SendFragmentDirections.actionSendFragmentToAccountFragment(currentUser)
                        findNavController().navigate(action)
                    } else {
                        Snackbar.make(requireView(), "Not enough Money !", Snackbar.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            Snackbar.make(requireView(), "Please Fill all Fields !", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(username: String, balance: String) : Boolean {
        return !(TextUtils.isEmpty(username) || TextUtils.isEmpty(balance))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


}