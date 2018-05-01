package com.aiub.com.profilemanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;


public class LocalService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private AudioManager myAudioManager;
    private Sensor mLight;
    private Sensor mProximity;
    private Sensor mAcceleration;
    boolean isPhoneFaceUp = true;
    boolean isObstacle = false;
    boolean ifShaking = false;
    boolean ifLight = true;

    private final IBinder mBinder = new LocalBinder();
    private final Random mGenerator = new Random();

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            if(sensorEvent.values[0] < 10){
                ifLight = false;
                //Toast.makeText(this, "Now in Dark Mode", Toast.LENGTH_SHORT).show();
            }
            else
                ifLight = true;

        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
            if(sensorEvent.values[0] < 5) {
                isObstacle = true;
                //Toast.makeText(this, "Something is there", Toast.LENGTH_SHORT).show();
            }
            else
                isObstacle = false;
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float shakingX = sensorEvent.values[0];
            if(shakingX < 0 && shakingX >0.6)
                ifShaking = false;
            else
                ifShaking = true;
            if(sensorEvent.values[2] > 0){
                isPhoneFaceUp = true;
            }
            else
                isPhoneFaceUp = false;

        }
        if(isPhoneFaceUp && !isObstacle && ifLight){
           // myAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
           //         AudioManager.VIBRATE_SETTING_OFF);
            myAudioManager.setStreamVolume(AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
            Log.i("Condition 1", "MAX VOLUME");
        }
        else if(!isPhoneFaceUp && !ifLight && isObstacle) {
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Log.i("Condition 3", "SILENT");
        }
        else if(ifShaking && !ifLight) {
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            myAudioManager.setStreamVolume(AudioManager.STREAM_RING, myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING)/2, 0);
            Log.i("Condition 2", "NORMAL");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class LocalBinder extends Binder{
        LocalService getService(){
            return LocalService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public int getRandomNumber(){
        return mGenerator.nextInt(100);
    }
//    public void profileManager(){
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        if(mLight != null){
//            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//        if(mProximity != null){
//            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(mLight != null){
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mProximity != null){
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mAcceleration != null)
            mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }
}
