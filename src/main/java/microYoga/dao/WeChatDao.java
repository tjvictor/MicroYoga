package microYoga.dao;

import microYoga.model.wx.Activity;
import microYoga.model.wx.Activity_Participate;
import microYoga.model.wx.Activity_Register;
import microYoga.model.wx.SNSUserInfo;
import microYoga.model.wx.OauthToken;

import java.sql.SQLException;
import java.util.List;

public interface WeChatDao {

    void insertSNSUserInfo(SNSUserInfo item) throws SQLException;

    void insertActivityRegister(Activity_Register item) throws SQLException;

    void insertActivityParticipate(Activity_Participate item) throws SQLException;

    Activity getActivityById(String id) throws SQLException;

    List<Activity_Register> getActivityRegistersByActivityId(String activityId) throws SQLException;

    Activity_Register getActivityRegisterByActivityIdAndRegisterId(String activityId, String registerId) throws SQLException;

    Activity_Register getActivityRegisterById(String id) throws SQLException;

    List<Activity_Participate> getActivityParticipatesByActivityRegisterId(String activityRegisterId) throws SQLException;

    boolean isRegisterExist(String activityId, String registerId) throws SQLException;

    boolean isParticipateExist(String activityRegisterId, String participateId) throws SQLException;

    OauthToken getOauthTokenByOpenId(String openId) throws SQLException;

    void replaceOauthToken(OauthToken item) throws SQLException;

    void replaceSNSUserInfo(SNSUserInfo item) throws SQLException;
}

