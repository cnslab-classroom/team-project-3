package com.example.dailyhelper.fragmentUI.Home.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.dailyhelper.MainActivity;
import com.example.dailyhelper.R;
import com.example.dailyhelper.dao.TodoDAO;
import com.example.dailyhelper.database.TodoDatabase;
import com.example.dailyhelper.dto.Todo;
import com.example.dailyhelper.utils.CustomApplication;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.Executors;

public class SelectDuration extends AppCompatActivity {

    private TimePicker startTime;
    private TimePicker endTime;
    private Button nextBtn;
    private ImageButton backBtn;
    private ConstraintLayout durationView;

    String[] start = new String[2];
    String[] end = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_duration);
        startTime = (TimePicker) findViewById(R.id.start_time);
        endTime = (TimePicker) findViewById(R.id.end_time);
        nextBtn = (Button) findViewById(R.id.next_btn);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        durationView = (ConstraintLayout) findViewById(R.id.duration_view);

        String getDate = getIntent().getStringExtra("date");
        String getTitle = getIntent().getStringExtra("title");
        String getContent = getIntent().getStringExtra("content");
        String getCategory = getIntent().getStringExtra("category");

        TodoDatabase todoDB = TodoDatabase.getInstance(this);
        CustomApplication app = (CustomApplication) this.getApplication();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDuration.this, MainActivity.class);
                startActivity(intent); // 메인 화면으로 이동
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start[0] = String.valueOf(startTime.getHour());
                start[1] = String.valueOf(startTime.getMinute());
                end[0] = String.valueOf(endTime.getHour());
                end[1] = String.valueOf(endTime.getMinute());

                Log.d("Time", "get: start = " + start[0] + " : " + start[1]);
                Log.d("Time", "get: end = " + end[0] + " : " + end[1]);

                String startTime = (Integer.parseInt(start[0]) < 10 ? "0" + start[0] : start[0])
                        + " : "
                        + (Integer.parseInt(start[1]) < 10 ? "0" + start[1] : start[1]);
                String endTime = (Integer.parseInt(start[0]) < 10 ? "0" + start[0] : start[0])
                        + " : "
                        + (Integer.parseInt(start[1]) < 10 ? "0" + start[1] : start[1]);

                Todo todo = new Todo();
                todo.date = getDate;
                todo.name = getTitle;
                todo.category = getCategory;
                todo.content = getContent;
                todo.startTime = startTime;
                todo.endTime = endTime;

                app.getSingleThreadExecutor().execute(() -> {
                    TodoDAO todoDao = todoDB.todoDAO();
                    todoDao.insertTodo(todo);
                    Log.d("DB", "run: Successfully created Todo list");
                });

                Intent intent = new Intent(SelectDuration.this, MainActivity.class);
                Toast.makeText(SelectDuration.this, R.string.snackbar_complete_label, Toast.LENGTH_LONG).show();
                startActivity(intent); // 메인화면으로 이동
                Log.d("Intent", "Starting MainActivity Class");
                finish();
            }
        });
    }
}
