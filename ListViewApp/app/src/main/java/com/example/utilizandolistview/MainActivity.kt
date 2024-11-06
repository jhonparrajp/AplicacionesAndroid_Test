package com.example.utilizandolistview

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var lvLenguajes: ListView? = null
    private var tvVer:TextView?=null
    //Arreglo de elementos
    private val lenguajes = arrayOf("Java",
        "Kotlin", "C++", "Python", "JavaScript","PHP",
        "React Js","Cobol")
    private val posiciones = arrayOf("1","2","3","4","5","6","7")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        lvLenguajes = findViewById(R.id.lvLenguajes)
        tvVer = findViewById(R.id.txtVer)

        var adaptador:ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.list_view_jhon,lenguajes)
        lvLenguajes?.adapter = adaptador
        //Cuando se selecciona un elemento en la lista
        lvLenguajes?.setOnItemClickListener { adapterView, view, position, id ->
            tvVer?.text = "Posición: ${posiciones[position]} \nLenguaje: ${adapterView.getItemAtPosition(position)}"
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


}