package org.tripleng.likesidehotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tripleng.likesidehotel.model.BookedRoom;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookedRoom,Long> {

    List<BookedRoom> findByRoomId(Long id);

    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findByGuestEmail(String email);
}
