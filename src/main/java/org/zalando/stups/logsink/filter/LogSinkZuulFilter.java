package org.zalando.stups.logsink.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.zalando.stups.logsink.provider.TokenProvider;

import static java.util.Optional.empty;

public class LogSinkZuulFilter extends ZuulFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        final String accessToken = tokenProvider.getAccessToken();
        ctx.addZuulRequestHeader(AUTHORIZATION_HEADER, BEARER + " " + accessToken);
        return empty();
    }
}
