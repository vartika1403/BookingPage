package com.example.vartikasharma.bookingpage;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "a4aeb4e27f27b5786828f6cdf00d8d2cb44fe6d7";
    private final static int VC = 276;
    private final static String USER_NAME = "alok@x.coz";
    private final static String EXPERT_USERNAME = "neha@healthifyme.com";
    private final static String FORMAT = "json";

    @BindView(R.id.tabLayout)
    /* package-local */ TabLayout bookingSlotTabLayout;

    @BindView(R.id.pager)
    /* package-local */ ViewPager viewPager;

    @BindView(R.id.month_name)
    /* package-local */ TextView monthNameText;

    @BindView(R.id.progress_bar)
    /* package-local */ ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();


        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiService.getBookingSlots(USER_NAME, API_KEY, VC, EXPERT_USERNAME, FORMAT);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(final Call<JsonElement> call, Response<JsonElement> response) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                BookingSlot bookingSlot = gson.fromJson(response.body().getAsJsonObject().toString(), BookingSlot.class);
                final HashMap<String, HashMap<String, List<SlotItem>>> slotDateObjectHashMap = bookingSlot.getSlots();
                setUpViewPager(viewPager, slotDateObjectHashMap);
                bookingSlotTabLayout.setupWithViewPager(viewPager);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setUpTabLayout(slotDateObjectHashMap);
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // toolbar.setTitle(R.string.toolbarTitle);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        // toolbar.setNavigationIcon();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);



    private void setUpViewPager(ViewPager viewPager, HashMap<String, HashMap<String, List<SlotItem>>> slotDateObjectHashMap) {
        List<String> mapKey = new ArrayList<>();
        for (String date : slotDateObjectHashMap.keySet()) {
            mapKey.add(date);
        }
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < slotDateObjectHashMap.size(); i++) {
            viewPagerAdapter.addFrag(new DateBookingSlot(slotDateObjectHashMap.get(mapKey.get(i))), mapKey.get(i));
        }
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.requestTransparentRegion(viewPager);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FragmentChangeListener fragmentShown = (FragmentChangeListener) viewPagerAdapter.getItem(1);
                fragmentShown.onScrollFragment(position, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                FragmentChangeListener fragmentShown = (FragmentChangeListener) viewPagerAdapter.getItem(position);
                fragmentShown.onShowFragment();

                FragmentChangeListener fragmentHidden = (FragmentChangeListener) viewPagerAdapter.getItem(currentPosition);
                fragmentHidden.onHideFragment();

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(LOG_TAG, "onPageScrollStateChanged called" + state);
            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void setUpTabLayout(HashMap<String, HashMap<String, List<SlotItem>>> slotDateObjectHashMap) throws ParseException {
        Map.Entry<String, HashMap<String, List<SlotItem>>> entry = slotDateObjectHashMap.entrySet().iterator().next();
        String key = entry.getKey();
        Date dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(key);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.ENGLISH);
        String month_name = month_date.format(dateValue);
        monthNameText.setText(month_name);
        int i = slotDateObjectHashMap.size() -1;
        for (String dateText : slotDateObjectHashMap.keySet()) {
            View tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabText = (TextView) tabOne.findViewById(R.id.tab_day_no_text);
            TextView tabDayName = (TextView) tabOne.findViewById(R.id.tab_day_name_text);
            Date dateName = new SimpleDateFormat("yyyy-MM-dd").parse(dateText);
            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateName);
            String dateOfWeek = new SimpleDateFormat("dd", Locale.ENGLISH).format(dateName);
            tabDayName.setText(dayOfWeek);
            tabText.setText(dateOfWeek);
            bookingSlotTabLayout.getTabAt(i).setCustomView(tabOne);
            i--;
        }
    }
}
