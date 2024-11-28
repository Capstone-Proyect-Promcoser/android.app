package dev.axel.promcoser_capstone_project.ui.model


data class ModeloDetalleParteDiario (
    val detalleParteID: Int,
    val idParte: Int,
    val horaInicio: String?,
    val horaFinTime: String?,
    val trabajoEfectuado: String,
    val incidencias: String?,
    val estadoTarea: Boolean,
    val observaciones: String?,
    val cantidad: Double?

    )