package com.petarpetrevski.mobilnidomasna2

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecnikAdapter(private val context: Context, private val recnikList : ArrayList<Zbor>) : RecyclerView.Adapter<RecnikAdapter.ZborViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ZborViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.recnik_zbor, parent, false)
        return ZborViewHolder(view)
    }

    override fun onBindViewHolder(holder: ZborViewHolder, position: Int) {
        val  momentalenZbor = recnikList[position]
        holder.labelMkZbor.text = momentalenZbor.inputMakedonski + " -> " + momentalenZbor.inputAngliski
        holder.btnIzmeniZbor.setOnClickListener {
            (context as MainActivity).popolniPodatociEdit(momentalenZbor.inputMakedonski!!, momentalenZbor.inputAngliski!!)
        }
    }

    override fun getItemCount(): Int {
        return recnikList.size
    }

    class ZborViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelMkZbor = itemView.findViewById<TextView>(R.id.labelMkZbor)
        val btnIzmeniZbor = itemView.findViewById<Button>(R.id.btnIzmeniZbor)
    }

}

