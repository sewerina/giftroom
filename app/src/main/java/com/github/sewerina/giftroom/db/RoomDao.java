package com.github.sewerina.giftroom.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoomDao {
    @Query("select * from room")
    List<RoomEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoomEntity roomEntity);

    @Query("select * from room where id= :id LIMIT 1")
    RoomEntity getById(String id);

    @Delete
    void delete(RoomEntity roomEntity);
}
