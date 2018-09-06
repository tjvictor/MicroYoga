package microYoga.rest;

import microYoga.dao.WeChatDao;
import microYoga.model.wx.Activity;
import microYoga.model.wx.Activity_Participate;
import microYoga.model.wx.Activity_Register;
import microYoga.model.wx.ArticleItem;
import microYoga.model.wx.OauthToken;
import microYoga.model.wx.SNSUserInfo;
import microYoga.model.wx.WeChatContant;
import microYoga.model.wx.WxError;
import microYoga.model.wx.WxResponse;
import microYoga.utils.CommonUtils;
import microYoga.utils.WeChatUtil;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/wxServices")
public class wxServices {

    //region private
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WeChatDao weChatDaoImp;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "test";
    }

    @RequestMapping(value = "/wx", method = RequestMethod.GET)
    public String login(@RequestParam(value = "signature") String signature,
                        @RequestParam(value = "timestamp") String timestamp,
                        @RequestParam(value = "nonce") String nonce,
                        @RequestParam(value = "echostr") String echostr) {

        return WeChatUtil.checkSignature(signature, timestamp, nonce) ? echostr : null;
    }

    /**
     * 此处是处理微信服务器的消息转发的
     */
    @RequestMapping(value = "/wx", method = RequestMethod.POST)
    public String processMsg(HttpServletRequest request) {
        // xml格式的消息数据
        String respXml = null;
        // 默认返回的文本消息内容
        String respContent;
        try {
            // 调用parseXml方法解析请求消息
            Map<String, String> requestMap = WeChatUtil.parseXml(request);
            // 消息类型
            String msgType = (String) requestMap.get(WeChatContant.MsgType);
            String mes = null;
            // 文本消息
            if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_TEXT)) {
                mes = requestMap.get(WeChatContant.Content).toString();
                if (mes != null && mes.length() < 2) {
                    List<ArticleItem> items = new ArrayList<>();
                    ArticleItem item = new ArticleItem();
                    item.setTitle("照片墙");
                    item.setDescription("阿狸照片墙");
                    item.setPicUrl("http://changhaiwx.pagekite.me/photo-wall/a/iali11.jpg");
                    item.setUrl("http://changhaiwx.pagekite.me/page/photowall");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("哈哈");
                    item.setDescription("一张照片");
                    item.setPicUrl("http://changhaiwx.pagekite.me/images/me.jpg");
                    item.setUrl("http://changhaiwx.pagekite.me/page/index");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("小游戏2048");
                    item.setDescription("小游戏2048");
                    item.setPicUrl("http://changhaiwx.pagekite.me/images/2048.jpg");
                    item.setUrl("http://changhaiwx.pagekite.me/page/game2048");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("百度");
                    item.setDescription("百度一下");
                    item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
                    item.setUrl("http://www.baidu.com");
                    items.add(item);

                    respXml = WeChatUtil.sendArticleMsg(requestMap, items);
                } else if ("我的信息".equals(mes)) {
                    /*Map<String, String> userInfo = getUserInfo(requestMap.get(WeChatContant.FromUserName));
                    System.out.println(userInfo.toString());
                    String nickname = userInfo.get("nickname");
                    String city = userInfo.get("city");
                    String province = userInfo.get("province");
                    String country = userInfo.get("country");
                    String headimgurl = userInfo.get("headimgurl");
                    List<ArticleItem> items = new ArrayList<>();
                    ArticleItem item = new ArticleItem();
                    item.setTitle("你的信息");
                    item.setDescription("昵称:"+nickname+"  地址:"+country+" "+province+" "+city);
                    item.setPicUrl(headimgurl);
                    item.setUrl("http://www.baidu.com");
                    items.add(item);

                    respXml = WeChatUtil.sendArticleMsg(requestMap, items);*/
                }
            }
            // 图片消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "您发送的是图片消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 语音消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是语音消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 视频消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VIDEO)) {
                respContent = "您发送的是视频消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 地理位置消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 链接消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 事件推送
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = (String) requestMap.get(WeChatContant.Event);
                // 关注
                if (eventType.equals(WeChatContant.EVENT_TYPE_SUBSCRIBE)) {
                    respContent = "谢谢您的关注！";
                    respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
                }
                // 取消关注
                else if (eventType.equals(WeChatContant.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
                }
                // 扫描带参数二维码
                else if (eventType.equals(WeChatContant.EVENT_TYPE_SCAN)) {
                    // TODO 处理扫描带参数二维码事件
                }
                // 上报地理位置
                else if (eventType.equals(WeChatContant.EVENT_TYPE_LOCATION)) {
                    // TODO 处理上报地理位置事件
                }
                // 自定义菜单
                else if (eventType.equals(WeChatContant.EVENT_TYPE_CLICK)) {
                    // TODO 处理菜单点击事件
                }
            }
            mes = mes == null ? "不知道你在干嘛" : mes;
            if (respXml == null)
                respXml = WeChatUtil.sendTextMsg(requestMap, mes);
            System.out.println(respXml);
            return respXml;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    @RequestMapping(value = "/jump", method = RequestMethod.GET)
    public void jump(HttpServletResponse response) throws IOException {
        response.sendRedirect("/test.html");
    }

    //region Biz Logic
    /*
    invoke below link in wechat client explorer, then wechat server will callback generatePersonalityPage function;
    https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx573faabcfb33a8a0&redirect_uri=http://ea8156c2.ngrok.io/wxServices/generatePersonalityPage&response_type=code&scope=snsapi_base&state=activityId,register#wechat_redirect
    */
    @RequestMapping("/generateRegisterPage")
    public void generateRegisterPage(HttpServletResponse response,
                                        @RequestParam(value = "state") String state,
                                        @RequestParam(value = "code") String snsapi_base_code) throws IOException, SQLException {

        String activityId = state.split(",")[0];
        String requestMode = state.split(",")[1];
        Activity activity = weChatDaoImp.getActivityById(activityId);
        WxResponse wxResponse = getWxOpenIdBySNSApi_Base(response, snsapi_base_code);
        if(wxResponse.getOauthToken() == null){
            response.sendRedirect("/activity/error.html?errcode="+wxResponse.getWxError().getErrorCode()+"&errmsg="+wxResponse.getWxError().getErrorMessage());
            return;
        }

        Activity_Register activityRegister = weChatDaoImp.getActivityRegisterByActivityIdAndRegisterId(activityId, wxResponse.getOauthToken().getOpenId());
        if(activityRegister != null){
            String participateUrl = String.format("%s?activityRegisterId=%s&nickName=%s",
                    activity.getRegisterPage(), activityRegister.getId(), activityRegister.getRegisterName());
            response.sendRedirect(participateUrl);
            return;
        }

        OauthToken token = weChatDaoImp.getOauthTokenByOpenId(wxResponse.getOauthToken().getOpenId());
        if(StringUtils.isEmpty(token.getAccessToken())){
            requestSNSApi_UserInfo_Code(response, state);
            return;
        }else{
            wxResponse = getSNSUserInfoByAccessToken(token.getAccessToken(), token.getOpenId());
            if(wxResponse.getSnsUserInfo() == null){
                //refresh access token
                wxResponse = refreshOauthToken(token);
                if(wxResponse.getOauthToken() == null){
                    //refresh failed, re launch user info authentication
                    requestSNSApi_UserInfo_Code(response, state);
                    return;
                }
                //refresh successfully, re get user info
                wxResponse = getSNSUserInfoByAccessToken(token.getAccessToken(), token.getOpenId());
                if(wxResponse.getSnsUserInfo() == null){
                    response.sendRedirect("/activity/error.html?errcode="+wxResponse.getWxError().getErrorCode()+"&errmsg="+wxResponse.getWxError().getErrorMessage());
                    return;
                }
            }


            Activity_Register item = fillActivity_Register(UUID.randomUUID().toString(),
                    activityId, wxResponse.getSnsUserInfo().getOpenId(),
                    wxResponse.getSnsUserInfo().getNickName(), CommonUtils.getCurrentDateTime());
            weChatDaoImp.insertActivityRegister(item);
            String participateUrl = String.format("%s?activityId=%s&nickName=%s",
                    activity.getParticipatePage(), activity.getId(), item.getRegisterName());
            response.sendRedirect(participateUrl);
            return;
        }
    }

    @RequestMapping("/generateParticipatePage")
    public void generateParticipatePage(HttpServletResponse response,
                                        @RequestParam(value = "state") String state,
                                        @RequestParam(value = "code") String snsapi_base_code) throws IOException, SQLException {
        String activityRegisterId = state.split(",")[0];
        String requestMode = state.split(",")[1];
        Activity_Register activityRegister = weChatDaoImp.getActivityRegisterById(activityRegisterId);
        Activity activity = weChatDaoImp.getActivityById(activityRegister.getActivityId());
        WxResponse wxResponse = getWxOpenIdBySNSApi_Base(response, snsapi_base_code);
        if(wxResponse.getOauthToken() == null){
            response.sendRedirect("/activity/error.html?errcode="+wxResponse.getWxError().getErrorCode()+"&errmsg="+wxResponse.getWxError().getErrorMessage());
            return;
        }

        OauthToken token = weChatDaoImp.getOauthTokenByOpenId(wxResponse.getOauthToken().getOpenId());
        if(StringUtils.isEmpty(token.getAccessToken())){
            requestSNSApi_UserInfo_Code(response, state);
            return;
        }else{
            wxResponse = getSNSUserInfoByAccessToken(token.getAccessToken(), token.getOpenId());
            if(wxResponse.getSnsUserInfo() == null){
                //refresh access token
                wxResponse = refreshOauthToken(token);
                if(wxResponse.getOauthToken() == null){
                    //refresh failed, re launch user info authentication
                    requestSNSApi_UserInfo_Code(response, state);
                    return;
                }
                //refresh successfully, re get user info
                wxResponse = getSNSUserInfoByAccessToken(token.getAccessToken(), token.getOpenId());
                if(wxResponse.getSnsUserInfo() == null){
                    response.sendRedirect("/activity/error.html?errcode="+wxResponse.getWxError().getErrorCode()+"&errmsg="+wxResponse.getWxError().getErrorMessage());
                    return;
                }
            }

            Activity_Participate item = fillActivity_Participate(UUID.randomUUID().toString(),
                    activityRegisterId, wxResponse.getSnsUserInfo().getOpenId(),
                    wxResponse.getSnsUserInfo().getNickName(), CommonUtils.getCurrentDateTime());
            weChatDaoImp.insertActivityParticipate(item);
            String participateUrl = String.format("%s?activityRegisterId=%s&nickName=%s",
                    activity.getRegisterPage(), item.getId(), item.getParticipateName());
            response.sendRedirect(participateUrl);
            return;
        }
    }

    private WxResponse getWxOpenIdBySNSApi_Base(HttpServletResponse response, String snsapi_base_code) throws IOException {
        WxResponse wxResponse = new WxResponse();
        String requestUrl = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", WeChatContant.APP_ID, WeChatContant.APP_SECRET, snsapi_base_code);
        JSONObject jsonObject = WeChatUtil.httpsRequest(requestUrl, "GET", null);
        String openId = "";
        if (null != jsonObject) {
            try {
                wxResponse.setOauthToken(fillOauthTokenEntity(jsonObject));
            }catch (Exception e){
                wxResponse.setWxError(fillWxErrorEntity(jsonObject));
            }
        }

        return wxResponse;
    }

    private void requestSNSApi_UserInfo_Code(HttpServletResponse response, String state) throws IOException {
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s/wxServices/getSNSUserInfoBySNSApi_UserInfo&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect", WeChatContant.APP_ID, WeChatContant.REQ_DOMAIN ,state);

        response.sendRedirect(url);
    }

    private WxResponse refreshOauthToken(OauthToken item){
        WxResponse wxResponse = new WxResponse();
        String requestUrl = String.format("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s", WeChatContant.APP_ID, item.getRefreshToken());
        JSONObject jsonObject = WeChatUtil.httpsRequest(requestUrl, "GET", null);
        if(jsonObject != null){
            try {
                OauthToken token = fillOauthTokenEntity(jsonObject);
                weChatDaoImp.replaceOauthToken(token);
                wxResponse.setOauthToken(token);
            }catch (Exception e){
                wxResponse.setWxError(fillWxErrorEntity(jsonObject));
            }
        }
        return wxResponse;
    }
    /*
    https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx573faabcfb33a8a0&redirect_uri=http://ea8156c2.ngrok.io/wxServices/generatePersonalityPage&response_type=code&scope=snsapi_userinfo&state=activityId,register#wechat_redirect
    https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx573faabcfb33a8a0&secret=83f320c9694458ddf718451ae12f6b80&code=081Hezb50YWj0K1e12e50b5xb50Hezbt&grant_type=authorization_code
    https://api.weixin.qq.com/sns/userinfo?access_token=13_e4yg1899-hgY8M4AXKOPSF399jEaE6uKkM1dWfpWhF5kmubmibOAO6fkTYeFCHV2SPGSivPb2kxVmWHFeV_GuA&openid=oguMF1vk1hV17FHMYS4pTtOr6uQU&lang=zh_CN

    获取第二步的refresh_token后，请求以下链接获取access_token：
    https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
     */
    @RequestMapping("/getSNSUserInfoBySNSApi_UserInfo")
    public void getSNSUserInfoBySNSApi_UserInfo(HttpServletResponse response,
                                                @RequestParam(value = "state") String state,
                                                @RequestParam(value = "code") String snsapi_userinfo_code) throws SQLException, IOException {
        String id = state.split(",")[0];
        String requestMode = state.split(",")[1];
        Activity activity = new Activity();
        Activity_Register activityRegister = new Activity_Register();
        if(requestMode.equals(WeChatContant.REQ_BIZ_REGISTER)) {
            activity = weChatDaoImp.getActivityById(activityRegister.getActivityId());
        }else if(requestMode.equals(WeChatContant.REQ_BIZ_PARTICIPATE)){
            activityRegister = weChatDaoImp.getActivityRegisterById(id);
            activity = weChatDaoImp.getActivityById(activityRegister.getActivityId());
        }else{
            response.sendRedirect("/activity/error.html?errcode=-1&errmsg=unknown request mode");
            return ;
        }

        String requestUrl = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                WeChatContant.APP_ID, WeChatContant.APP_SECRET, snsapi_userinfo_code);
        JSONObject jsonObject = WeChatUtil.httpsRequest(requestUrl, "GET", null);
        if(jsonObject != null){
            try {
                OauthToken token = fillOauthTokenEntity(jsonObject);
                weChatDaoImp.replaceOauthToken(token);

                WxResponse wxResponse = getSNSUserInfoByAccessToken(token.getAccessToken(), token.getOpenId());
                if(wxResponse.getSnsUserInfo() != null){
                    if(requestMode.equals(WeChatContant.REQ_BIZ_REGISTER)) {
                        Activity_Register item = fillActivity_Register(UUID.randomUUID().toString(),
                                activity.getId(), wxResponse.getSnsUserInfo().getOpenId(),
                                wxResponse.getSnsUserInfo().getNickName(), CommonUtils.getCurrentDateTime());
                        weChatDaoImp.insertActivityRegister(item);
                        String participateUrl = String.format("%s?activityRegisterId=%s&nickName=%s",
                                activity.getRegisterPage(), item.getId(), item.getRegisterName());
                        response.sendRedirect(participateUrl);
                        return;
                    }
                    else if(requestMode.equals(WeChatContant.REQ_BIZ_PARTICIPATE)){
                        Activity_Participate item = fillActivity_Participate(UUID.randomUUID().toString(),
                                activityRegister.getId(), wxResponse.getSnsUserInfo().getOpenId(),
                                wxResponse.getSnsUserInfo().getNickName(), CommonUtils.getCurrentDateTime());
                        weChatDaoImp.insertActivityParticipate(item);
                        String participateUrl = String.format("%s?activityId=%s&nickName=%s",
                                activity.getParticipatePage(), activity.getId(), item.getParticipateName());
                        response.sendRedirect(participateUrl);
                        return;
                    }
                }else{
                    response.sendRedirect("/activity/error.html?errcode="+wxResponse.getWxError().getErrorCode()+"&errmsg="+wxResponse.getWxError().getErrorMessage());
                }
            }catch (Exception e){
                WxError wxError = fillWxErrorEntity(jsonObject);
                response.sendRedirect("/activity/error.html?errcode="+wxError.getErrorCode()+"&errmsg="+wxError.getErrorMessage());
            }
        }
    }

    private WxResponse getSNSUserInfoByAccessToken(String accessToken, String openId){
        WxResponse wxResponse = new WxResponse();
        String requestUrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", accessToken, openId);
        JSONObject jsonObject = WeChatUtil.httpsRequest(requestUrl, "GET", null);
        if(jsonObject != null){
            try {
                SNSUserInfo item = fillSNSUserInfoEntity(jsonObject);
                weChatDaoImp.replaceSNSUserInfo(item);
                wxResponse.setSnsUserInfo(item);
            }catch (Exception e){
                wxResponse.setWxError(fillWxErrorEntity(jsonObject));
            }
        }

        return wxResponse;
    }

    private WxError fillWxErrorEntity(JSONObject jsonObject){
        String errorCode = jsonObject.getString("errcode");
        String errorMsg = jsonObject.getString("errmsg");
        return new WxError(errorCode, errorMsg);
    }

    private OauthToken fillOauthTokenEntity(JSONObject jsonObject){
        OauthToken token = new OauthToken();
        token.setAccessToken(jsonObject.getString("access_token"));
        token.setExpiresIn(jsonObject.getString("expires_in"));
        token.setRefreshToken(jsonObject.getString("refresh_token"));
        token.setOpenId(jsonObject.getString("openid"));
        token.setScope(jsonObject.getString("scope"));
        return token;
    }

    private SNSUserInfo fillSNSUserInfoEntity(JSONObject jsonObject){
        SNSUserInfo item = new SNSUserInfo();
        item.setOpenId(jsonObject.getString("openid"));
        item.setNickName(jsonObject.getString("nickname"));
        item.setSex(jsonObject.getString("sex"));
        item.setProvince(jsonObject.getString("province"));
        item.setCity(jsonObject.getString("city"));
        item.setCountry(jsonObject.getString("country"));
        item.setHeadImgUrl(jsonObject.getString("headimgurl"));
        item.setPrivilegeList(jsonObject.getJSONArray("privilege"));
        return item;
    }

    private Activity_Register fillActivity_Register(String id, String activityId,
                                                    String registerId,
                                                    String registerName, String date){
        Activity_Register item = new Activity_Register();
        item.setId(id);
        item.setActivityId(activityId);
        item.setRegisterId(registerId);
        item.setRegisterName(registerName);
        item.setDate(date);
        return item;
    }

    private  Activity_Participate fillActivity_Participate(String id, String activityRegisterId,
                                                           String participateId,
                                                           String participateName, String date){
        Activity_Participate item = new Activity_Participate();
        item.setId(id);
        item.setActivityRegisterId(activityRegisterId);
        item.setParticipateId(participateId);
        item.setParticipateName(participateName);
        item.setDate(date);
        return item;
    }
    //endregion
}
