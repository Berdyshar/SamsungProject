package com.example.samsungprojectlanglearner.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.samsungprojectlanglearner.data.Dict.Dict;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DictDao {
    @Query("SELECT * FROM Dicts ORDER BY name")
    Single<List<Dict>> getDicts();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable add(Dict dict);
    @Query("DELETE FROM Dicts WHERE id = :id")
    Completable remove(int id);
    @Query("UPDATE Dicts SET name=:name, components =:Comps, result =:result WHERE id=:id")
    Completable update(int id, String Comps, String name, String result);
    @Query("SELECT * FROM Dicts WHERE id=:id")
    Dict get(int id);
}
