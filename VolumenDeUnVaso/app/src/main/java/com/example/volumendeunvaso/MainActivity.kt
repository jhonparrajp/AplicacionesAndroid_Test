package com.example.volumendeunvaso

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.PI
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    //Vamos a vincular el texview deresultado con el boton calcular
    var txtViewResultado: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        txtViewResultado = findViewById(R.id.txtViewResultado)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun calcular(view: View) {
        var txtLitrosBorracho: EditText = findViewById(R.id.txtLitrosBorracho)
        var txtDiametroVaso: EditText = findViewById(R.id.txtDiametroVaso)
        var txtAlturaVaso: EditText = findViewById(R.id.txtAlturaVaso)
        var txtVasosTomado: EditText = findViewById(R.id.txtVasosTomados)

        var alturaVaso = txtAlturaVaso.text.toString().toDouble()
        var diametro = txtDiametroVaso.text.toString().toDouble()
        var radio = diametro / 2

        var volumen = PI * radio * 2 * alturaVaso
        var mililitroTomados = volumen * txtVasosTomado.text.toString().toDouble()
        var litrosBorracho = txtLitrosBorracho.text.toString().toDouble()
        var limiteVasos = litrosBorracho * 1000 / volumen
        limiteVasos=limiteVasos.round(2)

        if (mililitroTomados > litrosBorracho * 1000) {
            txtViewResultado?.text = "Ya estas Borracho Tu limite es $limiteVasos Vasos"
        } else {
            txtViewResultado?.text = "Aun no estas tan ebrio mano , siguele"
        }
    }


    fun Double.round(decimales: Int): Double {
        var multiplicador = 1.0
        repeat(decimales) { multiplicador *= 10 }
        return round(this * multiplicador) / multiplicador
    }

}



