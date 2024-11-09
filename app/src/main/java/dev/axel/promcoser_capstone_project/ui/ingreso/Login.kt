package dev.axel.promcoser_capstone_project.ui.ingreso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.axel.promcoser_capstone_project.MainActivity
import dev.axel.promcoser_capstone_project.R

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
        val auth = Firebase.auth

        // En caso se quiera registrar
        Login_btn_register.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        // Para el acceso a la cuenta
        Login_btn_login.setOnClickListener {
            val email = Login_et_email.text.toString()
            val psw = Login_et_psw.text.toString()

            auth.signInWithEmailAndPassword(email, psw).addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    // En caso si se pueda logear
                    Snackbar.make(findViewById(android.R.id.content), "Bienvenido", Snackbar.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    // En caso de que no se pueda logear
                    Snackbar.make(findViewById(android.R.id.content), "Error al iniciar sesión", Snackbar.LENGTH_SHORT).show()

                }
            }

        }

    }
}