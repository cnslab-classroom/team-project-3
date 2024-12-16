package com.example.dailyhelper.fragmentUI.preset;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.dailyhelper.MainActivity;
import com.example.dailyhelper.R;
import com.example.dailyhelper.dao.PresetDAO;
import com.example.dailyhelper.dao.TodoDAO;
import com.example.dailyhelper.database.PresetDatabase;
import com.example.dailyhelper.dto.Preset;
import com.example.dailyhelper.fragmentUI.Home.todo.SelectDuration;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.Executors;

public class MakePreset extends AppCompatActivity {

    PresetDatabase presetDB;

    private TextInputLayout titleTextField;
    private TextInputLayout contentTextField;
    private Button okBtn;
    private ImageButton backBtn;
    private ConstraintLayout makePresetView;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_preset);

        Chip healthChip = (Chip) findViewById(R.id.health_chip);
        Chip friendChip = (Chip) findViewById(R.id.friend_chip);
        Chip workChip = (Chip) findViewById(R.id.work_chip);
        Chip etcChip = (Chip) findViewById(R.id.etc_chip);
        okBtn = (Button) findViewById(R.id.next_btn);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        titleTextField = (TextInputLayout) findViewById(R.id.title_outlinedTextField);
        contentTextField = (TextInputLayout) findViewById(R.id.content_outlinedTextField);
        makePresetView = (ConstraintLayout) findViewById(R.id.make_preset_view);

        presetDB = PresetDatabase.getInstance(this);
        category = "건강";
        healthChip.setChecked(true);

        healthChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendChip.setChecked(false);
                workChip.setChecked(false);
                etcChip.setChecked(false);
                category = "건강";
            }
        });

        friendChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthChip.setChecked(false);
                workChip.setChecked(false);
                etcChip.setChecked(false);
                category = "친목";
            }
        });

        workChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendChip.setChecked(false);
                healthChip.setChecked(false);
                etcChip.setChecked(false);
                category = "일";
            }
        });

        etcChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendChip.setChecked(false);
                workChip.setChecked(false);
                healthChip.setChecked(false);
                category = "기타";
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakePreset.this, MainActivity.class);
                startActivity(intent); // 메인 화면으로 이동
                finish();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preset preset = new Preset();

                if(titleTextField.getEditText() == null){
                    Snackbar.make(makePresetView, R.string.snackbar_start_time_text_label, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(contentTextField.getEditText() == null){
                    Snackbar.make(makePresetView, R.string.snackbar_start_time_text_label, Snackbar.LENGTH_LONG).show();
                    return;
                }

                preset.name = titleTextField.getEditText().getText().toString();
                preset.content = contentTextField.getEditText().getText().toString();
                preset.category = category;
                Log.d("Data", "inspect: name = " + preset.name + " content = " + preset.content + " category = " + preset.category);

                /*
                * inspect: name = com.google.android.material.textfield.TextInputEditText{42f46ec VFED..CL. ........ 0,0-974,147 aid=1073741828} content = com.google.android.material.textfield.TextInputEditText{d5674fa VFED..CL. .F...... 0,0-974,147 aid=1073741829} category = null
                */

                Executors.newSingleThreadExecutor().execute(() -> {
                    PresetDAO presetDao = presetDB.presetDAO();
                    presetDao.insert(preset);
                    Log.d("DB", "run: Successfully created Preset");
                });

                Intent intent = new Intent(MakePreset.this, MainActivity.class);
                Toast.makeText(MakePreset.this, R.string.snackbar_saved_label, Toast.LENGTH_LONG).show();
                startActivity(intent); // 메인화면으로 이동
                Log.d("Intent", "Starting MainActivity Class");
                finish();
            }
        });
    }


}
