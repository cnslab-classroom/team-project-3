package com.example.dailyhelper.fragmentUI.Home.todo;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.dailyhelper.MainActivity;
import com.example.dailyhelper.R;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SelectDate extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private ConstraintLayout context_view;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);

        calendarView = (MaterialCalendarView) findViewById(R.id.calendar_view);
        context_view = (ConstraintLayout) findViewById(R.id.date_view);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        Button nextBtn = (Button) findViewById(R.id.next_btn);

        calendarView.state()
                .edit()
                .setFirstDayOfWeek(DayOfWeek.of(Calendar.SUNDAY))
                .commit();

        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDay date = calendarView.getSelectedDate();
                if (date == null) {
                    Snackbar.make(context_view, R.string.snackbar_date_text_label, Snackbar.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                LocalDate local_date = date.getDate();
                Intent intent = new Intent(SelectDate.this, SelectPreset.class);

                intent.putExtra("date", local_date.toString());
                Log.d("Intent", "Extra: (date) " + local_date.toString());

                startActivity(intent); // 프리셋 고르는 화면으로 이동
                Log.d("Intent", "Starting SelectPreset Class");
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDate.this, MainActivity.class);
                startActivity(intent); // 메인 화면으로 이동
                finish();
            }
        });
    }
}
