package dev.axel.promcoser_capstone_project.ui.Views

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.axel.promcoser_capstone_project.R

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val etDescActividad: EditText = findViewById(R.id.etDescActividad)
        val etHoraInicio: EditText = findViewById(R.id.etHoraInic)
        val etHoraFin: EditText = findViewById(R.id.etHoraFin)
        val etDescanso: EditText = findViewById(R.id.etDescanso)
        val etMovilizacion: EditText = findViewById(R.id.etMovili)
        val etHorometro: EditText = findViewById(R.id.etHorometro)
        val etCombustible: EditText = findViewById(R.id.etCombustible)
        val cbSi: CheckBox = findViewById(R.id.cbSi)
        val cbNo: CheckBox = findViewById(R.id.cbNo)
        val btnGuardar: Button = findViewById(R.id.btnGuardarFormRegistro)

        btnGuardar.setOnClickListener {
            val descripcion = etDescActividad.text.toString()
            val horaInicio = etHoraInicio.text.toString()
            val horaFin = etHoraFin.text.toString()
            val descanso = etDescanso.text.toString()
            val movilizacion = etMovilizacion.text.toString()
            val horometro = etHorometro.text.toString()
            val combustible = etCombustible.text.toString()
            val huboIncidente = when {
                cbSi.isChecked -> "Sí"
                cbNo.isChecked -> "No"
                else -> "No especificado"
            }

            if (descripcion.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty()) {
                Toast.makeText(this, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()


                println("Descripción: $descripcion")
                println("Hora Inicio: $horaInicio, Hora Fin: $horaFin")
                println("Descanso: $descanso, Movilización: $movilizacion")
                println("Horómetro: $horometro, Combustible: $combustible")
                println("Hubo incidente: $huboIncidente")
            }
        }
    }
}