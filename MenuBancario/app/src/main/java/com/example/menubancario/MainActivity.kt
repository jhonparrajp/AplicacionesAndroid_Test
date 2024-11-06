package com.example.menubancario

import com.example.menubancario.R

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //Declarando variables para los Radio Buttons
    private var rbVerSaldo: RadioButton? = null
    private var rbIngresar: RadioButton? = null
    private var rbRetirar: RadioButton? = null
    private var rbsalir: RadioButton? = null

    //Declarando una variable para el TextView
    private var tvVerSaldo: TextView? = null

    //Declarando variables para los EditText
    private var txtIngresar: EditText? = null
    private var txtRetirar: EditText? = null

    private var saldo: Double = 100.0 //Saldo inicial en el banco


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        //vinculandolos los radio buton
        rbVerSaldo = findViewById(R.id.rbVerSaldo)
        rbIngresar = findViewById(R.id.rbIngresar)
        rbRetirar = findViewById(R.id.rbRetirar)
        rbsalir = findViewById(R.id.rbSalir)
        //vinculando los Textview
        tvVerSaldo = findViewById(R.id.tvVerSaldo)
        //vinculando los EditText
        txtIngresar = findViewById(R.id.txtIngresar)
        txtRetirar = findViewById(R.id.txtRetirar)







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun accion(view: View) {
        tvVerSaldo?.visibility = View.INVISIBLE
        txtIngresar?.visibility = View.INVISIBLE
        txtRetirar?.visibility = View.INVISIBLE
        if (rbVerSaldo?.isChecked() == true) {
            tvVerSaldo?.visibility = View.VISIBLE
            tvVerSaldo?.text = "Tu Saldo Es De: $saldo"
        } else if (rbIngresar?.isChecked() == true) {
            txtIngresar?.visibility = View.VISIBLE
            tvVerSaldo?.text = "Ingreso Exitoso"
        } else if (rbRetirar?.isChecked() == true) {
            txtRetirar?.visibility = View.VISIBLE
            tvVerSaldo?.text = "Retiro Exitoso"
        } else if (rbsalir?.isChecked() == true) {
            finish() // Cierra la activity y sale de la app
        }
    }

    fun btnOk(view: View) {
        tvVerSaldo?.visibility = View.INVISIBLE
        txtIngresar?.visibility = View.INVISIBLE
        txtRetirar?.visibility = View.INVISIBLE

        if (rbVerSaldo?.isChecked() == true) {
            tvVerSaldo?.visibility = View.VISIBLE
            tvVerSaldo?.text = "Tu Saldo Es De: $saldo"
        }
        if (rbIngresar?.isChecked() == true) {
            var ingresar = txtIngresar?.text.toString().toDouble()
            saldo += ingresar
            tvVerSaldo?.visibility = View.VISIBLE
            tvVerSaldo?.text = "Tu Saldo Es De: $saldo"
            Toast.makeText(this, "Operacion Realizada Con Exito!!", Toast.LENGTH_LONG).show()
        }
        if (rbRetirar?.isChecked() == true) {

            var retirar = txtRetirar?.text.toString().toDouble()

            if (saldo - retirar < 0.0) {
                tvVerSaldo?.visibility = View.VISIBLE
                tvVerSaldo?.text = "ERROR Saldo Insuficiente,pobre caga"
            } else {
                saldo = saldo - retirar;
                tvVerSaldo?.visibility = View.VISIBLE
                tvVerSaldo?.text = "Tu saldo quedo de: $saldo"
                Toast.makeText(this, "Retiro Realizado Con Exito!!", Toast.LENGTH_LONG).show()

            }
        }
        if (rbsalir?.isChecked() == true) {
            tvVerSaldo?.text = "Adios"
            finish() // Cierra la activity y sale de la app
        }
    }


}
