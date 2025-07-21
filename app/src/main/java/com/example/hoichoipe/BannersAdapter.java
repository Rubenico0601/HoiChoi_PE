package com.example.hoichoipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BannersAdapter extends RecyclerView.Adapter<BannersAdapter.BannerViewHolder> {

    private List<String> bannerList;
    private Context context;

    public BannersAdapter(Context context, List<String> bannerList) {
        this.context =context;
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banners, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Glide.with(context)
                .load(bannerList.get(position))
                .into(holder.bannerImage);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    // Inner ViewHolder class
    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.bannerImage);
        }
    }
}
