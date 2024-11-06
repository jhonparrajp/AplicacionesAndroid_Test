package com.example.checkboxapp

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var chKotlin: CheckBox? = null
    private var chJava: CheckBox? = null
    private var chC: CheckBox? = null
    private var chPhp: CheckBox? = null
    private var txtVer: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        chKotlin = findViewById(R.id.chKotlin)
        chJava = findViewById(R.id.chJava)
        chC = findViewById(R.id.chC)
        chPhp = findViewById(R.id.chPHP)
        txtVer = findViewById(R.id.textView2)




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun accion(view: View) {
        var seleccion:String=""
        if (chKotlin?.isChecked==true){
            seleccion+="Usted a Seleccionado ${chKotlin?.text}\n"
        }
        if (chJava?.isChecked==true){
            seleccion+="Usted a Seleccionado ${chJava?.text}\n"
        }
        if (chC?.isChecked==true){
            seleccion+="Usted a Seleccionado ${chC?.text}\n"
        }
        if (chPhp?.isChecked==true){
            seleccion+="Usted a Seleccionado ${chPhp?.text}\n"
        }
        if (seleccion.isEmpty()==true){
            txtVer?.text="Selecciona un lenguaje o varios"
        }else{
            txtVer?.text=seleccion
        }


    }


}