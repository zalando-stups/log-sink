package org.zalando.stups.logsink.provider;

import org.zalando.stups.tokens.AccessTokensBean;

public class TokenProvider {

    public static final String LOGSINK = "logsink";

    private AccessTokensBean accessTokensBean;

    public TokenProvider(final AccessTokensBean accessTokensBean) {
        this.accessTokensBean = accessTokensBean;
    }

    public String getAccessToken() {
        return accessTokensBean.get(LOGSINK);
    }
}
