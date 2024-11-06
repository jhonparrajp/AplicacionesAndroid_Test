package com.example.appconbasededatossqlite

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQlite(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table productos (codigo int primary key,descripcion text,precio real)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    @SuppressLint("Range")
    fun obtenerProductos(): List<Map<String, Any>> {
        val listaProductos = mutableListOf<Map<String, Any>>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM productos", null)

        if (cursor.moveToFirst()) {
            do {
                val producto = mutableMapOf<String, Any>()
                producto["codigo"] = cursor.getInt(cursor.getColumnIndex("codigo"))
                producto["descripcion"] = cursor.getString(cursor.getColumnIndex("descripcion"))
                producto["precio"] = cursor.getDouble(cursor.getColumnIndex("precio"))
                listaProductos.add(producto)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaProductos
    }

}