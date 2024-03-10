package org.tripleng.likesidehotel.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tripleng.likesidehotel.exception.InvalidBookingRequestException;
import org.tripleng.likesidehotel.exception.ResourceNotFoundException;
import org.tripleng.likesidehotel.model.BookedRoom;
import org.tripleng.likesidehotel.model.Room;
import org.tripleng.likesidehotel.repository.BookingRepository;
import org.tripleng.likesidehotel.repository.RoomRepository;
import org.tripleng.likesidehotel.response.BookingResponse;
import org.tripleng.likesidehotel.service.BookingService;
import org.tripleng.likesidehotel.service.RoomService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRoomRepository;
//    private final RoomService roomService;
    private final RoomRepository roomRepository;
    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long id) {
        return bookingRoomRepository.findByRoomId(id);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        List<BookedRoom> bookedRooms = bookingRoomRepository.findAll();
        List<BookingResponse> responses = new ArrayList<>();
        for (BookedRoom room : bookedRooms) {
            responses.add(getBookingResponse(room));
        }
        return responses;
    }
    @Override
    public String saveBooking(Long id, BookedRoom bookedRoom) {
        if(bookedRoom.getCheckOutDate().isBefore(bookedRoom.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookedRoom,existingBookings);
        if(roomIsAvailable){
            room.addBooking(bookedRoom);
            bookingRoomRepository.save(bookedRoom);
        }else{
            throw new InvalidBookingRequestException("Sorry, this room has been booked for the selected date.");
        }
        return bookedRoom.getBookingConfirmationCode();
    }

 

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRoomRepository.deleteById(bookingId);
    }

    @Override
    public BookedRoom findByConfirmationCode(String confirmationCode) {
        return bookingRoomRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(()->new ResourceNotFoundException("No booking found with booking code: "+confirmationCode));
    }

    @Override
    public List<BookedRoom> getBookingsByEmail(String email) {
        return bookingRoomRepository.findByGuestEmail(email);
    }

    public BookingResponse getBookingResponse(BookedRoom room) {
        return new BookingResponse(
                room.getBookingId(),
                room.getCheckInDate(),
                room.getCheckOutDate(),
                room.getGuestFullName(),
                room.getGuestEmail(),
                room.getNumOfAdults(),
                room.getNumOfChildren(),
                room.getTotalNumOfGuest(),
                room.getBookingConfirmationCode(),
                room.getRoom()
        );
    }
    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
