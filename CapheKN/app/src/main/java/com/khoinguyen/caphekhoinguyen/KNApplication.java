package com.khoinguyen.caphekhoinguyen;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

public class KNApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
