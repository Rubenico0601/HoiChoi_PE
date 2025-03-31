package com.example.hoichoipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.variables.Var;
import com.clevertap.android.sdk.variables.callbacks.FetchVariablesCallback;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView youMayAlsoLike;
    private RecyclerView newAdditions;
    private ViewPager2 topBanner;
    private Switch genderToggle;
    CleverTapAPI cleverTapAPI;
    private Var<String> subTheme, newAddTheme;
    private Var<String> premiumImages;
    private Var<Integer> premiumImagesCnt;
    EditText searchBarTop;
    private Var<Boolean> isPremium;
    private SharedPreferences sharedPreferences;
    ArrayAdapter<String> adapter;
    private List<String> bannerImages, youMayAlsoLikeImages, newAdditionImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cleverTapAPI = CleverTapAPI.getDefaultInstance(this);
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);

        List<String> searchSuggestions = new ArrayList<>();
        searchSuggestions.add("Inception");
        searchSuggestions.add("La La Land");
        searchSuggestions.add("Lion King");
        searchSuggestions.add("Shutter Island");
        searchSuggestions.add("Bohemian Rhapsody");
        searchSuggestions.add("Golmaal");
        searchSuggestions.add("Thriller Movies");
        searchSuggestions.add("Romantic Movies");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, searchSuggestions);

        //Top Banner Premium
        premiumImages = cleverTapAPI.defineVariable("Hoichoi.premium.premium_banner_img", "str");
        premiumImagesCnt = cleverTapAPI.defineVariable("Hoichoi.premium.premium_banner_cnt", 4);

        //You may also like
        subTheme = cleverTapAPI.defineVariable("Hoichoi.YMAL.themeData", "str");

        //New Additions
        newAddTheme = cleverTapAPI.defineVariable("Hoichoi.NAdd.themeData", "str");

        //User Type
        isPremium = cleverTapAPI.defineVariable("Hoichoi.isPremium", true);

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

        // Existing elements
        searchBarTop = findViewById(R.id.search_bar_top);
        youMayAlsoLike = findViewById(R.id.circular_carousel);
        newAdditions = findViewById(R.id.new_addition_card);


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


    }

    private void autoScrollBanner(ViewPager2 viewPager, int itemCount) {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            int currentPage = 0;

            public void run() {
                if (currentPage >= itemCount) {
                    currentPage = 0;  // Reset to first item
                }
                viewPager.setCurrentItem(currentPage++, true);  // Scroll to next item
                handler.postDelayed(this, 3000); // Change image every 3 seconds
            }
        };
        handler.postDelayed(update, 3000);
    }


    private void handleFetchedVariables() {
        try {
            Var<String> subTheme = cleverTapAPI.getVariable("Hoichoi.YMAL.themeData");
            Var<String> newAddition = cleverTapAPI.getVariable("Hoichoi.NAdd.themeData");
            Var<Boolean> isPremium = cleverTapAPI.getVariable("Hoichoi.isPremium");
            setupActivityScreen(isPremium,subTheme, newAddition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupActivityScreen(Var<Boolean> isPremium, Var<String> subTheme, Var<String> newAddition) throws JSONException {
        JSONObject themeObject = new JSONObject(subTheme.stringValue());
        JSONObject newAdd = new JSONObject(newAddition.stringValue());
        Boolean isPremiumUser = isPremium.value();

        if(isPremiumUser) {

            try {

                String autoText = themeObject.getString("autoText");
                String themeColor = themeObject.getString("theme_color");
                findViewById(R.id.main_layout).setBackgroundColor(Color.parseColor(themeColor));
                if ("true".equalsIgnoreCase(autoText)) {
                    AutoCompleteTextView searchBarTop = findViewById(R.id.search_bar_top_auto);
                    searchBarTop.setVisibility(View.VISIBLE);
                    searchBarTop.setAdapter(adapter);
                }else{
                    findViewById(R.id.search_bar_top).setVisibility(View.VISIBLE);
                }

                JSONObject premiumImages = new JSONObject(cleverTapAPI.getVariable("Hoichoi.premium.premium_banner_img").stringValue());
                Var<Integer> premiumImagesCnt = cleverTapAPI.getVariable("Hoichoi.premium.premium_banner_cnt");


                // Load Top Banner Image
                topBanner = findViewById(R.id.top_premium_banner);

                if(premiumImagesCnt.value() == 1){
                    bannerImages = Arrays.asList(
                            premiumImages.getString("img1")
                    );
                } else if (premiumImagesCnt.value() == 2) {
                    bannerImages = Arrays.asList(
                            premiumImages.getString("img1"),
                            premiumImages.getString("img2")
                    );

                } else if (premiumImagesCnt.value() == 3) {
                    bannerImages = Arrays.asList(
                            premiumImages.getString("img1"),
                            premiumImages.getString("img2"),
                            premiumImages.getString("img3")
                    );
                }else{
                    bannerImages = Arrays.asList(
                            premiumImages.getString("img1"),
                            premiumImages.getString("img2"),
                            premiumImages.getString("img3"),
                            premiumImages.getString("img4")
                    );
                }

                // Set up adapters
                topBanner.setAdapter(new BannerAdapter(this, bannerImages));

                if(premiumImagesCnt.value() > 1){
                    autoScrollBanner(topBanner, bannerImages.size());
                }

                findViewById(R.id.premium).setVisibility(View.VISIBLE);
                topBanner.setVisibility(View.VISIBLE);

                findViewById(R.id.categories).setVisibility(View.VISIBLE);
                findViewById(R.id.circular_carousel).setVisibility(View.VISIBLE);

                youMayAlsoLike.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                // Load carousel images
                youMayAlsoLikeImages = Arrays.asList(
                        themeObject.getString("img1"),
                        themeObject.getString("img2"),
                        themeObject.getString("img3"),
                        themeObject.getString("img4"),
                        themeObject.getString("img5")
                );
                TallCardAdapter tallCardAdapter = new TallCardAdapter(this, youMayAlsoLikeImages);
                youMayAlsoLike.setAdapter(tallCardAdapter);
                youMayAlsoLike.setHorizontalScrollBarEnabled(false);

                findViewById(R.id.new_addition).setVisibility(View.VISIBLE);
                findViewById(R.id.new_addition_card).setVisibility(View.VISIBLE);
                newAdditions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                newAdditionImages = Arrays.asList(
                        newAdd.getString("img1"),
                        newAdd.getString("img2"),
                        newAdd.getString("img3"),
                        newAdd.getString("img4")
                );
                NewAdditionAdapter newAdditionAdapter = new NewAdditionAdapter(this, newAdditionImages);
                newAdditions.setAdapter(newAdditionAdapter);
                newAdditions.setHorizontalScrollBarEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

            String autoText = themeObject.getString("autoText");
            String themeColor = themeObject.getString("theme_color");
            findViewById(R.id.main_layout).setBackgroundColor(Color.parseColor(themeColor));

            if ("true".equalsIgnoreCase(autoText)) {
                AutoCompleteTextView searchBarTop = findViewById(R.id.search_bar_top_auto);
                searchBarTop.setVisibility(View.VISIBLE);
                searchBarTop.setAdapter(adapter);
            }else{
                findViewById(R.id.search_bar_top).setVisibility(View.VISIBLE);
            }

            findViewById(R.id.categories).setVisibility(View.VISIBLE);
            findViewById(R.id.circular_carousel).setVisibility(View.VISIBLE);

            youMayAlsoLike.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            // Load carousel images
            youMayAlsoLikeImages = Arrays.asList(
                    themeObject.getString("img1"),
                    themeObject.getString("img2"),
                    themeObject.getString("img3"),
                    themeObject.getString("img4"),
                    themeObject.getString("img5")
            );
            TallCardAdapter tallCardAdapter = new TallCardAdapter(this, youMayAlsoLikeImages);
            youMayAlsoLike.setAdapter(tallCardAdapter);
            youMayAlsoLike.setHorizontalScrollBarEnabled(false);

            findViewById(R.id.new_addition).setVisibility(View.VISIBLE);
            findViewById(R.id.new_addition_card).setVisibility(View.VISIBLE);
            newAdditions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            newAdditionImages = Arrays.asList(
                    newAdd.getString("img1"),
                    newAdd.getString("img2"),
                    newAdd.getString("img3"),
                    newAdd.getString("img4")
            );
            NewAdditionAdapter newAdditionAdapter = new NewAdditionAdapter(this, newAdditionImages);
            newAdditions.setAdapter(newAdditionAdapter);
            newAdditions.setHorizontalScrollBarEnabled(false);


        }
    }



    // Function to update UI based on gender selection
    private void updateSubView(boolean isPremiumSelected) {
        if (isPremiumSelected) {
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Subscription Type", "Premium User");
            cleverTapAPI.pushProfile(profileUpdate);
            Toast.makeText(this, "Premium User selected", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Subscription Type", "Basic User");
            cleverTapAPI.pushProfile(profileUpdate);
            Toast.makeText(this, "Basic User selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to save toggle state in SharedPreferences
    private void saveToggleState(boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("subState", isChecked);
        editor.apply();

    }
}

