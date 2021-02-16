package com.example.banking.ui.dashboard

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.banking.R
import com.example.banking.data.model.History

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var historyList = emptyList<History>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amount: TextView = itemView.findViewById(R.id.amount)
        val sender: TextView = itemView.findViewById(R.id.sender)
        val receiver: TextView = itemView.findViewById(R.id.receiver)
        val layout: ConstraintLayout = itemView.findViewById(R.id.historyLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTransaction = historyList[position]
        val receiverToText: String = "to " + currentTransaction.receiver

        holder.amount.text = currentTransaction.amount.toString()
        holder.sender.text = currentTransaction.sender
        holder.receiver.text = receiverToText

        holder.layout.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToTransactionDetailsFragment(currentTransaction)
            holder.layout.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun setHistory(history: List<History>) {
        this.historyList += history
        notifyDataSetChanged()
    }
}