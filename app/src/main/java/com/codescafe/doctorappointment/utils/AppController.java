package com.codescafe.doctorappointment.utils;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        SubscripFirebaseNotification();
    }
    String newToken;

    private void SubscripFirebaseNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("Message")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //    Toast.makeText(getApplicationContext(),"SuccessFull SubscribeToTopic",Toast.LENGTH_SHORT).show();
                            task.getResult();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //  Toast.makeText(getApplicationContext(),"fail SubscribeToTopic  "+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
