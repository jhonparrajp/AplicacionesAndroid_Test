package com.example.appconbasededatossqlite

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var txtCodigo: EditText? = null
    var txtDescripcion: EditText? = null
    var txtPrecio: EditText? = null

    var tlProducto: TableLayout? = null

    var txtBuscarPorCodigo: EditText? = null

    var spBuscarPor: Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        txtCodigo = findViewById(R.id.txtCodigo)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtBuscarPorCodigo = findViewById(R.id.txtBuscarPorCodigo)
        spBuscarPor = findViewById(R.id.spBuscarPor)

        var listaDeCampos = arrayOf(
            "Seleccione el campo a buscar",
            "codigo", "descripcion", "precio"
        )
        var adaptador: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaDeCampos)
        spBuscarPor?.adapter=adaptador



        tlProducto = findViewById(R.id.tlProductos)
        llenarTabla()



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun insertarRegistros(view: View) {
        //instancioamos la clase
        var conexion = SQlite(this, "tienda", null, 1)
        var baseDatos = conexion.writableDatabase

        var codigo = txtCodigo?.text.toString()
        var descripcion = txtDescripcion?.text.toString()
        var precio = txtPrecio?.text.toString()

        if (codigo.isEmpty() == false && descripcion.isEmpty() == false
            && precio.isEmpty() == false
        ) {
            var registro = ContentValues()
            registro.put("codigo", codigo)
            registro.put("descripcion", descripcion)
            registro.put("precio", precio)
            baseDatos.insert("productos", null, registro)

            txtCodigo?.setText("")
            txtDescripcion?.setText("")
            txtPrecio?.setText("")
            Toast.makeText(this, "Su Registro se ah insertado exitosamente", Toast.LENGTH_LONG)
                .show()

            // Consultar y mostrar productos
            mostrarProductos(conexion) // Llamada a mostrarProductos

        } else {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
        }
        llenarTabla()
        baseDatos.close()
    }

    private fun mostrarProductos(conexion: SQlite) {
        val productos = conexion.obtenerProductos()
        for (producto in productos) {
            Log.d(
                "DB_DATA",
                "Código: ${producto["codigo"]}, Descripción: ${producto["descripcion"]}, Precio: ${producto["precio"]}"
            )
        }
    }

    fun buscar(view: View) {
        val conexion = SQlite(this, "tienda", null, 1)
        val baseDatos = conexion.writableDatabase
        val codigo = txtCodigo?.text.toString()
        if (codigo.isEmpty() == false) {
            val fila = baseDatos.rawQuery(
                "select descripcion,precio from productos where codigo = '$codigo'",
                null,
                null
            )
            if (fila.moveToFirst() == true) {
                txtDescripcion?.setText(fila.getString(0))
                txtPrecio?.setText(fila.getString(1))
                Toast.makeText(this, "Producto encontrado", Toast.LENGTH_LONG).show()
                baseDatos.close()
            } else {
                txtDescripcion?.setText("")
                txtPrecio?.setText("")
                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_LONG).show()
                baseDatos.close()
            }
        }
        llenarTabla()
    }

    fun eliminar(view: View) {

        val conexion = SQlite(this, "tienda", null, 1)
        val baseDatos = conexion.writableDatabase
        val codigo = txtCodigo?.text.toString()

        if (codigo.isEmpty() == false) {
            val cant = baseDatos.delete("productos", "codigo='" + codigo + "'", null)
            if (cant > 0) {
                Toast.makeText(this, "El producto fue eliminado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No se encontró el producto para eliminar", Toast.LENGTH_LONG)
                    .show()
            }
            txtCodigo?.setText("")
            txtDescripcion?.setText("")
            txtPrecio?.setText("")
        } else {
            Toast.makeText(this, "Debe llenar el código del producto", Toast.LENGTH_LONG).show()
        }
        llenarTabla()
    }

    fun editar(view: View) {
        val conexion = SQlite(this, "tienda", null, 1)
        val baseDatos = conexion.writableDatabase
        val codigo = txtCodigo?.text.toString()
        val descripcion = txtDescripcion?.text.toString()
        val precio = txtPrecio?.text.toString()

        if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()) {
            val registro = ContentValues()
            registro.put("precio", precio)
            registro.put("descripcion", descripcion)
            registro.put("codigo", codigo)

            val cant = baseDatos.update("productos", registro, "codigo='$codigo'", null)
            if (cant > 0) {
                Toast.makeText(this, "El producto fue editado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No se encontró el producto para editar", Toast.LENGTH_LONG)
                    .show()
            }

        } else {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
        }
        llenarTabla()
    }

    fun llenarTabla() {
        tlProducto?.removeAllViews()
        val conexion = SQlite(this, "tienda", null, 1)
        val baseDatos = conexion.writableDatabase

        val buscarPor = txtBuscarPorCodigo?.text.toString()
        val listaBuscarPor=spBuscarPor?.selectedItem.toString()
        var sql = ""
        if (!buscarPor.isEmpty()) {
            if (listaBuscarPor=="codigo") {
                sql = "select codigo,descripcion,precio from productos where codigo='$buscarPor'"
            }else if (listaBuscarPor=="descripcion") {
                sql = "select codigo,descripcion,precio from productos where descripcion like '%$buscarPor%'"
            }else if (listaBuscarPor=="precio"){
                sql = "select codigo,descripcion,precio from productos where precio=$buscarPor"
            }else{
                sql = "select codigo,descripcion,precio from productos"
            }

        } else {
            sql = "select codigo,descripcion,precio from productos"
        }

        val fila = baseDatos.rawQuery(sql, null)
        fila.moveToFirst()
        do {
            val registro =
                LayoutInflater.from(this).inflate(R.layout.item_tablet_layout_jp, null, false)
            val tvCodigo = registro.findViewById<View>(R.id.tvCodigo) as TextView
            val tvDescripcion = registro.findViewById<View>(R.id.tvDescripcion) as TextView
            val tvPrecio = registro.findViewById<View>(R.id.tvPrecio) as TextView

            tvCodigo.setText(fila.getString(0))
            tvDescripcion.setText(fila.getString(1))
            tvPrecio.setText(fila.getString(2))
            tlProducto?.addView(registro)
        } while (fila.moveToNext())
    }

    fun clickRegistro(view: View) {
        resetColorRegistro()
        view.setBackgroundColor(Color.GRAY)
        val registro = view as TableRow
        val controlCodigo = registro.getChildAt(0) as TextView
        val codigo = controlCodigo.text.toString()

        val conexion = SQlite(this, "tienda", null, 1)
        val baseDatos = conexion.writableDatabase

        if (!codigo.isEmpty()) {
            val fila = baseDatos.rawQuery(
                "select codigo,descripcion,precio from productos where codigo = $codigo",
                null
            )
            if (fila.moveToFirst()) {
                txtCodigo?.setText(fila.getString(0))
                txtDescripcion?.setText(fila.getString(1))
                txtPrecio?.setText(fila.getString(2))
            } else {
                txtCodigo?.setText("")
                txtDescripcion?.setText("")
                txtPrecio?.setText("")
                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_LONG).show()
            }

        }

    }

    fun resetColorRegistro() {
        for (i in 0..tlProducto!!.childCount) {
            val registro = tlProducto?.getChildAt(i)
            registro?.setBackgroundColor(Color.WHITE)
        }
    }

    fun clickBotonBuscar(view: View) {
        llenarTabla()
    }

}