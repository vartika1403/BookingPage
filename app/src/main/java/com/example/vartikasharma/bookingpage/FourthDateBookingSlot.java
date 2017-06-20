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

public class FourthDateBookingSlot extends Fragment implements FragmentChangeListener,
        RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {
    private static final String LOG_TAG = FourthDateBookingSlot.class.getSimpleName();
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private RecyclerView.LayoutManager fourthLayoutManager;
    private RecyclerView.Adapter fourthWrappedAdapter;
    private RecyclerViewExpandableItemManager fourthRecyclerViewExpandableItemManager;
    private HashMap<String, List<SlotItem>> slotFourthDateItems;

    @BindView(R.id.fouth_recycler_view)
    RecyclerView fourthRecyclerView;

    public FourthDateBookingSlot(HashMap<String, List<SlotItem>> slotFourthDateItems) {
        this.slotFourthDateItems = slotFourthDateItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fourth_date_booking_slot, container, false);
        ButterKnife.bind(this,view);
        fourthLayoutManager = new LinearLayoutManager(getContext());

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        fourthRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        fourthRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        fourthRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        List<String> slots = new ArrayList<String>();
        //   slots.add("Morning");
        // slots.add("Evening");
       /* List<String> morningData = new ArrayList<>();
        morningData.add("1");
        morningData.add("2");*/
        HashMap<String, List<SlotItem>> childItems = new HashMap<>();
        for(String slot: slotFourthDateItems.keySet()) {
            slots.add(slot);
            childItems.put(slot, slotFourthDateItems.get(slot));
            // childItems.put(slots.get(1), morningData);
        }
        final ExpandableAdapter myItemAdapter = new ExpandableAdapter(fourthRecyclerViewExpandableItemManager,slots, childItems);

        fourthWrappedAdapter = fourthRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);       // wrap for expanding

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Need to disable them when using animation indicator.
        animator.setSupportsChangeAnimations(false);

        fourthRecyclerView.setLayoutManager(fourthLayoutManager);
        fourthRecyclerView.setAdapter(fourthWrappedAdapter);  // requires *wrapped* adapter
        fourthRecyclerView.setItemAnimator(animator);
        fourthRecyclerView.setHasFixedSize(false);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            fourthRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        fourthRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        fourthRecyclerViewExpandableItemManager.attachRecyclerView(fourthRecyclerView);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state to support screen rotation, etc...
        if (fourthRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    fourthRecyclerViewExpandableItemManager.getSavedState());
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
        if (fourthRecyclerViewExpandableItemManager != null) {
            fourthRecyclerViewExpandableItemManager.release();
            fourthRecyclerViewExpandableItemManager = null;
        }

        if (fourthRecyclerView != null) {
            fourthRecyclerView.setItemAnimator(null);
            fourthRecyclerView.setAdapter(null);
            fourthRecyclerView = null;
        }

        if (fourthWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(fourthWrappedAdapter);
            fourthWrappedAdapter = null;
        }
        fourthLayoutManager = null;

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

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (getActivity().getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        fourthRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

   /* public AbstractExpandableDataProvider getDataProvider() {
        return abstractExpandableDataProvider;
       // return ((MainActivity) getActivity()).getDataProvider();

    }*/
}
