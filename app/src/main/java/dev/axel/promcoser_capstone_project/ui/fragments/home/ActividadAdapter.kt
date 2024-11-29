package dev.axel.promcoser_capstone_project.ui.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.ui.model.ModelActividadItem

class ActividadAdapter (
    private var lista_actividades: List<ModelActividadItem>, // Lista de actividades
    private val onItemClick: (Int) -> Unit  // Función lambda para manejar el clic en un elemento
): RecyclerView.Adapter<ActividadAdapter.ViewHolder>(){

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

        //Configuración del click en el item completo
        holder.itemView.setOnClickListener{
            onItemClick(position) //Llama aquí a la función lambda con la posición del elemento
        }

    }

}
