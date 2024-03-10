package org.tripleng.likesidehotel.service;

import org.tripleng.likesidehotel.model.BookedRoom;
import org.tripleng.likesidehotel.response.BookingResponse;
import org.tripleng.likesidehotel.response.RoomResponse;

import java.util.List;

public interface BookingService {
    List<BookedRoom> getAllBookingsByRoomId(Long id);

    List<BookingResponse> getAllBookings();

    String saveBooking(Long id, BookedRoom bookedRoom);

    void cancelBooking(Long bookingId);

    BookedRoom findByConfirmationCode(String confirmationCode);

    List<BookedRoom> getBookingsByEmail(String email);
}
