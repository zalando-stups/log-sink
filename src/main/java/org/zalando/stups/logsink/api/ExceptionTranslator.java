package org.zalando.stups.logsink.api;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ControllerAdvice
public class ExceptionTranslator {

    private final Logger log = getLogger(getClass());

    @ExceptionHandler(HystrixRuntimeException.class)
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public void handleHystrixException(HystrixRuntimeException e) {
        log.error("Dependency unavailable", e);
    }
}
