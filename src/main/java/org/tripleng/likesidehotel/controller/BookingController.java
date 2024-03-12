package org.tripleng.likesidehotel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tripleng.likesidehotel.exception.InvalidBookingRequestException;
import org.tripleng.likesidehotel.exception.ResourceNotFoundException;
import org.tripleng.likesidehotel.model.BookedRoom;
import org.tripleng.likesidehotel.model.Room;
import org.tripleng.likesidehotel.response.BookingResponse;
import org.tripleng.likesidehotel.response.RoomResponse;
import org.tripleng.likesidehotel.service.BookingService;
import org.tripleng.likesidehotel.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/booking/v1")
@CrossOrigin(origins = "https://hotel-project-client.onrender.com/")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final RoomService roomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try{
            BookedRoom bookedRoom = bookingService.findByConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(bookedRoom);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PostMapping("/room/{id}")
    public ResponseEntity<?> saveBookingRoom(@PathVariable Long id,@RequestBody BookedRoom bookedRoom){
        try{
            String confirmationCode = bookingService.saveBooking(id,bookedRoom);
            return ResponseEntity.ok("Room booked successfully, your confirmation code is: "+confirmationCode);
        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{bookingId}")
    public void cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedRoom bookedRoom) {
        RoomResponse savedRoom = roomService.getRoomById(bookedRoom.getRoom().getId());
        return new BookingResponse(bookedRoom.getBookingId(),
                bookedRoom.getCheckInDate(),
                bookedRoom.getCheckOutDate(),
                bookedRoom.getGuestFullName(),
                bookedRoom.getGuestEmail(),
                bookedRoom.getNumOfAdults(),
                bookedRoom.getNumOfChildren(),
                bookedRoom.getTotalNumOfGuest(),
                bookedRoom.getBookingConfirmationCode(),
                bookedRoom.getRoom()
        );
    }



}
