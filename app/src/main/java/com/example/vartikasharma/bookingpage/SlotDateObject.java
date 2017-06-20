package com.example.vartikasharma.bookingpage;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotDateObject {
    private HashMap<String, Map<String, List<SlotItem>>> slotTimeObjectHashMap = new HashMap<String,  Map<String, List<SlotItem>>>();

    public HashMap<String, Map<String, List<SlotItem>>> getSlotTimeObjectHashMap() {
        return slotTimeObjectHashMap;
    }

    public void setSlotTimeObjectHashMap(HashMap<String, Map<String, List<SlotItem>>> slotTimeObjectHashMap) {
        this.slotTimeObjectHashMap = slotTimeObjectHashMap;
    }
}
