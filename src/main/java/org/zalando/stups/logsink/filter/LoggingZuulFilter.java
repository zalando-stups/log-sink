package org.zalando.stups.logsink.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;

import static java.util.Optional.empty;
import static org.slf4j.LoggerFactory.getLogger;

public class LoggingZuulFilter extends ZuulFilter {

    private final Logger log = getLogger(getClass());

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        log.info("Routing request to {}", ctx.getRouteHost());
        return empty();
    }
}
