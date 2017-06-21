package com.example.vartikasharma.bookingpage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
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

    public ExpandableAdapter(List<String> slots, HashMap<String, List<SlotItem>> childItems) {
        this.slots = slots;
        this.childItems = childItems;
        setHasStableIds(true);
        for (int i = 0; i < slots.size(); i++) {
            List<SlotItem> slotItemList = childItems.get(slots.get(i));
            for (int j = 0; j < slotItemList.size(); j++) {
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
        String stringText = item.substring(0, 1).toUpperCase() + item.substring(1);
        holder.mTextView.setText(stringText);
        holder.slotNoText.setText(" " + availableSlotNo.get(item) + " " + "Slots available");

        // mark as clickable
        holder.itemView.setClickable(true);

        // set background resource (target view ID: container)
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & ExpandableItemConstants.STATE_FLAG_IS_UPDATED) != 0) {
            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                holder.arrowImage.animate().rotation(180).start();
            } else {
                holder.arrowImage.animate().rotation(0).start();
                ;
            }
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
            String startTime = simpleDateFormat.format(startDateTime);
            Log.i(LOG_TAG, "start time, " + startTime);
            Log.i(LOG_TAG, "startDateTime, " + startDateTime.getTime());
            Date endDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss+00:00", Locale.US).parse(item.getEnd_time());
            String endTime = simpleDateFormat.format(endDateTime);
            Log.i(LOG_TAG, "endTime, " + endTime);
            holder.mTextView.setText(" " + startTime + "-" + endTime);
            if (item.is_booked()) {
                holder.mContainer.setBackgroundResource(R.color.not_available_slot_background);
            } else {
                holder.mContainer.setBackgroundResource(R.color.available_slot_background);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        public ImageView morningSnackImage;
        public TextView mTextView;
        public TextView slotNoText;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = (RelativeLayout) v.findViewById(R.id.container);
            morningSnackImage = (ImageView) v.findViewById(R.id.morning_snack_image);
            mTextView = (TextView) v.findViewById(R.id.text1);
            slotNoText = (TextView) v.findViewById(R.id.slot_no_text);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public ImageView arrowImage;

        public MyGroupViewHolder(View v) {
            super(v);
            arrowImage = (ImageView) v.findViewById(R.id.arrow_image);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public MyChildViewHolder(View v) {
            super(v);
        }
    }
}
