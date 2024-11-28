package dev.axel.promcoser_capstone_project.ui.interfaz

import dev.axel.promcoser_capstone_project.ui.model.ModeloDetalleParteDiario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DetalleParteDiarioApi {

    // GET: Recuperar todos los registros de DetalleParteDiario
    @GET("detalleParteDiario")
    fun obtenerDetalles(): Call<List<ModeloDetalleParteDiario>>

    // POST: Agregar un nuevo registro a la tabla DetalleParteDiario
    @POST("detalleParteDiario")
    fun agregarDetalle(@Body nuevoDetalle: ModeloDetalleParteDiario): Call<ModeloDetalleParteDiario>
}
