package org.tripleng.likesidehotel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.tripleng.likesidehotel.model.Room;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestFullName;
    private String guestEmail;
    private int numOfAdults;
    private int numOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;
    private Room room;

    public BookingResponse(Long id, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode){
        this.bookingId = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public BookingResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String guestFullName, String guestEmail, int numOfAdults, int numOfChildren, int totalNumOfGuest, String bookingConfirmationCode) {
        this.bookingId=bookingId;
        this.checkInDate=checkInDate;
        this.checkOutDate=checkOutDate;
        this.guestFullName=guestFullName;
        this.guestEmail=guestEmail;
        this.numOfAdults=numOfAdults;
        this.numOfChildren=numOfChildren;
        this.totalNumOfGuest=totalNumOfGuest;
        this.bookingConfirmationCode=bookingConfirmationCode;
    }
}
