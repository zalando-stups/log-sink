package org.zalando.stups.logsink.provider;

import lombok.AllArgsConstructor;
import org.zalando.stups.tokens.AccessTokensBean;

@AllArgsConstructor
public class TokenProvider {

    public static final String LOGSINK = "logsink";

    private AccessTokensBean accessTokensBean;

    public String getAccessToken(final String uri) {
        return accessTokensBean.get(LOGSINK);
    }


}
