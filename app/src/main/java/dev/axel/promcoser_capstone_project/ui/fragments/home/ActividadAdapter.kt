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

class ActividadAdapter (private var lista_actividades: List<ModelActividadItem>): RecyclerView.Adapter<ActividadAdapter.ViewHolder>(){

    // Declaración de los items de la actividad
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val actividad_tv_actividad = itemView.findViewById<TextView>(R.id.item_actividad_tv_actividad)
        val actividad_iv_status = itemView.findViewById<ImageView>(R.id.item_actividad_iv_status)
    }

    // Asignación del layout modelo a crearse
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_actividad_view, parent, false))
    }

    override fun getItemCount(): Int {
        return lista_actividades.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista_actividades[position]
        holder.actividad_tv_actividad.text = item.item_actividad_tv_actividad.toString()
        holder.actividad_iv_status.setImageResource(item.item_actividad_iv_status)

        // Para cuando se de un clic en el item
        holder.itemView.setOnClickListener { view ->
            var clickedItem = lista_actividades[position]
            var clickedItemJson = clickedItem.item_actividad_json

            // Para pasar los datos al otro fragmento
            val bundle = Bundle()
            bundle.putString("actividad", Gson().toJson(clickedItemJson))
            Log.d("Mensaje", "Actividad: $clickedItemJson")

            // Seleccionando el fragmento
            val fragment = GalleryFragment()
            fragment.arguments = bundle

            // Navegación al otro fragmento
            findNavController(view.findFragment()).navigate(R.id.action_nav_home_to_nav_gallery, bundle)
        }
    }

}
