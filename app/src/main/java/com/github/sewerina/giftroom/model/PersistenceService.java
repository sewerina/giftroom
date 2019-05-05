package com.github.sewerina.giftroom.model;

import com.github.sewerina.giftroom.db.RoomDao;
import com.github.sewerina.giftroom.db.RoomEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersistenceService implements Service {

    private final RoomDao mRoomDao;

    public PersistenceService(RoomDao roomDao) {
        mRoomDao = roomDao;
    }

    @Override
    public Room addRoom(String name) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.name = name;
        roomEntity.id = UUID.randomUUID().toString();

        mRoomDao.insert(roomEntity);
        return new PersistenceRoom(roomEntity);
    }

    @Override
    public void removeRoom(Room room) {

    }

    @Override
    public Iterable<Room> rooms() {
        List<Room> rooms = new ArrayList<>();
        for (RoomEntity roomEntity : mRoomDao.getAll()) {
            Room room = new PersistenceRoom(roomEntity);
            rooms.add(room);
        }
        return rooms;
    }

    @Override
    public Room roomById(String id) {
        return null;
    }

    public static class PersistenceRoom implements Room {

        private final RoomEntity mRoomEntity;

        public PersistenceRoom(RoomEntity roomEntity) {
            mRoomEntity = roomEntity;
        }

        @Override
        public String name() {
            return mRoomEntity.name;
        }

        @Override
        public String id() {
            return mRoomEntity.id;
        }

        @Override
        public Iterable<Gift> gifts() {
            return null;
        }

        @Override
        public Gift addGift(String name) {
            return null;
        }
    }
}
