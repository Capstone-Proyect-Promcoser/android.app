package dev.axel.promcoser_capstone_project.ui.fragments.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.databinding.FragmentActividadBinding
import dev.axel.promcoser_capstone_project.ui.fragments.home.HomeFragment
import dev.axel.promcoser_capstone_project.ui.model.ModelActividadItem
import retrofit2.http.POST
import retrofit2.http.PUT

class GalleryFragment : Fragment() {

    private var _binding: FragmentActividadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_actividad, container, false)

        val tv_actividad: TextView = view.findViewById(R.id.tv_trabajo)
        val tv_hora_inicio: EditText = view.findViewById(R.id.et_inicio)
        val tv_hora_fin: EditText = view.findViewById(R.id.et_fin)
        val et_incidencia: EditText = view.findViewById(R.id.et_incidencia)
        val et_observacion: EditText = view.findViewById(R.id.et_observacion)
        val et_horometro: EditText = view.findViewById(R.id.et_horometro)
        val btn_registro: Button = view.findViewById(R.id.btn_registro)

        val json = arguments?.getString("actividad")
        Log.d("Mensaje", "Json: $json")

        // Recuperando la información del json
        val actividad = Gson().fromJson(json, HomeFragment.DetalleParteDiarioItem::class.java)
        Log.d("Mensaje", "Actividad: $actividad")

        // Cargando el nombre de la actividad
        tv_actividad.text = actividad.trabajoEfectuado.toString()

        if (actividad.estadoTarea) {
            // Cargando las hora
            tv_hora_inicio.setText(actividad.horaInicio.toString())
            tv_hora_fin.setText(actividad.horaFin.toString())

            // Cargando la incidencia
            et_incidencia.setText(actividad.incidencias.toString())

            // Cargando la observacion
            et_observacion.setText(actividad.observaciones.toString())

            // Cargando el horometro
            et_horometro.setText(actividad.cantidad.toString())
        }

        btn_registro.setOnClickListener {
            Log.d("Mensaje", "Boton de actualización")

        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface DetalleParteService {
        @PUT("api/DetalleParte")
        suspend fun putDetalleParte(): mensaje

    }

    data class mensaje(
        val mensaje: String
    )
}