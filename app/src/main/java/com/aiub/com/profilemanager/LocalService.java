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
import android.widget.Toast;

import java.util.Random;


public class LocalService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private AudioManager myAudioManager;
    private Sensor mLight;
    private Sensor mProximity;
    private Sensor mAcceleration;
    boolean isphoneFaceUp = true;
    boolean isObstacle = false;
    boolean ifShaking = false;
    boolean ifLight = true;

    private final IBinder mBinder = new LocalBinder();
    private final Random mGenerator = new Random();

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            if(sensorEvent.values[0] < 8){
                ifLight = false;
                Toast.makeText(this, "Now in Dark Mode", Toast.LENGTH_SHORT).show();
            }
            else
                ifLight = true;

        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
            if(sensorEvent.values[0] < 5) {
                isObstacle = true;
                Toast.makeText(this, "Something is there", Toast.LENGTH_SHORT).show();
            }
            else
                isObstacle = false;
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float shakingX = sensorEvent.values[0];
            if(shakingX < 0 && shakingX >0.6)
                ifShaking = true;
            else
                ifShaking = false;
            if(sensorEvent.values[2] > 0){
                isphoneFaceUp = true;
            }
            else
                isphoneFaceUp = false;

        }
        if(isphoneFaceUp && !isObstacle && !ifShaking && ifLight){
           // myAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
           //         AudioManager.VIBRATE_SETTING_OFF);
            myAudioManager.setStreamVolume(AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
        }
        else if(ifShaking && !ifLight)
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        else if(!isphoneFaceUp)
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
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
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(mLight != null){
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mProximity != null){
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return mBinder;
    }
    public int getRandomNumber(){
        return mGenerator.nextInt(100);
    }
    public void profileManager(){
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
    }

}
