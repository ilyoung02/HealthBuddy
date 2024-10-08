package com.example.healthbuddypro.Matching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.Matching.Chat.ChatActivity;
import com.example.healthbuddypro.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileDetailActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView name;
    private TextView workoutInfo; // 추가 정보 텍스트뷰
    private TextView introText; // 소개글 텍스트뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.profile_name);
        workoutInfo = findViewById(R.id.profile_workout_info); // 구력과 운동 정보 표시할 부분
        introText = findViewById(R.id.profile_intro_text); // 소개글 표시할 부분

        // Intent로 전달된 데이터를 받음
        Intent intent = getIntent();
        int profileId = intent.getIntExtra("profileId", -1); // profileId를 받음
        String nickname = intent.getStringExtra("nickname"); // 기타 데이터도 받을 수 있음
        String image = intent.getStringExtra("image");

        // 받은 데이터를 UI에 표시할 수 있도록 설정
        if (profileId != -1) {
            fetchProfileDetails(profileId); // profileId를 사용하여 백엔드에서 데이터 가져오기
        } else {
            // 전달된 데이터가 없을 경우의 처리
            Toast.makeText(this, "프로필 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        // 추가적으로 받은 데이터로 UI 업데이트
        name.setText(nickname);
        Glide.with(this).load(image).into(profileImage);

        // 채팅 버튼 클릭 처리
        Button btnChat = findViewById(R.id.btn_chat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetailActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchProfileDetails(int profileId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://165.229.89.154:8080/") // 백엔드 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // 단일 프로필 데이터를 가져오도록 함
        Call<ProfileResponse> call = apiService.getProfileDetails(profileId);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 프로필 데이터 설정
                    UserProfile profile = response.body().getData();
                    updateUI(profile);
                } else {
                    String errorMessage = "프로필 정보를 불러오지 못했습니다. 오류 코드: " + response.code();
                    introText.setText(errorMessage);
                    Toast.makeText(ProfileDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("ProfileDetailActivity", errorMessage);  // Logcat에 오류 메시지 출력
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                String errorMessage = "데이터 로드 실패: " + t.getMessage();
                introText.setText(errorMessage);
                Toast.makeText(ProfileDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("ProfileDetailActivity", errorMessage);  // Logcat에 오류 메시지 출력
            }
        });
    }

    private void updateUI(UserProfile profile) {
        name.setText(profile.getNickName() + " " + profile.getAge() + "세");
        workoutInfo.setText("구력 " + profile.getWorkoutYears() + "년, 좋아요 " + profile.getLikeCount());

        // 이미지 로딩 (Glide 사용)
        Glide.with(this)
                .load(profile.getImage())
                .into(profileImage);
    }
}
