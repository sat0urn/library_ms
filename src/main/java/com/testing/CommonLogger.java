package com.testing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonLogger {
    private static final Logger logger = LogManager.getLogger(CommonLogger.class);

    public static Logger getLogger() {
        return logger;
    }
}
