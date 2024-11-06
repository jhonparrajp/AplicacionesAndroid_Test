package com.example.sistemadecobro

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var txtPrecio: EditText? = null
    private var textViewLista: TextView? = null
    private var textViewTotal: TextView? = null
    private var txtPago: EditText? = null
    private var txtCambio: TextView? = null

    private var contador: Int = 0
    private var listaProducto = ArrayList<Double>()
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        txtPrecio = findViewById(R.id.txtPrecio)
        textViewLista = findViewById(R.id.textViewLista)
        textViewTotal = findViewById(R.id.textViewTotal)
        txtPago = findViewById(R.id.txtPago)
        txtCambio = findViewById(R.id.textViewCambio)




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun agregarProducto(view: View) {
        var precio: Double = txtPrecio?.text.toString().toDouble()
        listaProducto.add(precio)
        textViewLista?.text =
            textViewLista?.text.toString() + "\n" + "Producto ${contador + 1} = %.2f".format(precio)
        contador++
        txtPrecio?.setText("")
        total = 0.0
        listaProducto.forEach {
            total += it
        }
        textViewTotal?.text = "Total = %.2f".format(total)
    }

    fun pagar(view: View) {
        var pago: Double = txtPago?.text.toString().toDouble()
        var cambio = pago - total
        var centavos = cambio - cambio.toInt()
        if (centavos > 0.0) {
            // Muestra dialogo para donar centavos
            val dialogo: AlertDialog.Builder = AlertDialog.Builder(this)
            dialogo.setTitle("Deseas Donar Tus Centavos?")
            dialogo.setMessage(
                "Tienes %.2f centavos de cambio".format(centavos) + " " + " centavos " +
                        "haz click para donar"
            )
            dialogo.setPositiveButton("Donar") { dialog, which ->
                Toast.makeText(
                    this, "Gracias por donar %.2f".format(centavos) +
                            "Centavos", Toast.LENGTH_SHORT
                ).show()
                txtCambio?.text = "Su cambio es de  %.2f".format(cambio - centavos) + "Centavos"
            }
            dialogo.setNegativeButton("Cancelar") { dialog, which ->
                Toast.makeText(this, "Cambio: %.2f".format(cambio), Toast.LENGTH_SHORT).show()
                txtCambio?.text = "Su cambio es de  %.2f".format(cambio)
            }
            //creando y mostrando la alterta
            val alerta: AlertDialog = dialogo.create()
            alerta.setCanceledOnTouchOutside(false)
            alerta.show()
        } else {
            txtCambio?.text = "Su cambio es de  %.2f".format(cambio)
        }
    }


}