package dev.axel.promcoser_capstone_project.ui.fragments.home

import android.os.Bundle
import android.text.format.DateUtils.isToday
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.databinding.FragmentHomeBinding
import dev.axel.promcoser_capstone_project.ui.model.ModelActividadItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val db = FirebaseFirestore.getInstance()
        val auth = Firebase.auth

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        val user_uid = auth.currentUser?.uid
        Log.d("Mensaje", "User Id: $user_uid")

        db.collection("usuario").document(user_uid.toString()).get().addOnSuccessListener { document ->
            val home_tv_user_name: TextView = view.findViewById(R.id.home_tv_user_name)
            home_tv_user_name.text = document.get("u_nombre").toString()
            Log.d("Mensaje", "Nombre Usuario: " + document.get("u_nombre").toString())
        }

        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE dd 'de' MMMM", Locale("es", "ES"))
        val home_tv_fecha: TextView = view.findViewById(R.id.home_tv_fecha)
        home_tv_fecha.text = "PARA HOY " + dateFormat.format(today.time).uppercase()

        // Creando el layout para cada item
        val rvActividad: RecyclerView = view.findViewById(R.id.home_rv_actividades_list)
        rvActividad.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val activities = getListaActividades() // Call the suspending function


            rvActividad.adapter = ActividadAdapter(activities) { actividad ->




                //Aquí pasamos datos (depende de eso, sino lo eliminas) del ítem
                val bundle = Bundle().apply {
                    //Datos ejemplo:

                    /*
                    putString("descripcion", actividad.descripcion)
                    putInt("icono", actividad.icono)*/


                }

                //Aquí te rediriges al Fragment_Actividad
                findNavController().navigate(R.id.action_homeFragment_to_fragment_actividad) //Si envias info a la actividad colo: , bundle)


            }// Set the adapter with the result




        }
        //rvActividad.adapter = ActividadAdapter(getListaActividades())

        val home_btn_informe_actividades: TextView = view.findViewById(R.id.home_btn_informe_actividades)
        home_btn_informe_actividades.setOnClickListener {
            Snackbar.make(view, "Proximamente", Snackbar.LENGTH_SHORT).show()
        }

        return view
    }

    private suspend fun getListaActividades(): List<ModelActividadItem> = withContext(Dispatchers.IO) {
        val listaActividades: ArrayList<ModelActividadItem> = ArrayList()

        val auth = Firebase.auth
        val user_uid = auth.currentUser?.uid

        val db = FirebaseFirestore.getInstance()

        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        val documents = db.collection("parte_diario").whereEqualTo("pd_id_usuario", user_uid).get().await()


        for (document in documents) {
            val pdFecha = document.getTimestamp("pd_fecha")?.toDate()?.time
            Log.d("Mensaje", "Fecha - Inicio: " + startOfDay.toString())
            Log.d("Mensaje", "Fecha - Fin: " + endOfDay.toString())
            Log.d("Mensaje", "Fecha - Documento: " + pdFecha.toString())

            if (pdFecha in startOfDay..endOfDay){

                val descripcion = document.get("pd_descripcion").toString()
                val status = document.get("pd_status").toString()

                Log.d("Mensaje", "Descripcion: " + descripcion)
                Log.d("Mensaje", "status: " + status)

                if (status == "Pendiente"){
                    listaActividades.add(ModelActividadItem(descripcion, R.drawable.waiting_activity_icon))
                }
                if (status == "Realizado"){
                    listaActividades.add(ModelActividadItem(descripcion, R.drawable.check_icon))
                }
            }


        }

        Log.d("Mensaje", "Lista de elementos :\n" + listaActividades)
        listaActividades

        //val test: ArrayList<ModelActividadItem> = ArrayList()
        //test.add(ModelActividadItem("Actividad 1", R.drawable.waiting_activity_icon))
        //return  test
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}