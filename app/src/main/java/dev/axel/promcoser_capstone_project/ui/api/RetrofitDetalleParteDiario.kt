package dev.axel.promcoser_capstone_project.ui.api

import dev.axel.promcoser_capstone_project.ui.interfaz.DetalleParteDiarioApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitDetalleParteDiario {
    private const val BASE_URL = "http://localhost:5184/api/DetalleParte" // Cambia esta URL por la de tu API

    // Instancia única de Retrofit
    val instance: DetalleParteDiarioApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // URL base de la API
            .addConverterFactory(GsonConverterFactory.create()) // Convertidor para JSON
            .build()

        // Crear el servicio que implementa la interfaz de la API
        retrofit.create(DetalleParteDiarioApi::class.java)
    }
}
