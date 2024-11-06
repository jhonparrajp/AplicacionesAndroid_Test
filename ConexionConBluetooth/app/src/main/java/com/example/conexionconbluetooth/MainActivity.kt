package com.example.conexionconbluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.InputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var receivedDataTextView: TextView

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var pairedDevicesList = ArrayList<BluetoothDevice>()

    private val UUID_CODE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_dispositivos)
        receivedDataTextView = findViewById(R.id.txt_received_data)

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        // Verificar si el dispositivo soporta Bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no está disponible en este dispositivo", Toast.LENGTH_LONG).show()
            return
        }

        requestPermissionsIfNeeded()

        findViewById<Button>(R.id.btn_led_1).setOnClickListener {
            sendBluetoothData(1)
        }

        findViewById<Button>(R.id.btn_led_2).setOnClickListener {
            sendBluetoothData(2)
        }
    }

    private fun requestPermissionsIfNeeded() {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1)
        } else {
            showPairedDevices()
        }
    }

    private fun showPairedDevices() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            val pairedDevices = bluetoothAdapter?.bondedDevices
            if (pairedDevices != null && pairedDevices.isNotEmpty()) {
                val deviceNames = pairedDevices.map { it.name }
                pairedDevicesList.addAll(pairedDevices)

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames)
                listView.adapter = adapter

                listView.setOnItemClickListener { _, _, position, _ ->
                    connectToDevice(position)
                }
            } else {
                Toast.makeText(this, "No hay dispositivos vinculados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun connectToDevice(position: Int) {
        val device = pairedDevicesList[position]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID_CODE)
                bluetoothSocket?.connect()
                Toast.makeText(this, "Conectado a ${device.name}", Toast.LENGTH_SHORT).show()

                // Empezar a recibir datos
                startListeningForData()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendBluetoothData(data: Int) {
        if (bluetoothSocket?.isConnected == true) {
            try {
                bluetoothSocket?.outputStream?.write(data)
                Toast.makeText(this, "Dato $data enviado", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error al enviar dato", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Conéctate primero a un dispositivo", Toast.LENGTH_SHORT).show()
        }
    }

    // Iniciar escucha de datos Bluetooth
    private fun startListeningForData() {
        val inputStream: InputStream? = bluetoothSocket?.inputStream
        if (inputStream != null) {
            Thread {
                try {
                    val buffer = ByteArray(1024)
                    var bytes: Int

                    while (bluetoothSocket?.isConnected == true) {
                        bytes = inputStream.read(buffer)
                        if (bytes > 0) {
                            val dataReceived = String(buffer, 0, bytes)
                            handler.post {
                                receivedDataTextView.text = dataReceived
                                Toast.makeText(this, "Datos recibidos: $dataReceived", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    override fun onPause() {
        super.onPause()
        bluetoothSocket?.close()
    }

    private val REQUEST_BLUETOOTH_PERMISSIONS = 1

    private fun checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            ActivityCompat.requestPermissions(this, permissions, REQUEST_BLUETOOTH_PERMISSIONS)
        } else {
            // Handle permissions for older versions
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showPairedDevices()
            } else {
                Toast.makeText(this, "Se necesitan permisos para conectar al Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }
    }
}