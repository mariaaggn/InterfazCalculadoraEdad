package com.example.interfazcalculadoraedad

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.interfazcalculadoraedad.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    //Declaro la variable binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Obtener fecha actual
        actualizarFecha()
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) actualizarFecha()
    }

    private fun actualizarFecha() {
        // LocalDate.monthValue ya es 1..12, no hay que sumarle 1
        val hoy = LocalDate.now()
        val formateador = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.getDefault())
        binding.txtDiaHoy.text = getString(R.string.hoy_es, formateador.format(hoy))
    }
}
