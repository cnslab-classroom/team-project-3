package com.example.dailyhelper.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dailyhelper.dto.CategoryCount;
import com.example.dailyhelper.dto.Todo;

import java.util.List;
import java.util.Map;

@Dao
public interface TodoDAO {
    @Query("SELECT * FROM todo")
    List<Todo> getAll();

    @Query("SELECT * FROM todo WHERE date LIKE :dateQuery ")
    List<Todo> findDatesTodo(String dateQuery);

    @Query("SELECT category, COUNT(*) AS count FROM todo GROUP BY category ORDER BY count DESC LIMIT 1")
    CategoryCount getMostFrequentCategory();

    @Insert()
    void insertTodo(Todo todo);

    @Delete
    void delete(Todo todo);
}
