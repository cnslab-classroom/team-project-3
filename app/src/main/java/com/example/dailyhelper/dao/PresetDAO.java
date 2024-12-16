package com.example.dailyhelper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dailyhelper.dto.Preset;

import java.util.List;

@Dao
public interface PresetDAO {
    @Query("SELECT * FROM presets")
    List<Preset> getAll();

    @Query("SELECT * FROM presets WHERE category LIKE :dateQuery ")
    List<Preset> findTodoFromCategory(String dateQuery);

    @Insert()
    void insert(Preset preset);

    @Delete
    void delete(Preset preset);
}
