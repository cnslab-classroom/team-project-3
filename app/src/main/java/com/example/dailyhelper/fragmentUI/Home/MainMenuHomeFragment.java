package com.example.dailyhelper.fragmentUI.Home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dailyhelper.MainActivity;
import com.example.dailyhelper.R;
import com.example.dailyhelper.dao.TodoDAO;
import com.example.dailyhelper.database.TodoDatabase;
import com.example.dailyhelper.dto.Todo;
import com.example.dailyhelper.fragmentUI.Home.todo.SelectDate;
import com.example.dailyhelper.utils.DateFormat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import org.threeten.bp.DayOfWeek;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainMenuHomeFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private LinearLayout todoWarp;
    private FloatingActionButton fab;

    TodoDatabase todoDB;
    private ArrayList<CalendarDay> HealthDayList = new ArrayList<>();
    private ArrayList<CalendarDay> FriendDayList = new ArrayList<>();
    private ArrayList<CalendarDay> WorkDayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView daily_theme = (TextView) view.findViewById(R.id.daily_theme);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarview);
        todoWarp = (LinearLayout) view.findViewById(R.id.todoWarp);
        fab = (FloatingActionButton) view.findViewById(R.id.floating_action_button);

        todoDB = TodoDatabase.getInstance(getContext());

        String year = DateFormat.make_yyyy();
        String month = DateFormat.make_MM();
        String day = DateFormat.make_dd();

        int start, end;
        String daily_theme_text = "오늘은 "+ month + "월 " + day + "일 입니다! \n오늘의 일정 테마는 \"건강\" 입니다.";
        SpannableString text = new SpannableString(daily_theme_text);

        //month bold체 전환
        start = daily_theme_text.indexOf(month);
        end = start + month.length();
        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //day bold체 전환
        start = daily_theme_text.indexOf(day);
        end = start + day.length();
        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        daily_theme.setText(text);

        calendarView.state()
                .edit()
                .setFirstDayOfWeek(DayOfWeek.of(Calendar.SUNDAY))
                .commit();

        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        /*
        * Only the original thread that created a view hierarchy can touch its views.
        */
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    TodoDAO todoDao = todoDB.todoDAO();
                    List<Todo> todos = todoDao.findDatesTodo(
                            date.getYear() + "-" + date.getMonth() + "-" + date.getDay()
                    );

                    // 메인 스레드에서 UI 업데이트
                    requireActivity().runOnUiThread(() -> updateTodoList(todos, requireActivity()));
                    Log.d("DB", "run: Successfully updated Todo list");
                });
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectDate.class);
                startActivity(intent); // 날짜 선택 화면으로 이동
                requireActivity().finish();
            }
        });

        Thread t = readTodoThread(year, month, day, requireActivity());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    @NonNull
    public Thread readTodoThread(String year, String month, String day, Activity context) {
        class ReadRunnable implements Runnable {
            @Override
            public void run() {
                TodoDAO todoDao = todoDB.todoDAO();
                List<Todo> todo = todoDao.findDatesTodo(year + "-%"); //yyyy-MM-dd
                Log.d("DB", "run: Successfully read from DB");

                try {
                    monthRangeTodoList(todo);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                calendarView.setSelectedDate(CalendarDay.today());

                calendarView.addDecorators(new HomeCalenderDecorator(Color.rgb(242, 95, 57), HealthDayList, context));
                calendarView.addDecorators(new HomeCalenderDecorator(Color.rgb(138, 78, 255), FriendDayList, context));
                calendarView.addDecorators(new HomeCalenderDecorator(Color.rgb(1, 170, 251), WorkDayList, context));

                Log.d("DB", "run: Completed to decorate");
                todo = todoDao.findDatesTodo(
                        year + "-" + month + "-" + day
                );
                updateTodoList(todo, context);
            }
        }

        ReadRunnable readRunnable = new ReadRunnable();
        Thread t = new Thread(readRunnable);
        return t;
    }

    private void monthRangeTodoList(List<Todo> todo) throws ParseException {
        ArrayList<Integer> formatDate = new ArrayList<>();

        for(Todo data: todo) {
            switch (data.category) {
                case "건강":
                    formatDate = formatting(data);
                    //year, month, day 순 넣기
                    HealthDayList.add(CalendarDay.from(formatDate.get(0), formatDate.get(1), formatDate.get(2)));
                    break;
                case "친목":
                    formatDate = formatting(data);
                    //year, month, day 순 넣기
                    FriendDayList.add(CalendarDay.from(formatDate.get(0), formatDate.get(1), formatDate.get(2)));
                    break;
                case "일":
                    formatDate = formatting(data);
                    //year, month, day 순 넣기
                    WorkDayList.add(CalendarDay.from(formatDate.get(0), formatDate.get(1), formatDate.get(2)));
                    break;
            }
        }
    }

    private static ArrayList<Integer> formatting(Todo data) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date date = formatter.parse(data.date);
        ArrayList<Integer> result = new ArrayList<>();

        int year = Integer.parseInt(DateFormat.make_yyyy(date));
        int month = Integer.parseInt(DateFormat.make_MM(date));
        int day = Integer.parseInt(DateFormat.make_dd(date));

        result.add(year);
        result.add(month);
        result.add(day);

        return result;
    }

    private void updateTodoList(List<Todo> todo, Activity context) {
        SpannableString text;

        todoWarp.removeAllViews();

        if(todo.isEmpty()) { //todo가 비어 있다면 비어았다고 표시하기
            TextView empty = new TextView(context);
            empty.setGravity(View.TEXT_ALIGNMENT_CENTER);
            empty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            empty.setText("Empty Todo now. . .");
            empty.setTextSize(18);
            todoWarp.addView(empty);
            return;
        }

        for (Todo data: todo) {
            LinearLayout textWarp = new LinearLayout(context);
            TextView title = new TextView(context);
            TextView content = new TextView(context);

            switch (data.category) {
                case "건강":
                    textWarp.setBackgroundResource(R.drawable.health_todo_box);
                    break;
                case "친목":
                    textWarp.setBackgroundResource(R.drawable.friend_todo_box);
                    break;
                case "일":
                    textWarp.setBackgroundResource(R.drawable.work_todo_box);
                    break;
            }

            text = new SpannableString(data.name);
            text.setSpan(new StyleSpan(Typeface.BOLD), 0, data.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(text);
            title.setTextSize(18);

            text = new SpannableString(data.content);
            text.setSpan(new StyleSpan(Typeface.BOLD), 0, data.content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            content.setText(text);

            textWarp.setOrientation(LinearLayout.VERTICAL);
            textWarp.addView(title);
            textWarp.addView(content);
            todoWarp.addView(textWarp);
        }
    }
}