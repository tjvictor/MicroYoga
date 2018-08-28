package microYoga.model;

public class ScheduleExt extends Schedule {

    private int orderedCount;
    private boolean isOrdered;
    private String startTime;
    private String endTime;

    public int getOrderedCount() {
        return orderedCount;
    }

    public void setOrderedCount(int orderedCount) {
        this.orderedCount = orderedCount;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }
}
