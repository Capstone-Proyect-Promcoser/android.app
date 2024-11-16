package dev.axel.promcoser_capstone_project.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.ui.model.ActividadesModelo

class ActividadesAdapter (private var actividadesList: List<ActividadesModelo>) :
        RecyclerView.Adapter<ActividadesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreActividad: TextView = itemView.findViewById(R.id.tvActividadIte)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list_actividades, parent, false))

    }

    override fun getItemCount(): Int {
        return actividadesList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nombreActividad = actividadesList[position]
        holder.tvNombreActividad.text = nombreActividad.nombreact


    }
    fun updateActividadesList(newProductList: List<ActividadesModelo>) {
        actividadesList = newProductList
        notifyDataSetChanged()
    }
}