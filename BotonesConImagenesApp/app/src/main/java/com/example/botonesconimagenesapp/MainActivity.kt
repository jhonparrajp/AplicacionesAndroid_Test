package com.example.botonesconimagenesapp

import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun saludo(view:View){
        var texto=view.contentDescription.toString()
        if (texto=="Hola"){
            Toast.makeText(this,"Hola como estas ", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Adios Que te vaya bien ", Toast.LENGTH_LONG).show()
        }
    }


}