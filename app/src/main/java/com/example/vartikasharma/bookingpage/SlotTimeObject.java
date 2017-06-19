package com.example.vartikasharma.bookingpage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotTimeObject {
    private Map<String, List<SlotItem>> slotItem  = new HashMap<String, List<SlotItem>>();

    public Map<String, List<SlotItem>> getSlotItem() {
        return slotItem;
    }

    public void setSlotItem(Map<String, List<SlotItem>> slotItem) {
        this.slotItem = slotItem;
    }
}