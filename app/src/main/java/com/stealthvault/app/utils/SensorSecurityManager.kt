package com.stealthvault.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class SensorSecurityManager(
    private val onTrigger: () -> Unit
) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var context: Context? = null
    private var isRunning = false

    // Accelerometer
    private var acceleration = 10f
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH

    // Screen Off Receiver
    private val screenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                onTrigger()
            }
        }
    }

    fun start(context: Context, isEnabled: Boolean) {
        if (!isEnabled || isRunning) return
        this.context = context
        isRunning = true

        // Register Sensors
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        
        val proximity = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (proximity != null) {
            sensorManager?.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // Register Screen Off Receiver
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        context.registerReceiver(screenOffReceiver, filter)
    }

    fun stop() {
        if (!isRunning) return
        isRunning = false

        sensorManager?.unregisterListener(this)
        try {
            context?.unregisterReceiver(screenOffReceiver)
        } catch (e: Exception) {
            // Ignore if not registered
        }
        context = null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!isRunning) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt(x * x + y * y + z * z)
            val delta = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            
            // 12 is a reasonable threshold for a firm shake
            if (acceleration > 12) {
                onTrigger()
            }
        } else if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
            val distance = event.values[0]
            // If distance is less than the maximum range, it's typically covered (e.g. face down or covered by hand)
            // Some sensors return 0 for near, or a small value like 3.0 or 5.0 for far.
            // We use a safe threshold: if distance < maximumRange and < 2.0cm, we consider it covered.
            if (distance < 2.0f && distance < event.sensor.maximumRange) {
                onTrigger()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
