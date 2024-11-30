package dev.axel.promcoser_capstone_project.ui.model

data class ModeloUsuario(
    val dni: String? = "",
    val nombres: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val celular: String = "",
    val correo: String = "",
    val direccion: String = "",
    val contrasenia: String = "",
    val estado: Boolean = true,
)
