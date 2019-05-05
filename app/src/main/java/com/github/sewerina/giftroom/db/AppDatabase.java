package com.github.sewerina.giftroom.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoomDao roomDao();
}
