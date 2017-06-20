package com.example.vartikasharma.bookingpage;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingSlot {
    private HashMap<String, HashMap<String, List<SlotItem>>> slots = new HashMap<>();

    public void setSlots(HashMap<String, HashMap<String, List<SlotItem>>> slots) {
        this.slots = slots;
    }

    public HashMap<String, HashMap<String, List<SlotItem>>> getSlots() {
        return slots;
    }
}
