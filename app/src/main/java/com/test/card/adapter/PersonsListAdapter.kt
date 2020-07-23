package com.test.card.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.test.card.MainActivity.Companion.db
import com.test.card.R
import com.test.card.databinding.ShaadicardItemsBinding
import com.test.card.model.Result
import java.util.*

class PersonsListAdapter(
    private val listOfPersonsList: ArrayList<Result>
) : RecyclerView.Adapter<PersonsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemsBinding: ShaadicardItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shaadicard_items,
            parent,
            false
        )
        return ViewHolder(itemsBinding)
    }

    override fun getItemCount(): Int {
        return listOfPersonsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val personData = listOfPersonsList.get(position)
        holder.onBind(personData)
        holder.setIsRecyclable(false)

        if (personData.isSelected){
            holder.acceptanceView.visibility = View.GONE
            holder.status.visibility = View.VISIBLE
            holder.status.text = personData.status
        }

        holder.acceptView.setOnClickListener { click ->
            holder.acceptanceView.visibility = View.GONE
            holder.status.visibility = View.VISIBLE
            holder.status.text = "Accepted"
            updateDB(true, personData)
        }

        holder.declineView.setOnClickListener { click ->
            holder.acceptanceView.visibility = View.GONE
            holder.status.visibility = View.VISIBLE
            holder.status.text = "Declined"
            updateDB(false, personData)
        }
    }

    private fun updateDB(flag: Boolean, result: Result) {
        result.isSelected = true
        if (flag) result.status = "Accepted" else result.status = "Declined"
        val gson = Gson()
        val jsonObject = gson.toJson(result)
        val entity = com.test.card.db.Entity(result.email, jsonObject)
        db.dao().update(entity)
    }


    class ViewHolder(itemView: ShaadicardItemsBinding) : RecyclerView.ViewHolder(itemView.root) {
        val itemBinding: ShaadicardItemsBinding = itemView
        val acceptView: ImageView = itemView.acceptImageView
        val declineView: ImageView = itemView.declineImageView
        val status: TextView = itemView.statusTextView
        val acceptanceView: LinearLayout = itemView.acceptanceView

        fun onBind(result: Result) {
            itemBinding.result = result
        }
    }
}