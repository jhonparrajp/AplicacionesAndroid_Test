package com.example.tabletlayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var matriz= arrayOf(
        arrayOf(1,2,3,4,5,6,7,8),
        arrayOf(4,5,6,7,8,9,2,1),
        arrayOf(7,8,9,2,3,4,1,5)
    )
    var tlTabla:TableLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tlTabla=findViewById(R.id.tlTablas)
        tlTabla?.removeAllViews()

        for (i in (0 until matriz.count())) {
            val registro = LayoutInflater.from(this).inflate(R.layout.row_table_jp,
                null, false)
            val tv0=registro.findViewById<View>(R.id.tv0) as TextView
            val tv1=registro.findViewById<View>(R.id.tv1) as TextView
            val tv2=registro.findViewById<View>(R.id.tv2) as TextView
            val tv3=registro.findViewById<View>(R.id.tv3) as TextView
            val tv4=registro.findViewById<View>(R.id.tv4) as TextView
            val tv5=registro.findViewById<View>(R.id.tv5) as TextView
            val tv6=registro.findViewById<View>(R.id.tv6) as TextView
            val tv7=registro.findViewById<View>(R.id.tv7) as TextView




            tv0.text=matriz[i][0].toString()
            tv1.text=matriz[i][1].toString()
            tv2.text=matriz[i][2].toString()
            tv3.text=matriz[i][3].toString()
            tv4.text=matriz[i][4].toString()
            tv5.text=matriz[i][5].toString()
            tv6.text=matriz[i][6].toString()
            tv7.text=matriz[i][7].toString()



            tlTabla?.addView(registro)
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}