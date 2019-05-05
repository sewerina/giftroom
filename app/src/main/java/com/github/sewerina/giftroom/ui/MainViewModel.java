package com.github.sewerina.giftroom.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.sewerina.giftroom.model.Room;
import com.github.sewerina.giftroom.model.Service;

public class MainViewModel extends ViewModel {
    private final Service mService;
    private final MutableLiveData<Iterable<Room>> mRooms = new MutableLiveData<>();

    public MainViewModel(Service service) {
        mService = service;
    }

    public LiveData<Iterable<Room>> rooms() {
        return mRooms;
    }

    void load() {
        mRooms.postValue(mService.rooms());
    }

    public void createRoom(String name) {
        mService.addRoom(name);
        load();
    }
}
