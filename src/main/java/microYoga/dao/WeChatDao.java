package microYoga.dao;

import microYoga.model.wx.Activity;
import microYoga.model.wx.Activity_Participate;
import microYoga.model.wx.Activity_Register;
import microYoga.model.wx.SNSUserInfo;

import java.sql.SQLException;
import java.util.List;

public interface WeChatDao {

    void insertSNSUserInfo(SNSUserInfo item) throws SQLException;

    void insertActivityRegister(Activity_Register item) throws SQLException;

    void insertActivityParticipate(Activity_Participate item) throws SQLException;

    List<Activity> getActivityById(String id) throws SQLException;

    List<Activity_Register> getActivityRegistersByActivityId(String activityId) throws SQLException;

    List<Activity_Participate> getActivityParticipatesByActivityIdAndRegisterId(String activityId, String registerId) throws SQLException;
}

