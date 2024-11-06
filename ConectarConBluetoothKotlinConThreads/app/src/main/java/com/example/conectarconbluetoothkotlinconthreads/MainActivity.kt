package com.example.conectarconbluetoothkotlinconthreads

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.UUID

import android.os.Handler
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers



class MainActivity : AppCompatActivity() {

    private val TAG = "ConexionActivityJP"
    private val REQUEST_ENABLE_BT = 1
    private val ERROR_READ = 0

    private lateinit var bluetoothActivityResultLauncher: ActivityResultLauncher<Intent>
    private var arduinoBTModule: BluetoothDevice? = null
    private var arduinoUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    var btReadings: TextView? = null
    var btDevices: TextView? = null
    var connectToDevice: Button? = null
    var searchDevices: Button? = null
    var clearValues: Button? = null

    companion object {
        lateinit var handler: Handler
    }


    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager.adapter

        btReadings = findViewById(R.id.btReadings)
        btDevices = findViewById(R.id.btDevices)
        connectToDevice = findViewById(R.id.connectToDevice)
        searchDevices = findViewById(R.id.seachDevices)
        clearValues = findViewById(R.id.refresh)

        Log.d(TAG, "Comenzado a Ejecutar")

        handler = Handler(Looper.getMainLooper()) { msg ->
            when (msg.what) {
                ERROR_READ -> {
                    val arduinoMsg = msg.obj.toString()
                    btReadings?.text = arduinoMsg
                }
            }
            true
        }

        clearValues?.setOnClickListener {
            btReadings?.text = ""
            btDevices?.text = ""
        }

        val connectToBTObservable: Observable<String> = Observable.create { emitter ->

            val context = this // Context de la actividad
            Log.d(TAG, "LLamando a connectThread class")
            val conectThread = ConectThread(context ,arduinoBTModule, arduinoUUID, handler)
            conectThread.run()

            if (conectThread.mmSocket?.isConnected == true) {
                Log.d(TAG, "LLamando a connectedThread class")
                val connectedThread = ConnectecThread(conectThread.mmSocket!!)
                connectedThread.run()


                connectedThread.valueRead?.let {
                    emitter.onNext(it)
                }
                connectedThread.cancel()
            }
            conectThread.cancel()
            emitter.onComplete()


        }

        connectToDevice?.setOnClickListener {
            btReadings?.text = ""
            if (arduinoBTModule != null) {
                connectToBTObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe({ valueRead ->
                        btReadings?.text = valueRead
                    }, { error ->
                        Log.d(TAG, "Error en la conexion: $error")
                    })
            }
        }

        bluetoothActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "Bluetooth Habilitado")
            } else {
                Log.d(TAG, "el usuario desabilito el Bluetooth ")
            }
        }

        searchDevices?.setOnClickListener {
            if (bluetoothAdapter == null) {
                Log.d(TAG, "Device doesn't support Bluetooth")
            } else {
                Log.d(TAG, "Device supports Bluetooth")
                if (!bluetoothAdapter.isEnabled) {
                    Log.d(TAG, "Bluetooth is disabled")
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    bluetoothActivityResultLauncher.launch(enableBtIntent)
                } else {
                    Log.d(TAG, "Bluetooth is enabled")
                }

                // Verifica los permisos de Bluetooth en Android 12+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Solicita el permiso BLUETOOTH_CONNECT
                    val REQUEST_BLUETOOTH_PERMISSION = 0
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                        REQUEST_BLUETOOTH_PERMISSION
                    )
                    return@setOnClickListener // Detén la ejecución hasta que el permiso sea concedido
                }

                // Obtener dispositivos emparejados
                var btDevicesString = ""
                val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
                if (pairedDevices.isNotEmpty()) {
                    for (device in pairedDevices) {
                        val deviceName = device.name
                        val deviceHardwareAddress = device.address
                        Log.d(TAG, "deviceName: $deviceName")
                        Log.d(TAG, "deviceHardwareAddress: $deviceHardwareAddress")
                        btDevicesString += "$deviceName || $deviceHardwareAddress\n"

                        if (deviceName == "ESP32-BT-Slave") {
                            Log.d(TAG, "ESP32 found")
                            arduinoUUID = device.uuids[0].uuid
                            arduinoBTModule = device
                            connectToDevice?.isEnabled = true
                        }
                    }
                    btDevices?.text = btDevicesString
                }
            }
            Log.d(TAG, "Button Pressed")
        }

    }
}