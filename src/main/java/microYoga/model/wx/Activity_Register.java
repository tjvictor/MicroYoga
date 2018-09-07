package microYoga.model.wx;

import java.util.ArrayList;
import java.util.List;

public class Activity_Register {
    private String id;
    private String activityId;
    private String registerId;
    private String registerName;
    private String date;

    private List<Activity_Participate> activity_participateList;

    public Activity_Register(){
        this.activity_participateList = new ArrayList<Activity_Participate>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        date = date;
    }

    public List<Activity_Participate> getActivity_participateList() {
        return activity_participateList;
    }

    public void setActivity_participateList(List<Activity_Participate> activity_participateList) {
        this.activity_participateList = activity_participateList;
    }
}
