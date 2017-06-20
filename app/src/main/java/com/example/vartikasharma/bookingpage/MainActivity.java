package com.example.vartikasharma.bookingpage;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.i(LOG_TAG, "apiservice, " + apiService.toString());

        Call<JsonElement> call = apiService.getBookingSlots(USER_NAME, API_KEY, VC, EXPERT_USERNAME, FORMAT);

        Log.i(LOG_TAG, "url, " + call.request().url());

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(final Call<JsonElement> call, Response<JsonElement> response) {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                Log.i(LOG_TAG, "response, " + response.body().getAsJsonObject().toString());
                String resonseArray = "[" + response.body().getAsJsonObject().toString() + "]";
                Log.i(LOG_TAG, "responseArray, " + response.body().getAsJsonObject().get("slots"));
               // response.body().getAsJsonObject().get("slots");
               BookingSlot bookingSlot = gson.fromJson(response.body().getAsJsonObject().toString() , BookingSlot.class);
                Log.i(LOG_TAG, "slots are, " + bookingSlot.getSlots());
               final HashMap<String, HashMap<String, List<SlotItem>>> slotDateObjectHashMap = bookingSlot.getSlots();
                Log.i(LOG_TAG, "slotDate, " +  slotDateObjectHashMap);
               SlotItem slotItem = slotDateObjectHashMap.get("2017-06-20").get("afternoon").get(0);
                Log.i(LOG_TAG, "slotItem, " + slotItem.getSlot_id());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpViewPager(viewPager, slotDateObjectHashMap);
                        setUpTabLayout();
                        bookingSlotTabLayout.setupWithViewPager(viewPager);
                    }
                });
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager, HashMap<String, HashMap<String, List<SlotItem>>> slotDateObjectHashMap) {
        List<String> mapKey = new ArrayList<>();
        for(String date: slotDateObjectHashMap.keySet()) {
            mapKey.add(date);
        }
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFrag(new FirstDateBookingSlot(slotDateObjectHashMap.get(mapKey.get(0))), "first");
           viewPagerAdapter.addFrag(new SecondDateBookingSlot(slotDateObjectHashMap.get(mapKey.get(1))), "second");
            viewPagerAdapter.addFrag(new ThirdDateBookingSlot(slotDateObjectHashMap.get(mapKey.get(2))), "third");
            viewPagerAdapter.addFrag(new FourthDateBookingSlot(slotDateObjectHashMap.get(mapKey.get(3))), "fourth");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.requestTransparentRegion(viewPager);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(LOG_TAG, "onPageScrolled called: " + position + " " + positionOffset + " " + positionOffsetPixels);
                FragmentChangeListener fragmentShown = (FragmentChangeListener) viewPagerAdapter.getItem(1);
                fragmentShown.onScrollFragment(position, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(LOG_TAG, "onPageSelected called");
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

    private void setUpTabLayout() {
        // Add Tab
        bookingSlotTabLayout.addTab(bookingSlotTabLayout.newTab().setText("19"));
        bookingSlotTabLayout.addTab(bookingSlotTabLayout.newTab().setText("20"));
        bookingSlotTabLayout.addTab(bookingSlotTabLayout.newTab().setText("21"));
        bookingSlotTabLayout.addTab(bookingSlotTabLayout.newTab().setText("22"));
    }
}
