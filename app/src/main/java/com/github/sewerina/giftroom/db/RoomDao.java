package com.github.sewerina.giftroom.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoomDao {
    @Query("select * from room")
    List<RoomEntity> getAll();

    @Insert
    void insert(RoomEntity roomEntity);
}
