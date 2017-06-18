package com.example.vartikasharma.bookingpage;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import butterknife.ButterKnife;

public class FirstDateBookingSlot extends Fragment implements FragmentChangeListener,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {
    private static final String LOG_TAG = FirstDateBookingSlot.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for19_booking_slot, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onShowFragment() {

    }

    @Override
    public void onHideFragment() {

    }

    @Override
    public void onScrollFragment(int position, int offset) {

    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser, Object payload) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser, Object payload) {

    }
}
