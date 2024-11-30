package dev.axel.promcoser_capstone_project.ui.model

import androidx.lifecycle.ViewModel
import dev.axel.promcoser_capstone_project.ui.ingreso.Login

class SharedViewModel: ViewModel()  {
    var loginResponse: Login.ExtendedLoginResponse? = null
}