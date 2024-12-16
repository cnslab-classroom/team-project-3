package com.example.dailyhelper.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dailyhelper.dao.PresetDAO;
import com.example.dailyhelper.dto.Preset;

@Database(entities = {Preset.class}, version = 1)
public abstract class PresetDatabase extends RoomDatabase {
    private static PresetDatabase INSTANCE = null;

    public abstract PresetDAO presetDAO();

    public static PresetDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PresetDatabase.class, "preset.db").build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
