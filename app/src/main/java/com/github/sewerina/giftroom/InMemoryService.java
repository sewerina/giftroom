package com.github.sewerina.giftroom;

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

    @Override
    public void createRoom(String name) {
        Room r = new InMemoryRoom(name);
        mRooms.add(r);
    }

    static class InMemoryRoom implements Room {
        private String mName;

        public InMemoryRoom(String name) {
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

        @Override
        public Iterable<Gift> gifts() {
            List<Gift> gifts = new ArrayList<>();
            gifts.add(new InMemoryGift("Parfume"));
            gifts.add(new InMemoryGift("Toy"));
            gifts.add(new InMemoryGift("Flowers"));
            gifts.add(new InMemoryGift("Tea cup"));
            gifts.add(new InMemoryGift("Cake"));
            gifts.add(new InMemoryGift("Travel tickets"));
            return gifts;
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
