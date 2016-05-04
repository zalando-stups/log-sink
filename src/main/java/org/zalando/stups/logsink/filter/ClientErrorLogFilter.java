package org.zalando.stups.logsink.filter;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ClientErrorLogFilter extends OncePerRequestFilter {

    private final Logger log = getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            logFailureResponses(response);
        }
    }

    private void logFailureResponses(final HttpServletResponse response) {
        final HttpStatus status = HttpStatus.valueOf(response.getStatus());
        if (status.is4xxClientError()) {
            log.warn("Response status {}: {}", status, status.getReasonPhrase());
        }
    }
}
