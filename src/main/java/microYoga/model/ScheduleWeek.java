package microYoga.model;

import java.util.ArrayList;
import java.util.List;

public class ScheduleWeek {

    private String shortDate;
    private String weekName;
    private List<ScheduleExt> scheduleExts;

    public ScheduleWeek(){

        this.scheduleExts = new ArrayList<ScheduleExt>();
    }

    public String getShortDate() {
        return shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public List<ScheduleExt> getScheduleExts() {
        return scheduleExts;
    }

    public void setScheduleExts(List<ScheduleExt> scheduleExts) {
        this.scheduleExts = scheduleExts;
    }
}
