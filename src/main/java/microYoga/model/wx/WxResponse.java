package microYoga.model.wx;

public class WxResponse {
    private OauthToken oauthToken;
    private SNSUserInfo snsUserInfo;
    private WxError wxError;

    public OauthToken getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(OauthToken oauthToken) {
        this.oauthToken = oauthToken;
    }

    public SNSUserInfo getSnsUserInfo() {
        return snsUserInfo;
    }

    public void setSnsUserInfo(SNSUserInfo snsUserInfo) {
        this.snsUserInfo = snsUserInfo;
    }

    public WxError getWxError() {
        return wxError;
    }

    public void setWxError(WxError wxError) {
        this.wxError = wxError;
    }
}
