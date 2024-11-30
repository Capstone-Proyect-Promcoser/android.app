package dev.axel.promcoser_capstone_project.ui.fragments.home

import android.os.Bundle
import android.text.format.DateUtils.isToday
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.databinding.FragmentHomeBinding
import dev.axel.promcoser_capstone_project.ui.model.ModelActividadItem
import dev.axel.promcoser_capstone_project.ui.model.ModelParteDiarioItem
import dev.axel.promcoser_capstone_project.ui.model.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class DetalleParteFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val sharedViewModel: SharedViewModel by activityViewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_detalle_parte, container, false)

        // Creando el layout para cada item
        val rvActividad: RecyclerView = view.findViewById(R.id.rvParteDiario)
        rvActividad.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val activities = getListaDetalle() // Call the suspending function
            withContext(Dispatchers.Main) {
                rvActividad.adapter = ParteDiarioAdapter(activities) // Set the adapter with the result
            }
        }

        return view
    }

    private suspend fun getListaDetalle(): List<ModelParteDiarioItem> {
        val listaActividades: ArrayList<ModelParteDiarioItem> = ArrayList()
        //listaActividades.add(ModelActividadItem(descripcion, R.drawable.waiting_activity_icon))

        val retrofit = Retrofit.Builder()
            .baseUrl("https://amusing-presumably-man.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val parteDiarioService = retrofit.create(ParteDiarioService::class.java)


        //lifecycleScope.launch {
        try {
            val parteDiarioItems = parteDiarioService.getParteDiario()
            //Log.d("Mensaje", "ParteDiarioItems: $parteDiarioItems")

            for (parteDiarioItem in parteDiarioItems) {

                val idparte = parteDiarioItem.idParte.toInt()
                    //Log.d("Mensaje", "ID del ParteDiario: $idparte")

                val fechaEjecucion = parteDiarioItem.fechaEjecucion

                val fechaRegistro = parteDiarioItem.fechaRegistro





                listaActividades.add(ModelParteDiarioItem(idparte,fechaEjecucion,fechaRegistro))



            }
        } catch (e: Exception) {
            Log.d("Mensaje", "Error al obtener las actividades: ${e.message}")
        }
        //}


        // Impresión de las actividades
        Log.d("Mensaje", "Lista de elementos :\n" + listaActividades)
        return listaActividades
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface ParteDiarioService{
        @GET("api/ParteDiario")
        suspend fun getParteDiario(): List<ParteDiarioItem>
    }

    interface DetalleParteDiarioService{
        @GET("api/DetalleParte/{idParteDiario}")
        suspend fun getDetalleParteDiario(@Path("idParteDiario") idParteDiario : Int): List<DetalleParteDiarioItem>
    }

    data class  ParteDiarioItem(
        val idParte: Int,
        val idCliente: Int,
        val idLugarTrabajo: Int,
        val idOperador: String,
        val idControlador: String,
        val idIngeniero: String,
        val fechaRegistro: Date,
        val fechaEjecucion: Date,
        val idMaquina: Int,
        val horometroInicio: Double,
        val horometroFinal: Double,
        val totalHm: Double,
        val combustible: Double,
        val aceite: Double,
        val estConformidad: Boolean
    )

    data class DetalleParteDiarioItem(
        val detalleParteId: Int,
        val idParte: Int,
        val horaInicio: String,
        val horaFin: String,
        val trabajoEfectuado: String,
        val incidencias: String,
        val estadoTarea: Boolean,
        val observaciones: String,
        val cantidad: Int
    )

}
