package com.example.vartikasharma.bookingpage;


import java.util.HashMap;

public class SlotDateObject {
    private HashMap<String, SlotTimeObject> slotTimeObjectHashMap = new HashMap<String, SlotTimeObject>();

    public HashMap<String, SlotTimeObject> getSlotTimeObjectHashMap() {
        return slotTimeObjectHashMap;
    }

    public void setSlotTimeObjectHashMap(HashMap<String, SlotTimeObject> slotTimeObjectHashMap) {
        this.slotTimeObjectHashMap = slotTimeObjectHashMap;
    }
}
