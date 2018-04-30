package com.aiub.com.profilemanager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LocalService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
     @Override
    protected void onStart(){
        super.onStart();
     }
     @Override
    protected void onStop(){
        super.onStop();
     }
     public void startBoundService(View v){
         Intent intent = new Intent(this, LocalService.class);
         bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
         //mService.profileManager();
         Toast.makeText(this, "Bound Service Started", Toast.LENGTH_SHORT).show();
     }
     public void stopBoundService(View v){
         unbindService(mConnection);
         mBound = false;
         Toast.makeText(this, "Bound Service Stopped", Toast.LENGTH_SHORT).show();
     }
     public void testBoundServiceRunning(View v){
         if(mBound){
             int num = mService.getRandomNumber();
             Toast.makeText(this, "number : " + num, Toast.LENGTH_SHORT).show();
         }
     }
     private ServiceConnection mConnection = new ServiceConnection() {
         @Override
         public void onServiceConnected(ComponentName componentName, IBinder service) {
             LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
             mService = binder.getService();
             mBound = true;
         }

         @Override
         public void onServiceDisconnected(ComponentName componentName) {
             mBound = false;
         }
     };
}
