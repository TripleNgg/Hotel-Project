package org.tripleng.likesidehotel.exception;

public class InvalidBookingRequestException extends RuntimeException {

    public InvalidBookingRequestException(String msg){
        super(msg);
    }
}
