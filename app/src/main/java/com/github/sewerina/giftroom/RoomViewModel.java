package com.github.sewerina.giftroom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoomViewModel extends ViewModel {
    private final Service mService;
    private final MutableLiveData<Iterable<Gift>> mGifts = new MutableLiveData<>();

    public RoomViewModel(Service service) {
        mService = service;
    }

    public LiveData<Iterable<Gift>> gifts() {
        return mGifts;
    }

    void load(String id) {
        Room room = mService.roomById(id);
        if (room != null) {
            mGifts.postValue(room.gifts());
        }
    }
}
