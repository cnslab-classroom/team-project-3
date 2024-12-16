package com.example.dailyhelper.fragmentUI.diary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dailyhelper.R;
import com.example.dailyhelper.dao.PresetDAO;
import com.example.dailyhelper.database.PresetDatabase;
import com.example.dailyhelper.dto.Preset;
import com.example.dailyhelper.dto.Todo;
import com.example.dailyhelper.fragmentUI.Home.todo.SelectDate;
import com.example.dailyhelper.fragmentUI.Home.todo.SelectPreset;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainMenuDiaryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_diary, container, false);
    }
}