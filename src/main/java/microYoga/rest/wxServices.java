package microYoga.rest;

import microYoga.model.wx.ArticleItem;
import microYoga.model.wx.SNSUserInfo;
import microYoga.model.wx.WeChatContant;
import microYoga.model.wx.WeixinOauth2Token;
import microYoga.utils.WeChatUtil;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/wxServices")
public class wxServices {

    //region private
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @RequestMapping("/oauth")
    public void auth(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        // 用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 获取网页授权access_token
            WeixinOauth2Token weixinOauth2Token = WeChatUtil.getOauth2AccessToken(WeChatContant.APP_ID, WeChatContant.APP_SECRET, code);
            // 网页授权接口访问凭证
            String accessToken = weixinOauth2Token.getAccessToken();
            // 用户标识
            String openId = weixinOauth2Token.getOpenId();
            // 获取用户信息
            SNSUserInfo snsUserInfo = WeChatUtil.getSNSUserInfo(accessToken, openId);

            response.sendRedirect("/test.html?openId="+openId+"&nickname="+snsUserInfo.getNickName());

            // 设置要传递的参数
            //request.setAttribute("snsUserInfo", snsUserInfo);
            //request.setAttribute("state", state);
        }
        // 跳转到index.jsp
        //request.getRequestDispatcher("index.jsp").forward(request, response);
        response.sendRedirect("/rejectAuth.html");
    }

    //region Biz Logic
    /*
    invoke below link in wechat client explorer, then wechat server will callback generatePersonalityPage function;
    https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx573faabcfb33a8a0&redirect_uri=http://ea8156c2.ngrok.io/wxServices/generatePersonalityPage&response_type=code&scope=snsapi_base&state=activityId,register#wechat_redirect
    */
    @RequestMapping("/generatePersonalityPage")
    public void generatePersonalityPage(HttpServletResponse response,
                                        @RequestParam(value = "state") String state,
                                        @RequestParam(value = "code") String snsapi_base_code){

//        String activityId = state.split(",")[0];
//        String requestMode = state.split(",")[1];
//        System.out.println(activityId);
//        System.out.println(requestMode);
//
//        String openId = getWxOpenIdBySNSApi_Base(snsapi_base_code);
//        System.out.println(openId);

    }

    private String getWxOpenIdBySNSApi_Base(String snsapi_base_code){
        String requestUrl = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", WeChatContant.APP_ID, WeChatContant.APP_SECRET, snsapi_base_code);

        JSONObject jsonObject = WeChatUtil.httpsRequest(requestUrl, "GET", null);
        WeixinOauth2Token wat = new WeixinOauth2Token();
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                //log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }

        return wat.getOpenId();
    }

    /*
    https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx573faabcfb33a8a0&redirect_uri=http://ea8156c2.ngrok.io/wxServices/generatePersonalityPage&response_type=code&scope=snsapi_userinfo&state=activityId,register#wechat_redirect
    https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx573faabcfb33a8a0&secret=83f320c9694458ddf718451ae12f6b80&code=081Hezb50YWj0K1e12e50b5xb50Hezbt&grant_type=authorization_code
    https://api.weixin.qq.com/sns/userinfo?access_token=13_e4yg1899-hgY8M4AXKOPSF399jEaE6uKkM1dWfpWhF5kmubmibOAO6fkTYeFCHV2SPGSivPb2kxVmWHFeV_GuA&openid=oguMF1vk1hV17FHMYS4pTtOr6uQU&lang=zh_CN

    获取第二步的refresh_token后，请求以下链接获取access_token：
    https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
     */
    private String getSNSUserInfoBySNSApi_UserInfo(){
        return "";
    }
    //endregion
}
