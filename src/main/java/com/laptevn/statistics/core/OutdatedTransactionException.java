package com.laptevn.statistics.core;

public class OutdatedTransactionException extends Exception {
    private static final long serialVersionUID = 3848559160595534143L;

    public OutdatedTransactionException(String message) {
        super(message);
    }
}