package com.example.vartikasharma.bookingpage;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ExpandableAdapter extends
        AbstractExpandableItemAdapter<ExpandableAdapter.MyGroupViewHolder,
                ExpandableAdapter.MyChildViewHolder> {
    private static final String LOG_TAG = ExpandableAdapter.class.getSimpleName();

    private List<String> slots = new ArrayList<>();
    private HashMap<String, List<SlotItem>> childItems = new HashMap<>();
    private int availableSlots = 0;
    private HashMap<String, Integer> availableSlotNo = new HashMap<>();

    public ExpandableAdapter(RecyclerViewExpandableItemManager recyclerViewExpandableItemManager, List<String> slots, HashMap<String, List<SlotItem>> childItems) {

        this.slots = slots;
        this.childItems = childItems;
        setHasStableIds(true);
        for(int i = 0; i < slots.size(); i++) {
            List<SlotItem> slotItemList = childItems.get(slots.get(i));
            for (int j = 0; j <slotItemList.size(); j++) {
                if (!slotItemList.get(j).is_booked() || !slotItemList.get(j).is_expired()) {
                    availableSlots++;
                }
            }
            availableSlotNo.put(slots.get(i), availableSlots);
            availableSlots = 0;
        }
    }

    @Override
    public int getGroupCount() {
        return slots.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
       return childItems.get(slots.get(groupPosition)).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {
        // child item
        final String item = slots.get(groupPosition);

        // set text
        holder.mTextView.setText(item);
        holder.slotNoText.setText(" " + availableSlotNo.get(item) + " " + "Slots available");

        // mark as clickable
        holder.itemView.setClickable(true);

        // set background resource (target view ID: container)
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // group item
        final SlotItem item = childItems.get(slots.get(groupPosition)).get(childPosition);
        // set text
        try {
            String strDateFormat = "hh:mm:ss a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strDateFormat);
            Date startDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss+00:00", Locale.US).parse(item.getStart_time());
            String startTime =  simpleDateFormat.format(startDateTime);
            Log.i(LOG_TAG, "start time, "  + startTime);
            Log.i(LOG_TAG, "startDateTime, " + startDateTime.getTime());
            Date endDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss+00:00", Locale.US).parse(item.getEnd_time());
            String endTime = simpleDateFormat.format(endDateTime);
            Log.i(LOG_TAG, "endTime, " + endTime);
            holder.mTextView.setText(" " + startTime + "-" + endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // set background resource (target view ID: container)
        int bgResId;
        bgResId = R.drawable.bg_item_normal_state;
        holder.mContainer.setBackgroundResource(bgResId);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        return true;
    }

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        public RelativeLayout mContainer;
        public TextView mTextView;
        public TextView slotNoText;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = (RelativeLayout) v.findViewById(R.id.container);
            mTextView = (TextView) v.findViewById(R.id.text1);
            slotNoText = (TextView) v.findViewById(R.id.slot_no_text);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public ExpandableItemIndicator mIndicator;

        public MyGroupViewHolder(View v) {
            super(v);
            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public MyChildViewHolder(View v) {
            super(v);
        }
    }
}
