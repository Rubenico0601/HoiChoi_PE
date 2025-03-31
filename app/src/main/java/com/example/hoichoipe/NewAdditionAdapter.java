package com.example.hoichoipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewAdditionAdapter extends RecyclerView.Adapter<NewAdditionAdapter.ViewHolder> {
    private Context context;
    private List<String> imageList;

    public NewAdditionAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;  // ✅ Assign the list correctly
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_addition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageList.get(position); // ✅ Correctly accessing the list
        Glide.with(context).load(imageUrl).into(holder.imageView);

        // Adjust item width dynamically based on screen size
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.7); // Show 2-3 items
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return imageList.size();  // ✅ Ensure correct count
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.new_addition_image); // Ensure ID is correct in `new_addition_card.xml`
        }
    }
}
