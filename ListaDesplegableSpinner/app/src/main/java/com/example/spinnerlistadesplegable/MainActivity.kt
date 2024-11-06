package com.example.spinnerlistadesplegable

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var SpLenguaje:Spinner?=null
    private var tvSeleccion:TextView?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        SpLenguaje = findViewById(R.id.spLenguajes)
        tvSeleccion = findViewById(R.id.tvSeleccion)

        val listaLenguaje= arrayOf("Selecciona un lenguaje",
            "Kotlin","Java","C++", "PHP")
        //Con R.layout.spinner_items_programadorjhon ligamos el archivo xml
        var adaptador:ArrayAdapter<String> = ArrayAdapter(this,
            R.layout.spinner_items_programadorjhon, listaLenguaje)
        SpLenguaje?.adapter=adaptador

        SpLenguaje?.onItemSelectedListener=object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2>0){
                    tvSeleccion?.text="Seleccionaste : " +SpLenguaje?.getSelectedItem().toString()
                }else{
                    tvSeleccion?.text="no haz selecionado un lenguaje"
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                tvSeleccion?.text="no haz selecionado un lenguaje"
            }

        }





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }




}