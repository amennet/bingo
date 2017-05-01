package com.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathUtil {
    private static final Logger log = LoggerFactory.getLogger(MathUtil.class);

    public static int parseInt(String intStr, int defaultValue) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            log.debug("ParseInt false, for malformed intStr:" + intStr);
            return defaultValue;
        }
    }

    /**
     * return positive int value of originValue
     *
     * @param originValue
     * @return positive int
     */
    public static int getPositive(int originValue) {
        return 0x7fffffff & originValue;
    }
}
