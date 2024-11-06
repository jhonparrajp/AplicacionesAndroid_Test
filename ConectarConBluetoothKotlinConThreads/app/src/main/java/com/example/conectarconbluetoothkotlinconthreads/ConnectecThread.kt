package com.example.conectarconbluetoothkotlinconthreads

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectecThread(private val mmSocket: BluetoothSocket):Thread() {
    private val TAG="ConnectJP";
    private val mmInStream: InputStream? = mmSocket.inputStream
    private val mmOutStream: OutputStream? = mmSocket.outputStream
    var valueRead: String?= null

    override fun run() {
        val buffer = ByteArray(1024)
        var bytes = 0
        var numerOfReadings= 0

        while(numerOfReadings <1){
            try {
                buffer[bytes] = mmInStream?.read()?.toByte() ?: break
                if (buffer[bytes] == '\n'.code.toByte()){

                    valueRead = String(buffer, 0, bytes)
                    Log.e(TAG, valueRead!!)
                    bytes = 0
                    numerOfReadings++
                }else{
                    bytes++
                }
            }catch (e: IOException){
                Log.e(TAG, "Error reading inpu stream desconectado",e)
                break
            }
        }
    }

    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error Cerrando Socket", e)
        }
    }
}