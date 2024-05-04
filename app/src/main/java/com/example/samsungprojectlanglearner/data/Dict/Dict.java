package com.example.samsungprojectlanglearner.data.Dict;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "Dicts")
public class Dict {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "components")
    private String Comps;
    @ColumnInfo(name = "name")
    @NotNull
    private String name;
    public String getComps() {
        return Comps;
    }
    public void setComps(String comps) {
        Comps = comps;
    }
    @Ignore
    public Dict(int id,
                String Comps, String name) {
        this.id = id;
        this.Comps = Comps;
        this.name = name;
    }
    public Dict(
            String Comps, String name) {
        this.Comps = Comps;
        this.id = 0;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dict dict = (Dict) o;
        return id == dict.id && Objects.equals(Comps, dict.Comps) && Objects.equals(name, dict.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Comps, name);
    }
}
