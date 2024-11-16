package dev.axel.promcoser_capstone_project.ui.model

import com.google.firebase.Timestamp

data class ModeloUsuario(
    val u_id: String? = "",
    val u_id_rol: String = "",
    val u_nombre: String = "",
    val u_apellido: String = "",
    val u_email: String = "",
    val u_fecha_registro: Timestamp = Timestamp.now(),
    val u_estado: Boolean = true,
)
