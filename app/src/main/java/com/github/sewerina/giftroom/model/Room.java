package com.github.sewerina.giftroom.model;

public interface Room {
    String name();
    String id();
    void gifts(GiftsLoadedCallback callback);
    Gift addGift(String name);

    interface GiftsLoadedCallback {
        void call(Iterable<Gift> gifts);
    }
}
