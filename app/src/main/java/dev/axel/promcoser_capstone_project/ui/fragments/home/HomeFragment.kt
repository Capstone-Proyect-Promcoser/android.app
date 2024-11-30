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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val sharedViewModel: SharedViewModel by activityViewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        // Obteniendo información por parte del usuario



        val home_tv_user_name: TextView = view.findViewById(R.id.home_tv_user_name)
        home_tv_user_name.text = sharedViewModel.loginResponse?.nombres

        // Para mostra la fecha de hoy
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM", Locale("es", "ES")) // Spanish locale
        val formattedDate = currentDate.format(formatter).toString().uppercase()

        val home_tv_fecha: TextView = view.findViewById(R.id.home_tv_fecha)
        home_tv_fecha.text = "PARA HOY  $formattedDate"

        // Creando el layout para cada item
        val rvActividad: RecyclerView = view.findViewById(R.id.home_rv_actividades_list)
        rvActividad.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val activities = getListaActividades() // Call the suspending function
            withContext(Dispatchers.Main) {
                rvActividad.adapter = ActividadAdapter(activities) // Set the adapter with the result
            }
        }
        //rvActividad.adapter = ActividadAdapter(getListaActividades())


        // Vista del botón de lista de actividades
        val home_btn_informe_actividades: TextView = view.findViewById(R.id.home_btn_informe_actividades)
        home_btn_informe_actividades.setOnClickListener {
            Snackbar.make(view, "Proximamente", Snackbar.LENGTH_SHORT).show()
        }

        return view
    }

    private suspend fun getListaActividades(): List<ModelActividadItem> {
        val listaActividades: ArrayList<ModelActividadItem> = ArrayList()
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
                val currentDate = LocalDate.now()
                //Log.d("Mensaje", "Fecha actual: $currentDate")

                val fechaEjecucionLocalDate = parteDiarioItem.fechaEjecucion.toInstant()
                                                .atZone(ZoneId.systemDefault()).toLocalDate()
                //Log.d("Mensaje", "Fecha de ejecución: $fechaEjecucionLocalDate")

                if ((fechaEjecucionLocalDate.isEqual(currentDate)) &&
                    (parteDiarioItem.idOperador.toString() == sharedViewModel.loginResponse?.dni.toString()) ) {
                    // Obtención del id del ParteDiario
                    val idparte = parteDiarioItem.idParte.toInt()
                    //Log.d("Mensaje", "ID del ParteDiario: $idparte")

                    // Consultas de detalles del ParteDiario
                    val detalleParteDiarioService = retrofit.create(DetalleParteDiarioService::class.java)
                    val detalleParteDiarioItems = detalleParteDiarioService.getDetalleParteDiario(idparte)

                    for (detalleParteDiarioItem in detalleParteDiarioItems) {
                        //Log.d("Mensaje", "DetalleParteDiarioItem: $detalleParteDiarioItem")
                        val descripcion = detalleParteDiarioItem.trabajoEfectuado.toString()

                        if (detalleParteDiarioItem.estadoTarea == true){
                            listaActividades.add(ModelActividadItem(descripcion, R.drawable.check_icon, detalleParteDiarioItem))
                        } else {
                            listaActividades.add(ModelActividadItem(descripcion, R.drawable.waiting_activity_icon, detalleParteDiarioItem))
                        }
                    }


                }
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