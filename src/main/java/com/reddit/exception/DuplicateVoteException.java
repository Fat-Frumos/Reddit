package com.reddit.exception;

public class DuplicateVoteException
        extends RuntimeException {
    public DuplicateVoteException(String message) {
        super(message);
    }
}
