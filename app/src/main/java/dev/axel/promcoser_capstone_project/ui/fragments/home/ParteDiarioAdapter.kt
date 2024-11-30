package dev.axel.promcoser_capstone_project.ui.fragments.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.ui.fragments.gallery.GalleryFragment
import dev.axel.promcoser_capstone_project.ui.model.ModelActividadItem
import dev.axel.promcoser_capstone_project.ui.model.ModelParteDiarioItem


class ParteDiarioAdapter (private var lista_ParteDiario: List<ModelParteDiarioItem>): RecyclerView.Adapter<ParteDiarioAdapter.ViewHolder>(){

    // Declaración de los items de la actividad
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val idParteDiario = itemView.findViewById<TextView>(R.id.tvIdParteDiario)
        var fechReg = itemView.findViewById<TextView>(R.id.tvFechRegParteDiario)
        var fechEje = itemView.findViewById<TextView>(R.id.tvFechEjeParteDiario)
    }

    // Asignación del layout modelo a crearse
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_partediario, parent, false))
    }

    override fun getItemCount(): Int {
        return lista_ParteDiario.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista_ParteDiario[position]
        holder.idParteDiario.text = item.tvidParteDiarioItem.toString()
        holder.fechReg.text = item.tvFechRegParteDiarioItem.toString()
        holder.fechEje.text = item.tvFechEjeParteDiarioItem.toString()



    }

}