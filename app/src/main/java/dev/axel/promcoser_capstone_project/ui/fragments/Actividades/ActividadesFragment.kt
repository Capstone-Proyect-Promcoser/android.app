package dev.axel.promcoser_capstone_project.ui.fragments.Actividades

import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Response
import com.google.rpc.context.AttributeContext
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.ui.Adapter.ActividadesAdapter
import dev.axel.promcoser_capstone_project.ui.model.ActividadesModelo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActividadesFragment : Fragment() {
    private var actividadesList: MutableList<ActividadesModelo> = mutableListOf()
    private lateinit var ActividadesAdapter: ActividadesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_actividades, container, false)
        val rvActividades: RecyclerView = view.findViewById(R.id.rvActividades)
        rvActividades.layoutManager = LinearLayoutManager(requireContext())

        ActividadesAdapter = ActividadesAdapter(actividadesList)
        rvActividades.adapter = ActividadesAdapter

        loadActividades()


        return view
    }

    private fun loadActividades() {
        val call = RetrofitInstance.api.getActividades()
        call.enqueue(object : Callback<List<ActividadesModelo>> {
            override fun onResponse(
                call: Call<List<ActividadesModelo>>,
                response: Response<List<ActividadesModelo>>
            ) {
                if (response.isSuccessful) {
                    actividadesList = response.body() ?: emptyList()
                    ActividadesAdapter.updateActividadesList(actividadesList)
                     }
        }

            override fun onFailure(call: Call<List<ActividadesModelo>>, t: Throwable) {
                Log.e("Error", "Error al cargar las actividades", t)
            }
        })
    }
}