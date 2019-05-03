package com.github.sewerina.giftroom;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

public class App extends Application {
    private static ViewModelProvider.Factory sViewModelFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        Service service = new InMemoryService();
        sViewModelFactory = new AppViewModelFactory(service);
    }

    public static ViewModelProvider.Factory getViewModelFactory(){
        if (sViewModelFactory == null){
            throw new IllegalStateException("Application is not initialized");
        }
        return sViewModelFactory;
    }
}
