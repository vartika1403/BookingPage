package com.example.vartikasharma.bookingpage;


import java.util.HashMap;
import java.util.List;

public class BookingSlot {
    private HashMap<String, SlotDateObject> slots = new HashMap<String, SlotDateObject>();

    public void setSlots(HashMap<String, SlotDateObject> slots) {
        this.slots = slots;
    }

    public HashMap<String, SlotDateObject> getSlots() {
        return slots;
    }
}
