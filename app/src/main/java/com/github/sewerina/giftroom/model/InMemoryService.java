package com.github.sewerina.giftroom.model;

import java.util.ArrayList;
import java.util.List;

public class InMemoryService implements Service {
    private List<Room> mRooms = new ArrayList<>();

    public InMemoryService() {
        mRooms.add(new InMemoryRoom("Tom's birthday"));
        mRooms.add(new InMemoryRoom("Jerry's wedding"));
        mRooms.add(new InMemoryRoom("New Year"));
    }

    @Override
    public Room addRoom(String name) {
        Room room = new InMemoryRoom(name);
        mRooms.add(room);
        return room;
    }

    @Override
    public void joinRoom(String roomId, Room.JoinRoomCallback callback) {

    }

    @Override
    public void removeRoom(Room room) {
        mRooms.remove(room);
    }

    @Override
    public Iterable<Room> rooms() {
        return mRooms;
    }

    @Override
    public Room roomById(String id) {
        for (Room r : mRooms) {
            if (id.equals(r.id())) {
                return r;
            }
        }
        return null;
    }

    static class InMemoryRoom implements Room {
        private final List<Gift> mGifts = new ArrayList<>();

        private String mName;

        public InMemoryRoom(String name) {
            mName = name;

            mGifts.add(new InMemoryGift("Parfume"));
            mGifts.add(new InMemoryGift("Toy"));
            mGifts.add(new InMemoryGift("Flowers"));
            mGifts.add(new InMemoryGift("Tea cup"));
            mGifts.add(new InMemoryGift("Cake"));
            mGifts.add(new InMemoryGift("Travel tickets"));


        }

        @Override
        public String name() {
            return mName;
        }

        @Override
        public String id() {
            return mName;
        }

        @Override
        public void gifts(GiftsLoadedCallback callback) {
            callback.call(mGifts);
        }

        @Override
        public Gift addGift(String name) {
            Gift g = new InMemoryGift(name);
            mGifts.add(g);
            return g;
        }
    }

    static class InMemoryGift implements Gift {
        private String mName;

        public InMemoryGift(String name) {
            mName = name;
        }

        @Override
        public String name() {
            return mName;
        }

        @Override
        public String id() {
            return mName;
        }
    }
}
