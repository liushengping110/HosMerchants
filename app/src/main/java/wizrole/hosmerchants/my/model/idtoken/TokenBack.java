package wizrole.hosmerchants.my.model.idtoken;

/**
 * Created by liushengping on 2017/12/13/013.
 * 何人执笔？
 */

public class TokenBack {

    public String access_token;
    public String session_key;
    public String scope;
    public String refresh_token;
    public String session_secret;
    public String expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public String getSession_key() {
        return session_key;
    }

    public String getScope() {
        return scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public String getExpires_in() {
        return expires_in;
    }
}
