package com.example.interfazcalculadoraedad

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.interfazcalculadoraedad.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    //Sirve para las vistas
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Siempre se pone
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        actualizarFecha()
        binding.bttnFechaNacimiento.setOnClickListener { mostrarDatePicker() }
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) actualizarFecha()
    }

    //Interfaz de seleccion
    private fun mostrarDatePicker() {
        val hoy = LocalDate.now()

        val listener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker,
                year: Int,
                monthOfYear: Int,
                dayOfMonth: Int
            ) {
                val mesNacimiento = monthOfYear + 1

                val resultado = calculaDias(
                    hoy.monthValue,
                    mesNacimiento,
                    hoy.dayOfMonth,
                    dayOfMonth,
                    hoy.year,
                    year
                )

                binding.txtNaciste.text = "Naciste el $dayOfMonth/$mesNacimiento/$year"
                binding.txtTextoGrande.text = "Tienes ${resultado[0]} años y ${resultado[1]} días"
            }
        }

        val dialogo = DatePickerDialog(
            this, listener,
            hoy.year, hoy.monthValue - 1, hoy.dayOfMonth
        )
        dialogo.show()
    }

    //Pongo en el hoy es la fecha de hoy
    private fun actualizarFecha() {
        val hoy = LocalDate.now()
        val formateador = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault())
        binding.txtDiaHoy.text = getString(R.string.hoy_es, formateador.format(hoy))
    }

    //Funcion Jose Antonio
    private fun calculaDias(
        mesActual: Int,
        mesNacimiento: Int,
        diaActual: Int,
        diaNacimiento: Int,
        anhoActual: Int,
        anhoNacimiento: Int
    ): IntArray {
        var dias = 0
        val calculo = IntArray(2)
        var anhos = anhoActual - anhoNacimiento
        if (mesActual >= mesNacimiento) {
            for (i in mesNacimiento until mesActual) dias += numeroDeDias(i, anhoActual)
            if (diaActual < diaNacimiento && mesActual == mesNacimiento) {
                anhos--
                dias = 365 + diaActual - diaNacimiento
            } else {
                dias += diaActual - diaNacimiento
            }
        } else {
            anhos--
            dias = diasRestantes(diaNacimiento, mesNacimiento, anhoActual - 1)
            val veces = 12 + mesActual - mesNacimiento - 1
            var mesVer = mesNacimiento + 1
            var anhoVer = anhoActual - 1
            for (i in 1..veces) {
                if (mesVer > 12) {
                    mesVer = 1
                    anhoVer++
                }
                dias += numeroDeDias(mesVer, anhoVer)
                mesVer++
            }
            dias += diaActual
        }
        calculo[0] = anhos
        calculo[1] = dias
        return calculo
    }

    private fun esBisiesto(anho: Int): Boolean {
        var bisiesto = false
        if (anho > 1582) {
            if (anho % 400 == 0 || anho % 4 == 0 && anho % 100 != 0) {
                bisiesto = true
            }
        } else {
            bisiesto = false
        }
        return bisiesto
    }

    private fun numeroDeDias(m: Int, a: Int): Int {
        return when (m) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            else -> if (esBisiesto(a)) {
                29
            } else {
                28
            }
        }
    }

    private fun diasRestantes(dia: Int, mes: Int, anho: Int): Int {
        return numeroDeDias(mes, anho) - dia
    }
}