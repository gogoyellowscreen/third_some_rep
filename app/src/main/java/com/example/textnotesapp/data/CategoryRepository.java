package com.example.textnotesapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Category.class}, version = 1)
public abstract class CategoryRepository extends RoomDatabase {
    public abstract CategoryDao categoryDao();
}
