package org.tripleng.likesidehotel.service;

import org.springframework.web.multipart.MultipartFile;
import org.tripleng.likesidehotel.model.Room;
import org.tripleng.likesidehotel.response.RoomResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    RoomResponse addNewRoom(List<MultipartFile> photo, String roomType, BigDecimal roomPrice) throws IOException, SQLException;

    List<String> getAllRoomTypes();

    List<RoomResponse> getAllRooms() throws SQLException;

    
    Long deleteRoom(Long roomId);

    RoomResponse getRoomById(Long roomId);


    RoomResponse updateRoom(Long roomId, String roomType, BigDecimal roomPrice, List<MultipartFile> photo);

    List<RoomResponse> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
