package com.epam.esm.reddit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String message) {
        super(message);
        log.error(message);
    }

    public SpringRedditException(String message, Exception e) {
        super(message);
        log.error(message, e);
    }
}
