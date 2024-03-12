package org.tripleng.likesidehotel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tripleng.likesidehotel.model.Room;
import org.tripleng.likesidehotel.response.RoomResponse;
import org.tripleng.likesidehotel.service.RoomService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms/v1")
@CrossOrigin(origins = "https://hotel-project-client.onrender.com")
@RequiredArgsConstructor
public class RoomController{
    private final RoomService roomService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam(value = "photo",required = false)List<MultipartFile> photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice")BigDecimal roomPrice
            ) throws SQLException, IOException {
                RoomResponse  response = roomService.addNewRoom(photo,roomType,roomPrice);
                return ResponseEntity.ok(response);
    }

    @GetMapping("/room-types")
    public ResponseEntity<List<String>> getAllRoomTypes(){
        return ResponseEntity.ok(roomService.getAllRoomTypes());
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> deleteRoom(@PathVariable("roomId") Long roomId){
        Long id = roomService.deleteRoom(roomId);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId){
        RoomResponse response = roomService.getRoomById(roomId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(value = "photo",required = false)List<MultipartFile> photo,
            @RequestParam(value = "roomType",required = false) String roomType,
            @RequestParam(value = "roomPrice",required = false)BigDecimal roomPrice
            ){
        RoomResponse response = roomService.updateRoom(roomId,roomType,roomPrice,photo);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/available-rooms/{roomType}")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkOutDate,
            @PathVariable("roomType") String roomType
    ){
        List<RoomResponse> availableRooms = roomService.getAvailableRooms(checkInDate,checkOutDate,roomType);
        if(availableRooms.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(availableRooms);
        }
    }

}
