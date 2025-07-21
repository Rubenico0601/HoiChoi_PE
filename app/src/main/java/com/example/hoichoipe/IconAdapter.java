package com.example.hoichoipe;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;

import java.util.HashMap;
import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    private final Context context;
    private final List<String> titles;
    private final List<String> icons;
    private final String color;
    private final CleverTapAPI clevertapDefaultInstance;

    public IconAdapter(Context context, List<String> titles, List<String> icons, String color) {
        this.context = context;
        this.titles = titles;
        this.icons = icons;
        this.color = color;

        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String currentTitle = titles.get(position);

        Glide.with(context)
                .load(icons.get(position))
                .into(holder.icon);

        holder.title.setText(currentTitle);

        try {
            holder.title.setTextColor(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            holder.title.setTextColor(Color.BLACK);
        }

        // 🔹 Set click listener ONLY on the icon
        holder.icon.setOnClickListener(v -> {
            Toast.makeText(context, currentTitle + " has been clicked", Toast.LENGTH_SHORT).show();
            if (clevertapDefaultInstance != null) {
                HashMap<String, Object> profileUpdate = new HashMap<>();
                profileUpdate.put("Last Clicked Service", currentTitle);
                clevertapDefaultInstance.pushProfile(profileUpdate);
            }

        });
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_image);
            title = itemView.findViewById(R.id.icon_title);
        }
    }
}

