package com.github.sewerina.giftroom;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class AppViewModelFactory implements ViewModelProvider.Factory {
    private Service mService;
    public AppViewModelFactory(Service service) {
        mService = service;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(MainViewModel.class)) {
            return (T) new MainViewModel(mService);
        }

        if (modelClass.equals(RoomViewModel.class)) {
            return (T) new RoomViewModel(mService);
        }

        throw new IllegalArgumentException();
    }
}
