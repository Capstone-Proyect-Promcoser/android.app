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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dev.axel.promcoser_capstone_project.R
import dev.axel.promcoser_capstone_project.ui.model.ModeloUsuario

class Register : AppCompatActivity() {
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
        val Register_et_email: EditText = findViewById(R.id.register_et_email)
        val Register_et_psw: EditText = findViewById(R.id.register_et_psw)
        val Register_et_psw_validation: EditText = findViewById(R.id.register_et_psw_validation)
        val Register_btn_register: Button = findViewById(R.id.register_btn_register)
        val Register_btn_login: Button = findViewById(R.id.register_btn_login)
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        // En caso ya posea una cuenta
        Register_btn_login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        // En caso piense registrarse
        Register_btn_register.setOnClickListener {
            val name = Register_et_name.text.toString()
            val lastname = Register_et_lastname.text.toString()
            val email = Register_et_email.text.toString()
            val psw = Register_et_psw.text.toString()
            val psw_validation = Register_et_psw_validation.text.toString()

            if (psw != psw_validation){
                Snackbar.make(findViewById(android.R.id.content), "Las contraseñas no coinciden", Snackbar.LENGTH_SHORT).show()
            } else{
                // Se genera un usuario (se registra)
                auth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        val user: FirebaseUser? = auth.currentUser
                        val uid = user?.uid
                        val userModel = ModeloUsuario(name, lastname, email, uid)

                        db.collection("users").document(uid.toString()).set(userModel).addOnSuccessListener {
                            Snackbar.make(findViewById(android.R.id.content), "Usuario registrado", Snackbar.LENGTH_SHORT).show()
                            startActivity(Intent(this, Login::class.java))
                        }.addOnFailureListener {
                            Snackbar.make(findViewById(android.R.id.content), "Error al registrar usuario", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener{ error ->
                    Snackbar.make(findViewById(android.R.id.content), error.message.toString(), Snackbar.LENGTH_SHORT).show()
                }

            }

        }
    }
}