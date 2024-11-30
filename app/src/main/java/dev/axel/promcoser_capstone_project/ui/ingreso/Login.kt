package dev.axel.promcoser_capstone_project.ui.ingreso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dev.axel.promcoser_capstone_project.MainActivity
import dev.axel.promcoser_capstone_project.R
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val Login_et_email: EditText = findViewById(R.id.login_et_email)
        val Login_et_psw: EditText = findViewById(R.id.login_et_psw)
        val Login_btn_login: Button = findViewById(R.id.login_btn_login)
        val Login_btn_register: Button = findViewById(R.id.login_btn_register)


        // En caso se quiera registrar
        Login_btn_register.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        Login_btn_login.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://amusing-presumably-man.ngrok-free.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val authService = retrofit.create(AuthService::class.java)

            lifecycleScope.launch {
                try {
                    val loginRequest = LoginRequest(Login_et_email.text.toString(), Login_et_psw.text.toString())
                    val loginResponse = authService.iniciarSesion(loginRequest)

                    // Usando los valores obtenidos
                    val nombre = loginResponse.nombres

                    Snackbar.make(findViewById(android.R.id.content), "Bienvenido $nombre", Snackbar.LENGTH_SHORT).show()

                    // Añadiendo campos adicionales a la respuesta
                    val extendedLoginResponse = ExtendedLoginResponse(loginResponse.nombres, loginResponse.apellidos, loginResponse.token, loginResponse.isEmailSent, Login_et_email.text.toString())


                    // Pasando la data al siguiente activity
                    val gson = Gson()
                    val extendedLoginResponseJson = gson.toJson(extendedLoginResponse)

                    startActivity(Intent(this@Login, MainActivity::class.java).putExtra("loginResponseJson", extendedLoginResponseJson))
                } catch (e: HttpException) {
                    if (e.code() == 400) {
                        Snackbar.make(findViewById(android.R.id.content), "Credenciales incorrectas", Snackbar.LENGTH_SHORT).show()
                    } else {
                        // Handle other HTTP errors
                    }
                }  catch (e: Exception) {
                    // Handle errors
                }
            }
        }


    }

    interface AuthService {
        @POST("api/Personal/IniciarSesion")
        suspend fun iniciarSesion(@Body loginRequest: LoginRequest): LoginResponse
    }

    data class LoginRequest(val dni: String, val password: String)
    data class LoginResponse(val nombres: String, val apellidos: String, val token: String, val isEmailSent: Boolean)
    data class ExtendedLoginResponse(val nombres: String, val apellidos: String, val token: String, val isEmailSent: Boolean, val dni: String)
}