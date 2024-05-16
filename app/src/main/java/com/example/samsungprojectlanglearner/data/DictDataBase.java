package com.example.samsungprojectlanglearner.data;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.samsungprojectlanglearner.data.Dict.Dict;

@Database(entities = {Dict.class}, version = 4)
public abstract class DictDataBase extends RoomDatabase {
    private static final String DB_NAME = "database_dict.db";
    private static DictDataBase instance = null;
    public static DictDataBase newInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(application
                    ,DictDataBase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public abstract DictDao DictDao();
}
