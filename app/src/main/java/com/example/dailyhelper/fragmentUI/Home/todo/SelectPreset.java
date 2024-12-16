package com.example.dailyhelper.fragmentUI.Home.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.dailyhelper.MainActivity;
import com.example.dailyhelper.R;
import com.example.dailyhelper.dao.PresetDAO;
import com.example.dailyhelper.database.PresetDatabase;
import com.example.dailyhelper.database.TodoDatabase;
import com.example.dailyhelper.dto.Preset;
import com.example.dailyhelper.utils.CustomApplication;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.Executors;

public class SelectPreset extends AppCompatActivity {

    private ScrollView presetWarp;
    private ImageButton backBtn;
    private Button nextBtn;
    private RadioGroup rdoGroup;
    private ConstraintLayout presetSelectView;
    PresetDatabase presetDB;

    TodoDatabase todoDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_preset);

        presetDB = PresetDatabase.getInstance(this);
        CustomApplication app = (CustomApplication) this.getApplication();

        Chip healthChip = (Chip) findViewById(R.id.health_chip);
        Chip friendChip = (Chip) findViewById(R.id.friend_chip);
        Chip workChip = (Chip) findViewById(R.id.work_chip);
        Chip etcChip = (Chip) findViewById(R.id.etc_chip);
        presetWarp = (ScrollView) findViewById(R.id.presetView);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        nextBtn = (Button) findViewById(R.id.next_btn);
        presetSelectView = (ConstraintLayout) findViewById(R.id.preset_select_view);
        rdoGroup = (RadioGroup) findViewById(R.id.rdo_group);

        String getDate = getIntent().getStringExtra("date");

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
                    runOnUiThread(() -> updateTodoList(presetList, SelectPreset.this));
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
                    runOnUiThread(() -> updateTodoList(presetList, SelectPreset.this));
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
                    runOnUiThread(() -> updateTodoList(presetList, SelectPreset.this));
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
                    runOnUiThread(() -> updateTodoList(presetList, SelectPreset.this));
                    Log.d("DB", "run: Successfully updated Preset list in Etc Preset");
                });
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPreset.this, MainActivity.class);
                startActivity(intent); // 메인 화면으로 이동
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdoGroup.getCheckedRadioButtonId() == -1) {
                    Snackbar.make(presetSelectView, R.string.snackbar_preset_text_label, Snackbar.LENGTH_LONG).show();
                    return;
                }

                RadioButton rdoBtn = (RadioButton) findViewById(rdoGroup.getCheckedRadioButtonId());
                String text = rdoBtn.getText().toString();
                String[] lines = text.split("\n");
                String category = "";

                Intent intent = new Intent(SelectPreset.this, SelectDuration.class);
                intent.putExtra("date", getDate);
                intent.putExtra("title", lines[0]);
                intent.putExtra("content", lines[1]);

                if (healthChip.isChecked()) category = "건강";
                else if (friendChip.isChecked()) category = "친목";
                else if (workChip.isChecked()) category = "일";
                else if (etcChip.isChecked()) category = "기타";

                intent.putExtra("category", category);
                Log.d("Intent", "Extra: (todo) title: " + lines[0] + " content: " + lines[1]);

                startActivity(intent); // 프리셋 고르는 화면으로 이동
                Log.d("Intent", "Starting SelectStartTime Class");
                finish();
            }
        });

        // initialize
        healthChip.setChecked(true);
        app.getSingleThreadExecutor().execute(() -> {
            PresetDAO presetDAO = presetDB.presetDAO();
            List<Preset> presetList = presetDAO.findTodoFromCategory("건강");
            Log.d("DB", "run: Read from DB: " + presetList.toString());

            // 메인 스레드에서 UI 업데이트
            runOnUiThread(() -> updateTodoList(presetList, SelectPreset.this)); // 왜 안 뜨냐
            Log.d("DB", "run: Successfully updated Preset list in Health Preset");
        });
    }

    private void updateTodoList(List<Preset> presetList, Context context) {
        SpannableString span;

        if (rdoGroup.getChildCount() > 0) { //rdoGroup이 비어있는지 검사
            rdoGroup.removeAllViews();
        }

        if (presetList.isEmpty()) { //todo가 비어 있다면 비어았다고 표시하기
            RadioButton empty = new RadioButton(context);
            empty.setGravity(View.TEXT_ALIGNMENT_CENTER);
            empty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            empty.setText("Empty Preset List now. . .");
            empty.setTextSize(18);
            empty.setTextColor(ContextCompat.getColor(context, R.color.black));
            empty.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            rdoGroup.addView(empty);
            return;
        }

        for (Preset data : presetList) {
            RadioButton textWarp = new RadioButton(context);

            // LayoutParams 설정: MATCH_PARENT를 사용하여 부모 너비에 맞춤
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,  // 너비를 부모에 맞춤
                    RadioGroup.LayoutParams.WRAP_CONTENT   // 높이는 내용에 맞춤
            );
            params.setMargins(8, 8, 8, 8); // 여백 설정
            textWarp.setLayoutParams(params);

            textWarp.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            textWarp.setSingleLine(false);
            textWarp.setMaxLines(3);
            textWarp.setEllipsize(TextUtils.TruncateAt.END);


            switch (data.category) {
                case "건강":
                    textWarp.setBackgroundResource(R.drawable.selector_radio_button_health);
                    break;
                case "친목":
                    textWarp.setBackgroundResource(R.drawable.selector_radio_button_friend);
                    break;
                case "일":
                    textWarp.setBackgroundResource(R.drawable.selector_radio_button_work);
                    break;
            }

            textWarp.setButtonDrawable(null);
            textWarp.setTextSize(15);

            Log.d("Span", "updateTodoList: span content = " + data.name + "\n" + data.content);
            span = new SpannableString(data.name + "\n" + data.content);
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, data.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new RelativeSizeSpan(1.3f), 0 , data.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textWarp.setText(span);
            textWarp.setTextColor(ContextCompat.getColor(context, R.color.black));

            rdoGroup.addView(textWarp);
        }
        Log.d("UI", "updateTodoList: rdoGroup child count = " + rdoGroup.getChildCount());
    }
}
