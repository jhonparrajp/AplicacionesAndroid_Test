package com.example.bluetoothserietestkotlin

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "BluetoothLog"
    private var macAddress: String = ""
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothDevice: BluetoothDevice
    private lateinit var bluetoothSocket: BluetoothSocket
    private var outputStream: OutputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Comprobar y solicitar permisos de Bluetooth
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            ), 100)
            return
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Este dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad si no hay soporte para Bluetooth
        } else if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 100)
        } else {
            connectToDevice() // Llama a la función para conectar al dispositivo
        }
    }

    private fun connectToDevice() {
        // Aquí coloca la lógica para buscar y conectar al dispositivo ESP32
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
        for (device in pairedDevices) {
            Log.d(TAG, "Device Name: ${device.name}, MAC: ${device.address}")
            if (device.name == "ESP32-SLAVE") { // Asegúrate de que el nombre del dispositivo coincida
                macAddress = device.address
                bluetoothDevice = device
                break
            }
        }

        if (macAddress.isNotEmpty()) {
            Thread {
                try {
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid)
                    bluetoothAdapter.cancelDiscovery()
                    bluetoothSocket.connect()
                    outputStream = bluetoothSocket.outputStream

                    runOnUiThread {
                        Toast.makeText(this, "Bluetooth conectado con éxito", Toast.LENGTH_LONG).show()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Error al conectar: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this, "Error al conectar a Bluetooth: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        } else {
            Toast.makeText(this, "Dispositivo ESP32-SLAVE no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permisos concedidos, intenta conectar nuevamente
                connectToDevice()
            } else {
                Toast.makeText(this, "Permiso denegado para usar Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendCommand(ledIndex: Int, value: Int) {
        if (outputStream == null) {
            Log.d(TAG, "Error en el flujo de salida")
            return
        }

        try {
            val command = when (ledIndex) {
                1 -> "R_$value\n"
                2 -> "G_$value\n"
                3 -> "B_$value\n"
                else -> ""
            }

            outputStream?.write(command.toByteArray())
            Log.d(TAG, "Comando enviado: $command")
        } catch (e: IOException) {
            Log.e(TAG, "Error al enviar comando: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (::bluetoothSocket.isInitialized && bluetoothSocket.isConnected) {
                bluetoothSocket.close()
                Log.d(TAG, "Conexión cerrada")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error al cerrar la conexión: ${e.message}")
        }
    }
}
