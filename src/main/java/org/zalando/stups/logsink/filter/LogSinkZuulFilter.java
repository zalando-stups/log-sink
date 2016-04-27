package org.zalando.stups.logsink.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zalando.stups.logsink.provider.TokenProvider;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
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
        HttpServletRequest httpServletRequest = ctx.getRequest();
        final String accessToken = tokenProvider.getAccessToken(httpServletRequest.getRequestURI());
        ctx.addZuulRequestHeader(AUTHORIZATION_HEADER, BEARER + " " + accessToken);
        return null;
    }
}
