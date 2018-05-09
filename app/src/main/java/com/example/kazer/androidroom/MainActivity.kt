package com.example.kazer.androidroom
//Code borrowed from http://mihaifonoage.blogspot.com/2010/02/getting-battery-level-in-android-using.html
//
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.os.Bundle
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager : SensorManager?= null
    private var mAccelerometer : Sensor?= null
    var LowBat = false

    private fun batteryLevel() {
        val batteryLevelReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                context.unregisterReceiver(this)
                val rawlevel = intent.getIntExtra("level", -1)
                val scale = intent.getIntExtra("scale", -1)
                var level = -1
                if (rawlevel >= 0 && scale > 0) {
                    level = rawlevel * 100 / scale
                    Log.i("battery", "Battery: "+level.toString() )
                    if(level<=15){
                        wallLightAnimate(true)
                    }
                }

            }
        }
        val batteryLevelFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryLevelReceiver, batteryLevelFilter)
    }
    private fun time() {
        val timeChangedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action

                if (action == Intent.ACTION_TIME_CHANGED) {
                    if (Calendar.HOUR_OF_DAY > 6 && Calendar.HOUR_OF_DAY < 18) {
                        Wwindow.setImageResource(R.drawable.window_day)
                        Log.i("time", "Time of day is Day")
                    } else {
                        Wwindow.setImageResource(R.drawable.window_night)
                        Log.i("time", "Time of day is Night")

                    }
                }
            }
        }
        val timeFilter = IntentFilter(Intent.ACTION_TIME_CHANGED)
        registerReceiver(timeChangedReceiver, timeFilter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        batteryLevel()
        time()
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME )
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            Log.i("accelerometer", " " + event.values[1] + " " + event.values[0])
            if(event.values[1] > 0) {
                moveBox(event.values[1])
            }else{
                moveBox(-event.values[1])
            }
          }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


    fun moveBox(f: Float){
        //move the box
        var s=""
        if( f>1){
            s = "left"
        }
        if(f<1){
            s = "right"
        }
        Log.i("boxanim","The box should be moving in the " + s + f.toString())

    }

    fun wallLightAnimate(v:Boolean){
        Log.i("anim","The light should be flashing? " + v.toString())
        if(v){
            //start animation
        }else{
            //stop animation
        }
    }

    fun timeofday(l:Long){

    }
}

