package com.example.hoichoipe;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class TestingActivity extends AppCompatActivity {

    RecyclerView recyclerServices, recyclerProducts;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int currentPage = 0;
    private final int DELAY_MS = 3000;

    private final List<String> banners = Arrays.asList(
            "https://i.ibb.co/gbHKQM52/banner1.png",
            "https://i.ibb.co/NgyBZZqp/banner2.png",
            "https://i.ibb.co/d45pGRMR/banner3.png",
            "https://i.ibb.co/Pz1jZT5c/banner4.png",
            "https://i.ibb.co/4nDmxtMF/banner5.png"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        recyclerServices = findViewById(R.id.recycler_services);
        recyclerProducts = findViewById(R.id.recycler_products);

        viewPager = findViewById(R.id.bannerViewPager);
        tabLayout = findViewById(R.id.tabDots);

        BannersAdapter adapter = new BannersAdapter(this, banners);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setCustomView(R.layout.custom_dot);
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null && tab.getCustomView() != null) {
                        ImageView dot = (ImageView) tab.getCustomView();
                        dot.setBackgroundResource(i == position ? R.drawable.dot_selected : R.drawable.dot_unselected);
                    }
                }
            }
        });

        setupRecycler(recyclerServices, getServiceTitles(), getServiceIcons(), "#B22600");
        setupRecycler(recyclerProducts, getProductTitles(), getProductIcons(), "#B22600");


    }

    private void startAutoSlide() {
        runnable = new Runnable() {
            @Override
            public void run() {
                int nextPage = (viewPager.getCurrentItem() + 1) % banners.size();
                viewPager.setCurrentItem(nextPage, true);
                handler.postDelayed(this, DELAY_MS);
            }
        };
        handler.postDelayed(runnable, DELAY_MS);
    }



    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable); // <-- Add this to avoid overlap
        startAutoSlide();
    }

    private void setupRecycler(RecyclerView recyclerView, List<String> titles, List<String> icons, String color) {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new IconAdapter(this, titles, icons, "#B22600"));
    }

    private List<String> getServiceTitles() {
        return Arrays.asList("Premium Payment", "Statement", "Claim", "Top Up", "Fund Value", "Policy Update", "Nominee", "More");
    }

    private List<String> getServiceIcons() {
        return Arrays.asList("https://i.ibb.co/ycB3k3S8/ic-terms.png",
                "https://i.ibb.co/BVbvyGnx/ic-crorepati.png",
                "https://i.ibb.co/VpJg8jQy/pricing.png",
                "https://i.ibb.co/6cw3Pq5S/ic-child.png",
                "https://i.ibb.co/v6vCX4m6/ic-retire.png",
                "https://i.ibb.co/v4Z70zjd/ic-savings.png",
                "https://i.ibb.co/2317Xb0g/ic-car.png",
                "https://i.ibb.co/5x293S8W/ic-annuity.png"
        );
    }

    private List<String> getProductTitles() {
        return Arrays.asList("Term Insurance", "Crorepati", "Unit Linked", "Education", "Retirement", "Savings", "Car", "Annuity");
    }

    private List<String> getProductIcons() {
        return Arrays.asList("https://i.ibb.co/LzB2x166/ic-premium.png",
                "https://i.ibb.co/bRW12ND5/ic-statement.png",
                "https://i.ibb.co/RpNm9Gg8/ic-claim.png",
                "https://i.ibb.co/zCRBGzB/ic-topup.png",
                "https://i.ibb.co/DDVgN4KG/ic-fund.png",
                "https://i.ibb.co/zhNpkcfp/ic-policy.png",
                "https://i.ibb.co/jPL6Yy3q/ic-nominee.png",
                "https://i.ibb.co/TB7L4KfK/ic-more.png"
        );
    }
}
