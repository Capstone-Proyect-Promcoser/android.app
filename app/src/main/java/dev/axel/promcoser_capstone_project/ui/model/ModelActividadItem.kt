package dev.axel.promcoser_capstone_project.ui.model

import dev.axel.promcoser_capstone_project.ui.fragments.home.HomeFragment

data class ModelActividadItem(
    val item_actividad_tv_actividad: String,
    val item_actividad_iv_status: Int,
    //val item_actividad_json: String? = null
    val item_actividad_json: HomeFragment.DetalleParteDiarioItem? = null
)
