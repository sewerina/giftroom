package com.github.sewerina.giftroom.model;

public interface Room {
    String name();
    String id();
    void gifts(GiftsLoadedCallback callback);
    void addGift(String name, GiftAddedCallback callback);

    interface GiftsLoadedCallback {
        void call(Iterable<Gift> gifts);
    }

    interface JoinRoomCallback{
        void call();
    }

    interface GiftAddedCallback {
        void call(Gift gift);
    }
}
