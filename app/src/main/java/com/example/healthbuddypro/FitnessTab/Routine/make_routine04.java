package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;

public class make_routine04 extends AppCompatActivity {

    private Button completeMakeRoutine;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_routine04);

        backButton = findViewById(R.id.backButton1);
        completeMakeRoutine = findViewById(R.id.completeMakeRoutine);

        //뒤로가기
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // 시작하기 버튼 -> 구조가 엑티비티를 넘기고 moved_routineFragment 여기서 framelayout에 프래그먼트가 덧씌워지는 방식
        completeMakeRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(make_routine04.this, moved_routineFragment.class);  // CurrentActivity를 현재 액티비티 이름으로 변경
                startActivity(intent);
                finish();
            }
        });


    }
}