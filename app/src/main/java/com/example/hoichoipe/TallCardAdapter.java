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

public class TallCardAdapter extends RecyclerView.Adapter<TallCardAdapter.TallCardViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public TallCardAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public TallCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tall_card, parent, false);
        return new TallCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TallCardViewHolder holder, int position) {
        Glide.with(context).load(imageUrls.get(position)).into(holder.imageView);

        // Adjust width dynamically so multiple cards fit
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.35); // Adjust as needed
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class TallCardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public TallCardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tall_card_image);
        }
    }
}
