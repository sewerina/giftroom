package com.github.sewerina.giftroom.model;

import com.github.sewerina.giftroom.db.RoomDao;
import com.github.sewerina.giftroom.db.RoomEntity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersistenceService implements Service {

    private final RoomDao mRoomDao;
    private FirebaseFirestore mFirestore;

    public PersistenceService(RoomDao roomDao) {
        mRoomDao = roomDao;
        init();
    }

    private void init() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            mAuth.signInAnonymously();
        }

        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public Room addRoom(String name) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.name = name;
        roomEntity.id = UUID.randomUUID().toString();

        mRoomDao.insert(roomEntity);

        mFirestore.collection("rooms")
                .add(roomEntity);

        return new PersistenceRoom(roomEntity);
    }

    public void joinRoom(String roomId, final Room.JoinRoomCallback callback) {
        mFirestore.collection("rooms")
                .whereEqualTo("id", roomId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            RoomEntity roomEntity = document.toObject(RoomEntity.class);
                            mRoomDao.insert(roomEntity);
                        }
                        callback.call();
                    }
                });
    }

    @Override
    public void removeRoom(Room room) {
        RoomEntity roomEntity = mRoomDao.getById(room.id());
        mRoomDao.delete(roomEntity);
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
        RoomEntity roomEntity = mRoomDao.getById(id);
        PersistenceRoom room = new PersistenceRoom(roomEntity);
        return room;
    }

    public static class GiftEntity {
        public String id;
        public String name;
        public String roomId;
    }

    public class PersistenceRoom implements Room {

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
        public void gifts(final GiftsLoadedCallback callback) {
            mFirestore.collection("gifts")
                    .whereEqualTo("roomId", id())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Gift> gifts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                GiftEntity giftEntity = document.toObject(GiftEntity.class);

                                gifts.add(new PersistenceGift(giftEntity, document.getId()));
                            }
                            callback.call(gifts);
                        }
                    });
        }

        @Override
        public void addGift(String name, final GiftAddedCallback callback) {
            final GiftEntity giftEntity = new GiftEntity();
            giftEntity.name = name;
            giftEntity.id = UUID.randomUUID().toString();
            giftEntity.roomId = id();

            mFirestore.collection("gifts")
                    .add(giftEntity)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            final PersistenceGift gift = new PersistenceGift(giftEntity, documentReference.getId());
                            callback.call(gift);
                        }
                    });
        }

    }

    public class PersistenceGift implements Gift {
        private final GiftEntity mGiftEntity;
        private final String mFirestoreId;

        public PersistenceGift(GiftEntity giftEntity, String firestoreId) {
            mGiftEntity = giftEntity;
            mFirestoreId = firestoreId;
        }

        @Override
        public String name() {
            return mGiftEntity.name;
        }

        @Override
        public String id() {
            return mGiftEntity.id;
        }

        @Override
        public void delete() {
            mFirestore.collection("gifts")
                    .document(mFirestoreId)
                    .delete();
        }

    }
}
