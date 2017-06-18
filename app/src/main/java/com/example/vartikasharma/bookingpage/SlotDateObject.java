package com.example.vartikasharma.bookingpage;


import java.util.List;

public class SlotDateObject {
    private List<SlotItem> afternoon;
    private List<SlotItem> evening;
    private List<SlotItem> morning;

    public List<SlotItem> getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(List<SlotItem> afternoon) {
        this.afternoon = afternoon;
    }

    public List<SlotItem> getEvening() {
        return evening;
    }

    public void setEvening(List<SlotItem> evening) {
        this.evening = evening;
    }

    public List<SlotItem> getMorning() {
        return morning;
    }

    public void setMorning(List<SlotItem> morning) {
        this.morning = morning;
    }
}
