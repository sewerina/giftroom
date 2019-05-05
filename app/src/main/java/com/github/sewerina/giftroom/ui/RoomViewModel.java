package com.github.sewerina.giftroom.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.sewerina.giftroom.model.Gift;
import com.github.sewerina.giftroom.model.Room;
import com.github.sewerina.giftroom.model.Service;

public class RoomViewModel extends ViewModel implements Room.GiftsLoadedCallback {
    private final Service mService;
    private Room mRoom;
    private final MutableLiveData<Iterable<Gift>> mGifts = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

    public RoomViewModel(Service service) {
        mService = service;
    }

    public LiveData<Iterable<Gift>> gifts() {
        return mGifts;
    }
    public LiveData <Boolean> isLoading() {
        return mIsLoading;
    }

    void load(String id) {
        mRoom = mService.roomById(id);
        if (mRoom != null) {
            mIsLoading.postValue(true);
            mRoom.gifts(this);
        }
    }

    public void createGift(String name) {
        mRoom.addGift(name);
        mIsLoading.postValue(true);
        mRoom.gifts(this);
    }

    @Override
    public void call(Iterable<Gift> gifts) {
        mGifts.postValue(gifts);
        mIsLoading.postValue(false);
    }
}
