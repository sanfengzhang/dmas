package com.hanl.dams.common;

/**
 * @author: Hanl
 * @date :2020/3/23
 * @desc:
 */
public class BusException extends Exception {

    public BusException(String message) {

        super(message, null);
    }

    public BusException(String message, Throwable cause) {

        super(message, cause);
    }
}