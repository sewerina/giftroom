package com.github.sewerina.giftroom.model;

public interface Service {
    Room addRoom(String name);
    void joinRoom(String roomId, final Room.JoinRoomCallback callback);
    void removeRoom(Room room);
    Iterable<Room> rooms();
    Room roomById(String id);
}
