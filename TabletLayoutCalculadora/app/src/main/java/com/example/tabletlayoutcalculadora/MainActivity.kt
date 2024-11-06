package com.example.tabletlayoutcalculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var tvRes: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvRes = findViewById(R.id.tvRest)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun calcular(view: View){
        val boton= view as Button
        val textoBoton=boton.text.toString()
        var concatenar=tvRes?.text.toString()+textoBoton
        var mostrar=removerCerosIzquierda(concatenar)

        if (textoBoton=="="){
            var resultado=0.0

        }




    }
    fun removerCerosIzquierda(str:String):String?{
        var i=0
        while(i<str.length && str[i]=='0'){
            i++
        }
        val sb = StringBuffer(str)
        sb.replace(0,i,"")
        return sb.toString()
    }
   /* fun evaluar(str: String): Double{
   //PArte logica para operaciones
    }
*/


}