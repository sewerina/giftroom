package com.github.sewerina.giftroom;

public interface Service {
    Room addRoom(String name);
    void removeRoom(Room room);
    Iterable<Room> rooms();
    Room roomById(String id);
}
