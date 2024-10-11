package com.example.healthbuddypro.ShortTermMatching;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {

    private List<MatchPost> matchList;

    public MatchListAdapter(List<MatchPost> matchList) {
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchPost currentPost = matchList.get(position);

        // 제목 설정
        holder.textViewMatchInfo.setText(currentPost.getTitle());
        // 운동 부위 설정
        holder.textViewHealth.setText(currentPost.getHealth());
        // 지역 설정
        holder.textViewLocation.setText(currentPost.getLocation());
        // 성별 카테고리 설정
        holder.textViewCategory.setText(currentPost.getCategory());
        // 글 내용 설정
        holder.textViewContent.setText(currentPost.getContent());

        holder.btnApply.setOnClickListener(v -> {
            showApplyDialog(v.getContext(), currentPost);
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    private void showApplyDialog(Context context, MatchPost post) {
        new AlertDialog.Builder(context)
                .setTitle("매칭 신청")
                .setMessage(post.getTitle() + "에 매칭 신청하시겠습니까?")
                .setPositiveButton("네", (dialog, which) -> {
                    sendMatchRequest(context, post.getSenderId(), post.getReceiverId());
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    private void sendMatchRequest(Context context, int senderId, int receiverId) {
        ApiService apiService = RetrofitClient.getApiService("http://165.229.89.154:8080/");
        MatchRequest request = new MatchRequest(senderId, receiverId);

        apiService.sendMatchRequest(request).enqueue(new Callback<MatchResponse>() {
            @Override
            public void onResponse(Call<MatchResponse> call, Response<MatchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "매칭 신청 완료!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "매칭 신청 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MatchResponse> call, Throwable t) {
                Toast.makeText(context, "오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateList(List<MatchPost> newMatchList) {
        matchList = newMatchList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMatchInfo;
        TextView textViewHealth;
        TextView textViewLocation;
        TextView textViewCategory;
        TextView textViewContent; // content를 표시할 TextView
        Button btnApply;

        ViewHolder(View itemView) {
            super(itemView);
            textViewMatchInfo = itemView.findViewById(R.id.textViewMatchInfo);
            textViewHealth = itemView.findViewById(R.id.textViewHealth);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewContent = itemView.findViewById(R.id.textViewContent);  // content를 위한 TextView
            btnApply = itemView.findViewById(R.id.btn_apply);
        }
    }
}
