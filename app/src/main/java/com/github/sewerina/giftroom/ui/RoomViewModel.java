package com.github.sewerina.giftroom.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.sewerina.giftroom.model.Gift;
import com.github.sewerina.giftroom.model.Room;
import com.github.sewerina.giftroom.model.Service;

public class RoomViewModel extends ViewModel {
    private final Service mService;
    private Room mRoom;
    private final MutableLiveData<Iterable<Gift>> mGifts = new MutableLiveData<>();

    public RoomViewModel(Service service) {
        mService = service;
    }

    public LiveData<Iterable<Gift>> gifts() {
        return mGifts;
    }

    void load(String id) {
        mRoom = mService.roomById(id);
        if (mRoom != null) {
            mGifts.postValue(mRoom.gifts());
        }
    }

    public void createGift(String name) {
        mRoom.addGift(name);
        mGifts.postValue(mRoom.gifts());
    }
}
