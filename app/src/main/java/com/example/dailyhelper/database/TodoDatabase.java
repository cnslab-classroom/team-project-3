package com.example.dailyhelper.database;

import static com.example.dailyhelper.database.migration.TodoMigration.MIGRATION_1_2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.dailyhelper.dao.TodoDAO;
import com.example.dailyhelper.dto.Todo;

@Database(entities = {Todo.class}, version = 2)
public abstract class TodoDatabase extends RoomDatabase {
    private static TodoDatabase INSTANCE = null;

    public abstract TodoDAO todoDAO();

    public static TodoDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TodoDatabase.class, "todo.db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
