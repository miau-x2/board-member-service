package com.example.board.member.commons.utils;

import org.hibernate.exception.ConstraintViolationException;

public final class ExceptionsUtils {
    private ExceptionsUtils() {}

    public static String findConstraintName(Throwable throwable) {
        for(var cur = throwable; cur != null; cur = cur.getCause()) {
            if(cur instanceof ConstraintViolationException cve) {
                return cve.getConstraintName();
            }
        }
        return null;
    }
}
