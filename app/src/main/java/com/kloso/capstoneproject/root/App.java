package com.kloso.capstoneproject.root;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        FirebaseApp.initializeApp(this);
    }

    public ApplicationComponent getComponent(){
        return component;
    }

}
