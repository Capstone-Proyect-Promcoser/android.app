package dev.axel.promcoser_capstone_project.ui.ingreso

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.ui.model.ModeloUsuario
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

class Register : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        // Agregado en onResume para no perder la información de los tipos de usuario elegibles al cambiar de pantalla
        // Mostrando los tipos de usuario elegibles
        val user_types = resources.getStringArray(R.array.user_type)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, user_types)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val Register_et_name: EditText = findViewById(R.id.register_et_name)
        val Register_et_lastname: EditText = findViewById(R.id.register_et_lastname)
        val Register_et_dni: EditText = findViewById(R.id.register_et_dni)
        val Register_et_fecha_nacimiento: EditText = findViewById(R.id.register_et_fecha_nacimiento)
        val Register_et_celular: EditText = findViewById(R.id.register_et_celular)
        val Register_et_direccion: EditText = findViewById(R.id.register_et_direccion)
        val Register_et_email: EditText = findViewById(R.id.register_et_email)
        val Register_et_psw: EditText = findViewById(R.id.register_et_psw)
        val Register_et_psw_validation: EditText = findViewById(R.id.register_et_psw_validation)
        val Register_btn_register: Button = findViewById(R.id.register_btn_register)
        val Register_btn_login: Button = findViewById(R.id.register_btn_login)




        // En caso ya posea una cuenta
        Register_btn_login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        Register_btn_register.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://amusing-presumably-man.ngrok-free.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val registerService = retrofit.create(RegisterService::class.java)

            lifecycleScope.launch {
                try {
                    val registerRequest = ModeloUsuario(
                        Register_et_dni.text.toString(),
                        Register_et_name.text.toString(),
                        Register_et_lastname.text.toString(),
                        Register_et_fecha_nacimiento.text.toString(),
                        Register_et_celular.text.toString(),
                        Register_et_email.text.toString(),
                        Register_et_direccion.text.toString(),
                        Register_et_psw.text.toString()
                    )
                    val response = registerService.createUser(registerRequest)
                    Snackbar.make(findViewById(android.R.id.content), "Se ha registrado correctamente", Snackbar.LENGTH_SHORT).show()
                    startActivity(Intent(this@Register, Login::class.java))


                } catch (e: Exception) {
                    Snackbar.make(findViewById(android.R.id.content), "Error al registrar", Snackbar.LENGTH_SHORT).show()
                }
            }

        }

    }


    interface RegisterService  {
        @POST("api/Personal/CreatePersonal")
        suspend fun createUser(user: ModeloUsuario): Response
    }
    data class Response(val message: String)
}