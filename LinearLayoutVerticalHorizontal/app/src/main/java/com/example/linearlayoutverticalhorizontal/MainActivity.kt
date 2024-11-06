package com.example.linearlayoutverticalhorizontal

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var llLogin:LinearLayout? = null
    var llRegister: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        llLogin = findViewById(R.id.llLogin)
        llRegister = findViewById(R.id.llRegistro)









        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun clickLoginRegistro(view: View) {
        val nombreCompleto=resources.getResourceName(view.id)
        println(nombreCompleto)
        val nombre=nombreCompleto.substring(nombreCompleto.lastIndexOf("/")+1)
        if (nombre =="rbLogin"){
            llLogin?.visibility=View.VISIBLE
            llRegister?.visibility=View.GONE
        }else if (nombre =="rbRegistro"){
            llLogin?.visibility=View.GONE
            llRegister?.visibility=View.VISIBLE
        }
    }











}