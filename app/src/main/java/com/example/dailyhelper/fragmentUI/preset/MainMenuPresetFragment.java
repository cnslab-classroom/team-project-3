package com.example.dailyhelper.fragmentUI.preset;

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
import com.example.dailyhelper.utils.CustomApplication;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainMenuPresetFragment extends Fragment {
    private FloatingActionButton fab;
    private ScrollView presetWarp;

    PresetDatabase presetDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preset, container, false);

        Chip healthChip = (Chip) view.findViewById(R.id.health_chip);
        Chip friendChip = (Chip) view.findViewById(R.id.friend_chip);
        Chip workChip = (Chip) view.findViewById(R.id.work_chip);
        Chip etcChip = (Chip) view.findViewById(R.id.etc_chip);

        fab = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        presetWarp = (ScrollView) view.findViewById(R.id.presetView);

        presetDB = PresetDatabase.getInstance(getContext());
        CustomApplication app = (CustomApplication) requireActivity().getApplication();

        healthChip.setChecked(true); //initialize
        app.getSingleThreadExecutor().execute(() -> {
            PresetDAO presetDAO = presetDB.presetDAO();
            List<Preset> presetList = presetDAO.findTodoFromCategory("건강");

            // 메인 스레드에서 UI 업데이트
            requireActivity().runOnUiThread(() -> updateTodoList(presetList, requireActivity()));
            Log.d("DB", "run: Successfully updated Preset list in Health Preset");
        });

        healthChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendChip.setChecked(false);
                workChip.setChecked(false);
                etcChip.setChecked(false);

                app.getSingleThreadExecutor().execute(() -> {
                    PresetDAO presetDAO = presetDB.presetDAO();
                    List<Preset> presetList = presetDAO.findTodoFromCategory("건강");

                    // 메인 스레드에서 UI 업데이트
                    requireActivity().runOnUiThread(() -> updateTodoList(presetList, requireActivity()));
                    Log.d("DB", "run: Successfully updated Preset list in Health Preset");
                });
            }
        });

        friendChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthChip.setChecked(false);
                workChip.setChecked(false);
                etcChip.setChecked(false);

                app.getSingleThreadExecutor().execute(() -> {
                    PresetDAO presetDAO = presetDB.presetDAO();
                    List<Preset> presetList = presetDAO.findTodoFromCategory("친목");

                    // 메인 스레드에서 UI 업데이트
                    requireActivity().runOnUiThread(() -> updateTodoList(presetList, requireActivity()));
                    Log.d("DB", "run: Successfully updated Preset list in Friend Preset");
                });
            }
        });

        workChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendChip.setChecked(false);
                healthChip.setChecked(false);
                etcChip.setChecked(false);

                app.getSingleThreadExecutor().execute(() -> {
                    PresetDAO presetDAO = presetDB.presetDAO();
                    List<Preset> presetList = presetDAO.findTodoFromCategory("일");

                    // 메인 스레드에서 UI 업데이트
                    requireActivity().runOnUiThread(() -> updateTodoList(presetList, requireActivity()));
                    Log.d("DB", "run: Successfully updated Preset list of Work Preset");
                });
            }
        });

        etcChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendChip.setChecked(false);
                workChip.setChecked(false);
                healthChip.setChecked(false);

                app.getSingleThreadExecutor().execute(() -> {
                    PresetDAO presetDAO = presetDB.presetDAO();
                    List<Preset> presetList = presetDAO.findTodoFromCategory("기타");

                    // 메인 스레드에서 UI 업데이트
                    requireActivity().runOnUiThread(() -> updateTodoList(presetList, requireActivity()));
                    Log.d("DB", "run: Successfully updated Preset list in Etc Preset");
                });
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MakePreset.class);
                startActivity(intent); // 날짜 선택 화면으로 이동
                requireActivity().finish();
            }
        });

        return view;
    }

    private void updateTodoList(List<Preset> presetList, Activity context) {
        SpannableString text;

        presetWarp.removeAllViews();

        if(presetList.isEmpty()) { //todo가 비어 있다면 비어았다고 표시하기
            TextView empty = new TextView(context);
            empty.setGravity(View.TEXT_ALIGNMENT_CENTER);
            empty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            empty.setText("Empty Todo now. . .");
            empty.setTextSize(18);
            presetWarp.addView(empty);
            return;
        }

        for (Preset data: presetList) {
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
            presetWarp.addView(textWarp);
        }
    }
}