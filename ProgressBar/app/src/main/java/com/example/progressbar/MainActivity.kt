package com.example.progressbar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    var barra: ProgressBar? = null


    var barraTest: ProgressBar? = null
    var textoProgreso: TextView? = null
    var texto: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        barra = findViewById(R.id.pbUno)

        barraTest = findViewById(R.id.pbUnotest)
        textoProgreso = findViewById(R.id.tvProgress)
        texto = findViewById(R.id.textView2)

        // Usa 'barraTest' en lugar de 'barra'

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun aumentar(view: View) {
        // Actualiza la barra principal (barra) y el texto asociado a ella
        barra?.progress = (barra?.progress?.plus(50))?.coerceAtMost(500)!!
        barraTest?.progress = (barraTest?.progress?.plus(50))?.coerceAtMost(1000)!!
        textoProgreso?.text = "${barra?.progress}°C" // Usa 'barra' en lugar de 'barraTest'
        texto?.text = "${barraTest?.progress} Us/Cm"
    }

    fun reducir(view: View) {
        // Actualiza la barra principal (barra) y el texto asociado a ella
        barra?.progress = (barra?.progress?.minus(10))?.coerceAtLeast(0)!!
        barraTest?.progress = (barraTest?.progress?.minus(10))?.coerceAtLeast(0)!!
        textoProgreso?.text = "${barra?.progress}°C"
        texto?.text = "${barraTest?.progress} Us/Cm"// Usa 'barra' en lugar de 'barraTest'
    }



}