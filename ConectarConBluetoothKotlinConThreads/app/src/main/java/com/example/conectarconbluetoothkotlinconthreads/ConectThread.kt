package com.example.conectarconbluetoothkotlinconthreads

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID



@RequiresApi(Build.VERSION_CODES.S)
class ConectThread(private val context: Context,
                   private val device: BluetoothDevice?,
                   private val uuid: UUID,
                   private val handler: Handler
): Thread() {

        private val TAG= "conexionJP";
        var mmSocket: BluetoothSocket? = null

    init {
      if (ActivityCompat.checkSelfPermission(
              context,
          Manifest.permission.BLUETOOTH_CONNECT
      )!=PackageManager.PERMISSION_GRANTED){

          ActivityCompat.requestPermissions(
              context as Activity,
              arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
              1
          )
      }
        try {
            mmSocket = device?.createInsecureRfcommSocketToServiceRecord(
                uuid
            )
        }catch (e: IOException){
            Log.e(TAG,"Creacion de metodo de socket fallido",e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun run() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    1
                )
                return
            }
            mmSocket?.connect()
        }catch (connectException: IOException){
            Log.e(TAG,"Conexión fallida",connectException)
            try {
                mmSocket?.close()
            }catch (closeException: IOException){
                Log.e(TAG,"Cierre de socket fallido",closeException)
            }
            return
        }
    }

    fun cancel() {
        try {
            mmSocket?.close()
        } catch (closeException: IOException){
            Log.e(TAG,"Cierre de socket fallido",closeException)
        }
    }

}