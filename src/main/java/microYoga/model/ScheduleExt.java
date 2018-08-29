package microYoga.model;

public class ScheduleExt extends Schedule {

    private int orderedCount;
    private boolean isOrdered;
    private String orderId;
    private String startTime;
    private String endTime;
    private int residualCount;

    public int getResidualCount() {
        return getCapacity() - orderedCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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
