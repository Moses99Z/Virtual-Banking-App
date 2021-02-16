package com.example.banking.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.banking.R
import com.example.banking.data.model.History
import com.example.banking.databinding.FragmentSendBinding
import com.example.banking.databinding.FragmentSettingsBinding
import com.example.banking.databinding.FragmentTransactionDetailsBinding
import com.google.android.material.transition.MaterialFadeThrough
import java.text.SimpleDateFormat
import java.util.*


class TransactionDetailsFragment : Fragment() {

    private val args by navArgs<TransactionDetailsFragmentArgs>()
    private var _binding: FragmentTransactionDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentTransaction: History

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Details"
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)

        currentTransaction = args.currentTransaction

        val amountWithDollar = currentTransaction.amount.toString() + " $"
        val date = Date(currentTransaction.createdTime)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")

        binding.sender.text = currentTransaction.sender
        binding.receiver.text = currentTransaction.receiver
        binding.amount.text = amountWithDollar
        binding.date.text = format.format(date)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}