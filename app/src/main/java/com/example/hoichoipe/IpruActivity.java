package com.example.hoichoipe;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.variables.Var;
import com.clevertap.android.sdk.variables.callbacks.FetchVariablesCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class IpruActivity extends AppCompatActivity {

    private ViewPager2 topBanner;
    private Switch genderToggle;
    CleverTapAPI cleverTapAPI;

    RecyclerView recyclerServices, recyclerProducts;
    private SharedPreferences sharedPreferences;

    private ViewPager2 viewPager, viewPagerBottom;
    private TabLayout tabLayout, tabLayoutBottom;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int currentPage = 0;
    private final int DELAY_MS = 3000;

    private Var<String> loginImages, logoutImages;
    private Var<Integer> loginImagesCnt, logoutImagesCnt;
    private Var<String> servicesTheme, productsTheme, bgTheme;
    private List<String> bannerImage, getServiceIcons, getProductIcons, getServiceTitles, getProductTitles;

    private Var<Boolean> isLoggedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_here);

        cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);

        loginImages = cleverTapAPI.defineVariable("IPru.login.premium_banner_img", "{\n" +
                "  \"img1\": \"https://i.ibb.co/gbHKQM52/banner1.png\",\n" +
                "  \"img2\": \"https://i.ibb.co/gbHKQM52/banner1.png\",\n" +
                "  \"img3\": \"https://i.ibb.co/gbHKQM52/banner1.png\",\n" +
                "  \"img4\": \"https://i.ibb.co/gbHKQM52/banner1.png\",\n" +
                "  \"img5\": \"https://i.ibb.co/gbHKQM52/banner1.png\"\n" +
                "}");
        loginImagesCnt = cleverTapAPI.defineVariable("IPru.login.premium_banner_cnt", 5);

        logoutImages = cleverTapAPI.defineVariable("IPru.logout.premium_banner_img", "{\n" +
                "  \"img1\": \"https://i.ibb.co/NgyBZZqp/banner2.png\",\n" +
                "  \"img2\": \"https://i.ibb.co/NgyBZZqp/banner2.png\",\n" +
                "  \"img3\": \"https://i.ibb.co/NgyBZZqp/banner2.png\",\n" +
                "  \"img4\": \"https://i.ibb.co/NgyBZZqp/banner2.png\",\n" +
                "  \"img5\": \"https://i.ibb.co/NgyBZZqp/banner2.png\"\n" +
                "}");
        logoutImagesCnt = cleverTapAPI.defineVariable("IPru.logout.premium_banner_cnt", 5);

        bgTheme = cleverTapAPI.defineVariable("IPru.bgTheme.themeData", "{\n" +
                "  \"bg_color\": \"#F8F3F8\",\n" +
                "  \"text_color\": \"#3B3B3B\"\n" +
                "}");

        servicesTheme = cleverTapAPI.defineVariable("IPru.Services.themeData", "{\n" +
                "  \"iconImg1\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title1\": \"Premium\",\n" +
                "  \"iconImg2\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title2\": \"Statement\",\n" +
                "  \"iconImg3\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title3\": \"Claim\",\n" +
                "  \"iconImg4\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title4\": \"Top Up\",\n" +
                "  \"iconImg5\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title5\": \"Fund Value\",\n" +
                "  \"iconImg6\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title6\": \"Policy Update\",\n" +
                "  \"iconImg7\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title7\": \"Nominee\",\n" +
                "  \"iconImg8\": \"https://i.ibb.co/LzB2x166/ic-premium.png\",\n" +
                "  \"title8\": \"More\"\n" +
                "}");

        //New Additions
        productsTheme = cleverTapAPI.defineVariable("IPru.Products.themeData", "{\n" +
                "  \"iconImg1\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title1\": \"Term Insurance\",\n" +
                "  \"iconImg2\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title2\": \"Crorepati\",\n" +
                "  \"iconImg3\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title3\": \"Unit Linked\",\n" +
                "  \"iconImg4\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title4\": \"Education\",\n" +
                "  \"iconImg5\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title5\": \"Retirement\",\n" +
                "  \"iconImg6\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title6\": \"Savings\",\n" +
                "  \"iconImg7\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title7\": \"Car\",\n" +
                "  \"iconImg8\": \"https://i.ibb.co/ycB3k3S8/ic-terms.png\",\n" +
                "  \"title8\": \"Annuity\"\n" +
                "}");

        //User Type
        isLoggedIn = cleverTapAPI.defineVariable("IPru.isLoggedIn", false);

        cleverTapAPI.syncVariables();

        // Fetch variables from CleverTap
        cleverTapAPI.fetchVariables(new FetchVariablesCallback() {
            @Override
            public void onVariablesFetched(boolean isSuccess) {
                if (isSuccess) {
                    runOnUiThread(() -> {
                        handleFetchedVariables();
                    });
                }
            }
        });

        genderToggle = findViewById(R.id.gender_toggle);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("TogglePrefs", MODE_PRIVATE);

        // Retrieve and apply the saved toggle state (default is false -> Male)
        boolean isPremiumSelected = sharedPreferences.getBoolean("subState", false);
        genderToggle.setChecked(isPremiumSelected);
        updateSubView(isPremiumSelected);



        // Listen for toggle switch changes
        genderToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update UI based on the toggle state
                updateSubView(isChecked);

                // Save the state in SharedPreferences
                saveToggleState(isChecked);
            }
        });

        recyclerServices = findViewById(R.id.recycler_services);
        recyclerProducts = findViewById(R.id.recycler_products);

        viewPager = findViewById(R.id.bannerViewPager);
        tabLayout = findViewById(R.id.tabDots);

        viewPagerBottom = findViewById(R.id.bottom_bannerViewPager);
        tabLayoutBottom = findViewById(R.id.bottom_tabDots);




    }

    private void handleFetchedVariables() {
        try {
            Var<String> serviceTheme = cleverTapAPI.getVariable("IPru.Services.themeData");
            Var<String> productTheme = cleverTapAPI.getVariable("IPru.Products.themeData");
            Var<String> bgTheme = cleverTapAPI.getVariable("IPru.bgTheme.themeData");
            Var<Boolean> isLoggedIn = cleverTapAPI.getVariable("IPru.isLoggedIn");
            setupActivityScreen(isLoggedIn,serviceTheme, productTheme, bgTheme);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupActivityScreen(Var<Boolean> isLoggedIn, Var<String> serviceTheme, Var<String> productTheme, Var<String> bgTheme) throws JSONException {
        JSONObject serviceThemes = new JSONObject(serviceTheme.stringValue());
        JSONObject productThemes = new JSONObject(productTheme.stringValue());
        JSONObject bgThemes = new JSONObject(bgTheme.stringValue());
        Boolean isUserLoggedIn = isLoggedIn.value();

        if(isUserLoggedIn) {

            try {

                String themeColor = bgThemes.getString("bg_color");
                findViewById(R.id.main_layout).setBackgroundColor(Color.parseColor(themeColor));

                String textColor = bgThemes.getString("text_color");
                TextView productsTextView = findViewById(R.id.Products);
                TextView servicesTextView = findViewById(R.id.Services);

                productsTextView.setTextColor(Color.parseColor(textColor));
                servicesTextView.setTextColor(Color.parseColor(textColor));

                JSONObject bannerImages = new JSONObject(cleverTapAPI.getVariable("IPru.login.premium_banner_img").stringValue());
                Var<Integer> bannerImagesCnt = cleverTapAPI.getVariable("IPru.login.premium_banner_cnt");


                if(bannerImagesCnt.value() == 1){
                    bannerImage = Arrays.asList(
                            bannerImages.getString("img1")
                    );
                } else if (bannerImagesCnt.value() == 2) {
                    bannerImage = Arrays.asList(
                            bannerImages.getString("img1"),
                            bannerImages.getString("img2")
                    );

                } else if (bannerImagesCnt.value() == 3) {
                    bannerImage = Arrays.asList(
                            bannerImages.getString("img1"),
                            bannerImages.getString("img2"),
                            bannerImages.getString("img3")
                    );
                }else if (bannerImagesCnt.value() == 4) {
                    bannerImage = Arrays.asList(
                            bannerImages.getString("img1"),
                            bannerImages.getString("img2"),
                            bannerImages.getString("img3"),
                            bannerImages.getString("img4")
                    );
                } else{
                    bannerImage = Arrays.asList(
                            bannerImages.getString("img1"),
                            bannerImages.getString("img2"),
                            bannerImages.getString("img3"),
                            bannerImages.getString("img4"),
                            bannerImages.getString("img5")
                    );
                }

                if (bannerImage != null && bannerImage.size() > 1) {
                    startAutoSlide();
                }

                viewPagerBottom = findViewById(R.id.bottom_bannerViewPager);
                tabLayoutBottom = findViewById(R.id.bottom_tabDots);


// Set dynamic adapter
                BannersAdapter bannersAdapter = new BannersAdapter(this, bannerImage);
                viewPagerBottom.setAdapter(bannersAdapter);

                new TabLayoutMediator(tabLayoutBottom, viewPagerBottom, (tab, position) -> {
                    tab.setCustomView(R.layout.custom_dot);
                }).attach();

                viewPagerBottom.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        for (int i = 0; i < tabLayoutBottom.getTabCount(); i++) {
                            TabLayout.Tab tab = tabLayoutBottom.getTabAt(i);
                            if (tab != null && tab.getCustomView() != null) {
                                ImageView dot = (ImageView) tab.getCustomView();
                                dot.setBackgroundResource(i == position ? R.drawable.dot_selected : R.drawable.dot_unselected);
                            }
                        }
                    }
                });

                findViewById(R.id.bottom_bannerViewPager).setVisibility(View.VISIBLE);
                tabLayoutBottom.setVisibility(View.VISIBLE);

                findViewById(R.id.recycler_products).setVisibility(View.VISIBLE);
                findViewById(R.id.recycler_products).setVisibility(View.VISIBLE);

                getProductIcons = Arrays.asList(
                        productThemes.getString("iconImg1"),
                        productThemes.getString("iconImg2"),
                        productThemes.getString("iconImg3"),
                        productThemes.getString("iconImg4"),
                        productThemes.getString("iconImg5"),
                        productThemes.getString("iconImg6"),
                        productThemes.getString("iconImg7"),
                        productThemes.getString("iconImg8")
                );
                getProductTitles = Arrays.asList(
                        productThemes.getString("title1"),
                        productThemes.getString("title2"),
                        productThemes.getString("title3"),
                        productThemes.getString("title4"),
                        productThemes.getString("title5"),
                        productThemes.getString("title6"),
                        productThemes.getString("title7"),
                        productThemes.getString("title8")
                );
                getServiceIcons = Arrays.asList(
                        serviceThemes.getString("iconImg1"),
                        serviceThemes.getString("iconImg2"),
                        serviceThemes.getString("iconImg3"),
                        serviceThemes.getString("iconImg4"),
                        serviceThemes.getString("iconImg5"),
                        serviceThemes.getString("iconImg6"),
                        serviceThemes.getString("iconImg7"),
                        serviceThemes.getString("iconImg8")
                );
                getServiceTitles = Arrays.asList(
                        serviceThemes.getString("title1"),
                        serviceThemes.getString("title2"),
                        serviceThemes.getString("title3"),
                        serviceThemes.getString("title4"),
                        serviceThemes.getString("title5"),
                        serviceThemes.getString("title6"),
                        serviceThemes.getString("title7"),
                        serviceThemes.getString("title8")
                );

                setupRecycler(recyclerServices, getServiceTitles, getServiceIcons, textColor);
                setupRecycler1(recyclerProducts, getProductTitles, getProductIcons, textColor);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

            String themeColor = bgThemes.getString("bg_color");
            findViewById(R.id.main_layout).setBackgroundColor(Color.parseColor(themeColor));

            String textColor = bgThemes.getString("text_color");
            TextView productsTextView = findViewById(R.id.Products);
            TextView servicesTextView = findViewById(R.id.Services);

            productsTextView.setTextColor(Color.parseColor(textColor));
            servicesTextView.setTextColor(Color.parseColor(textColor));

            JSONObject bannerImages = new JSONObject(cleverTapAPI.getVariable("IPru.logout.premium_banner_img").stringValue());
            Var<Integer> bannerImagesCnt = cleverTapAPI.getVariable("IPru.logout.premium_banner_cnt");


            if(bannerImagesCnt.value() == 1){
                bannerImage = Arrays.asList(
                        bannerImages.getString("img1")
                );
            } else if (bannerImagesCnt.value() == 2) {
                bannerImage = Arrays.asList(
                        bannerImages.getString("img1"),
                        bannerImages.getString("img2")
                );

            } else if (bannerImagesCnt.value() == 3) {
                bannerImage = Arrays.asList(
                        bannerImages.getString("img1"),
                        bannerImages.getString("img2"),
                        bannerImages.getString("img3")
                );
            }else if (bannerImagesCnt.value() == 4) {
                bannerImage = Arrays.asList(
                        bannerImages.getString("img1"),
                        bannerImages.getString("img2"),
                        bannerImages.getString("img3"),
                        bannerImages.getString("img4")
                );
            } else{
                bannerImage = Arrays.asList(
                        bannerImages.getString("img1"),
                        bannerImages.getString("img2"),
                        bannerImages.getString("img3"),
                        bannerImages.getString("img4"),
                        bannerImages.getString("img5")
                );
            }

            viewPager = findViewById(R.id.bannerViewPager);
            tabLayout = findViewById(R.id.tabDots);

            // After bannerImage is filled
            if (bannerImage != null && bannerImage.size() > 1) {
                startAutoSlide();
            }

// Set dynamic adapter
            BannersAdapter bannersAdapter = new BannersAdapter(this, bannerImage);
            viewPager.setAdapter(bannersAdapter);

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

            findViewById(R.id.bannerViewPager).setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

            findViewById(R.id.recycler_products).setVisibility(View.VISIBLE);
            findViewById(R.id.recycler_products).setVisibility(View.VISIBLE);

            getProductIcons = Arrays.asList(
                    productThemes.getString("iconImg1"),
                    productThemes.getString("iconImg2"),
                    productThemes.getString("iconImg3"),
                    productThemes.getString("iconImg4"),
                    productThemes.getString("iconImg5"),
                    productThemes.getString("iconImg6"),
                    productThemes.getString("iconImg7"),
                    productThemes.getString("iconImg8")
            );
            getProductTitles = Arrays.asList(
                    productThemes.getString("title1"),
                    productThemes.getString("title2"),
                    productThemes.getString("title3"),
                    productThemes.getString("title4"),
                    productThemes.getString("title5"),
                    productThemes.getString("title6"),
                    productThemes.getString("title7"),
                    productThemes.getString("title8")
            );
            getServiceIcons = Arrays.asList(
                    serviceThemes.getString("iconImg1"),
                    serviceThemes.getString("iconImg2"),
                    serviceThemes.getString("iconImg3"),
                    serviceThemes.getString("iconImg4"),
                    serviceThemes.getString("iconImg5"),
                    serviceThemes.getString("iconImg6"),
                    serviceThemes.getString("iconImg7"),
                    serviceThemes.getString("iconImg8")
            );
            getServiceTitles = Arrays.asList(
                    serviceThemes.getString("title1"),
                    serviceThemes.getString("title2"),
                    serviceThemes.getString("title3"),
                    serviceThemes.getString("title4"),
                    serviceThemes.getString("title5"),
                    serviceThemes.getString("title6"),
                    serviceThemes.getString("title7"),
                    serviceThemes.getString("title8")
            );

            setupRecycler(recyclerServices, getServiceTitles, getServiceIcons, textColor);
            setupRecycler1(recyclerProducts, getProductTitles, getProductIcons, textColor);


        }
    }


    private void startAutoSlide() {
        runnable = new Runnable() {
            @Override
            public void run() {
                int nextTop = (viewPager.getCurrentItem() + 1) % bannerImage.size();
                int nextBottom = (viewPagerBottom.getCurrentItem() + 1) % bannerImage.size();

                viewPager.setCurrentItem(nextTop, true);
                viewPagerBottom.setCurrentItem(nextBottom, true);

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
        //startAutoSlide();
    }

    private void updateSubView(boolean isPremiumSelected) {
        if (isPremiumSelected) {
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Anonymous User", "Logged In");
            cleverTapAPI.pushProfile(profileUpdate);
            Toast.makeText(this, "User Logged In", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Anonymous User", "Logged Out");
            cleverTapAPI.pushProfile(profileUpdate);
            Toast.makeText(this, "User Logged Out", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to save toggle state in SharedPreferences
    private void saveToggleState(boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("subState", isChecked);
        editor.apply();

    }

    private void setupRecycler(RecyclerView recyclerView, List<String> titles, List<String> icons, String color) {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new IconAdapter(this, titles, icons, color));
    }

    private void setupRecycler1(RecyclerView recyclerView, List<String> titles, List<String> icons, String color) {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new IconsAdapter(this, titles, icons, color));
    }

//    private List<String> getServiceTitles() {
//        return Arrays.asList("Premium", "Statement", "Claim", "Top Up", "Fund Value", "Policy Update", "Nominee", "More");
//    }
//
//    private List<Integer> getServiceIcons() {
//        return Arrays.asList(R.drawable.ic_premium, R.drawable.ic_statement, R.drawable.ic_claim, R.drawable.ic_topup,
//                R.drawable.ic_fund, R.drawable.ic_policy, R.drawable.ic_nominee, R.drawable.ic_more);
//    }
//
//    private List<String> getProductTitles() {
//        return Arrays.asList("Term Insurance", "Crorepati", "Unit Linked", "Education", "Retirement", "Savings", "Car", "Annuity");
//    }
//
//    private List<Integer> getProductIcons() {
//        return Arrays.asList(R.drawable.ic_terms, R.drawable.ic_crorepati, R.drawable.pricing,
//                R.drawable.ic_child, R.drawable.ic_retire, R.drawable.ic_savings, R.drawable.ic_car, R.drawable.ic_annuity);
//    }
}