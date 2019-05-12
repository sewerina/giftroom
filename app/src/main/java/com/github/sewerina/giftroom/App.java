package com.github.sewerina.giftroom;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.github.sewerina.giftroom.db.AppDatabase;
import com.github.sewerina.giftroom.model.PersistenceService;
import com.github.sewerina.giftroom.model.Service;

public class App extends Application {
    private static ViewModelProvider.Factory sViewModelFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        AppDatabase db = Room.databaseBuilder(this,
                AppDatabase.class, "room.db")
                .allowMainThreadQueries()
                .build();

        Service service = new PersistenceService(db.roomDao());
        sViewModelFactory = new AppViewModelFactory(service);
    }

    public static ViewModelProvider.Factory getViewModelFactory(){
        if (sViewModelFactory == null){
            throw new IllegalStateException("Application is not initialized");
        }
        return sViewModelFactory;
    }
}
