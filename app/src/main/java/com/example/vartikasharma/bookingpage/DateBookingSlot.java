package com.example.vartikasharma.bookingpage;

import android.content.Context;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateBookingSlot extends Fragment implements FragmentChangeListener,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {
    private static final String LOG_TAG = DateBookingSlot.class.getSimpleName();
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    @BindView(R.id.first_recycler_view)
    RecyclerView firstRecyclerView;
    private RecyclerView.LayoutManager firstLayoutManager;
    private RecyclerView.Adapter firstWrappedAdapter;
    private RecyclerViewExpandableItemManager firstRecyclerViewExpandableItemManager;
    private HashMap<String, List<SlotItem>> slotFirstDateItems;

    public DateBookingSlot(HashMap<String, List<SlotItem>> slotFirstDateItems) {
        this.slotFirstDateItems = slotFirstDateItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for_booking_slot, container, false);
        ButterKnife.bind(this, view);
        firstLayoutManager = new LinearLayoutManager(getContext());

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        firstRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        firstRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        firstRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        List<String> slots = new ArrayList<String>();
        HashMap<String, List<SlotItem>> childItems = new HashMap<>();
        for (String slot : slotFirstDateItems.keySet()) {
            slots.add(slot);
            childItems.put(slot, slotFirstDateItems.get(slot));
        }

        final ExpandableAdapter myItemAdapter = new ExpandableAdapter(slots, childItems);

        firstWrappedAdapter = firstRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);       // wrap for expanding
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        firstRecyclerView.setLayoutManager(firstLayoutManager);
        firstRecyclerView.setAdapter(firstWrappedAdapter);  // requires *wrapped* adapter
        firstRecyclerView.setItemAnimator(animator);
        firstRecyclerView.setHasFixedSize(false);

        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            firstRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        firstRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));
        firstRecyclerViewExpandableItemManager.attachRecyclerView(firstRecyclerView);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state to support screen rotation, etc...
        if (firstRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    firstRecyclerViewExpandableItemManager.getSavedState());
        }
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
    public void onDestroyView() {
        if (firstRecyclerViewExpandableItemManager != null) {
            firstRecyclerViewExpandableItemManager.release();
            firstRecyclerViewExpandableItemManager = null;
        }

        if (firstRecyclerView != null) {
            firstRecyclerView.setItemAnimator(null);
            firstRecyclerView.setAdapter(null);
            firstRecyclerView = null;
        }

        if (firstWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(firstWrappedAdapter);
            firstWrappedAdapter = null;
        }
        firstLayoutManager = null;

        super.onDestroyView();
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

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }
}
