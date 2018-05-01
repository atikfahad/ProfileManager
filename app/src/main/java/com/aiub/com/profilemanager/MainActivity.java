package com.aiub.com.profilemanager;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LocalService mService;
    boolean mBound = false;
    boolean isFirst = true;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
     @Override
    protected void onStart(){
        super.onStart();
         NotificationManager notificationManager =
                 (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                 && !notificationManager.isNotificationPolicyAccessGranted()) {

             Intent intent = new Intent(
                     android.provider.Settings
                             .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

             startActivity(intent);
         }
         intent = new Intent(this, LocalService.class);
         if(isFirst){
            startService(intent);
            isFirst = false;
         }
         bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
         Toast.makeText(this, "Service Bounded", Toast.LENGTH_SHORT).show();
     }
     @Override
    protected void onStop(){
        super.onStop();
         if(mBound) {
             unbindService(mConnection);
             mBound = false;
             Toast.makeText(this, "Service Unbounded", Toast.LENGTH_SHORT).show();
         }
     }
     public void startBoundService(View v){

     }
     public void stopBoundService(View v){
         if(intent != null){
            stopService(intent);
            return;
         }
         Toast.makeText(this, "Your Service is not started yet.", Toast.LENGTH_SHORT).show();

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
