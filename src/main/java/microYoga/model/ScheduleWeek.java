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
        switch (weekName.toLowerCase()){
            case "mon": this.weekName = "周一"; break;
            case "tue": this.weekName = "周二"; break;
            case "wed": this.weekName = "周三"; break;
            case "thu": this.weekName = "周四"; break;
            case "fri": this.weekName = "周五"; break;
            case "sat": this.weekName = "周六"; break;
            case "sun": this.weekName = "周日"; break;
            default: this.weekName = ""; break;
        }
    }

    public List<ScheduleExt> getScheduleExts() {
        return scheduleExts;
    }

    public void setScheduleExts(List<ScheduleExt> scheduleExts) {
        this.scheduleExts = scheduleExts;
    }
}
