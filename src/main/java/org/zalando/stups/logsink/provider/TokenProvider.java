package org.zalando.stups.logsink.provider;

import org.zalando.stups.tokens.AccessTokensBean;

public class TokenProvider {

    public static final String LOGSINK = "logsink";

    private AccessTokensBean accessTokensBean;

    public String getAccessToken(final String uri) {
        return accessTokensBean.get(LOGSINK);
    }

    public TokenProvider(AccessTokensBean accessTokensBean) {
        this.accessTokensBean = accessTokensBean;
    }
}
