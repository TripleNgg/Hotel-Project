package org.tripleng.likesidehotel.service.serviceImpl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tripleng.likesidehotel.exception.PhotoRetrievalException;
import org.tripleng.likesidehotel.exception.ResourceNotFoundException;
import org.tripleng.likesidehotel.model.BookedRoom;
import org.tripleng.likesidehotel.model.Room;
import org.tripleng.likesidehotel.repository.RoomRepository;
import org.tripleng.likesidehotel.response.BookingResponse;
import org.tripleng.likesidehotel.response.RoomResponse;
import org.tripleng.likesidehotel.service.BookingService;
import org.tripleng.likesidehotel.service.RoomService;
import org.tripleng.likesidehotel.service.S3Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository  roomRepository;
    private final BookingService bookingService;
    private final S3Service s3Service;
    private final DbxClientV2 clientV2;
    @Override
    @Transactional
    public RoomResponse addNewRoom(List<MultipartFile> photoUrls, String roomType, BigDecimal roomPrice) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if(photoUrls==null||photoUrls.isEmpty()){
            room.setPhotoUrls(null);
        }else{
            List<String> photoUrlsString = new ArrayList<>();
            for (MultipartFile photoUrl : photoUrls) {
                try (InputStream in = photoUrl.getInputStream()) {
                    FileMetadata metadata = clientV2.files().uploadBuilder("/" + photoUrl.getOriginalFilename())
                            .uploadAndFinish(in);

                    // Get the link to the uploaded file
                    String fileLink =  metadata.getPathLower();
                    SharedLinkMetadata sharedLinkMetadata = clientV2.sharing().createSharedLinkWithSettings(fileLink);
                    photoUrlsString.add(sharedLinkMetadata.getUrl());
                } catch (IOException | DbxException e) {
                    // Handle exception
                    e.printStackTrace();
                }
            }
            room.setPhotoUrls(photoUrlsString);
        }
        Room roomNeedSave = roomRepository.save(room);
        return new RoomResponse(roomNeedSave.getId(),roomNeedSave.getRoomType(),roomNeedSave.getRoomPrice());
    }

    @Override
    public List<String> getAllRoomTypes() {
        List<String> roomTypes =  roomRepository.findDistinctRoomTypes();
        roomTypes.removeIf(String::isEmpty);
        return roomTypes;
    }

    @Override
//    @Transactional
    public List<RoomResponse> getAllRooms() throws SQLException {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : rooms){
            List<String> photoUrls = getRoomPhotoByRoomId(room.getId());
            if(photoUrls!=null){
                RoomResponse response = getRoomResponse(room);
                if (response!=null){
                    response.setPhotoUrls(photoUrls);
                    roomResponses.add(response);
                }
            }
        }
        return roomResponses;
    }

    @Override
    public Long deleteRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        List<String> photoUrls = room.get().getPhotoUrls();
        if(!photoUrls.isEmpty()){
            for (String url:photoUrls){
                try {
                    // Make a call to delete the file
                    clientV2.files().deleteV2(url);
                } catch (DeleteErrorException e) {
                    // Handle the case where the file is not found
                    if (e.errorValue.isPathLookup() && e.errorValue.getPathLookupValue().isNotFound()) {
                        System.out.println("File not found: " + url);
                    } else {
                        // Handle other delete errors
                        e.printStackTrace();
                    }

                } catch (DbxException e) {
                    // Handle DbxException
                    e.printStackTrace();
                }
            }
        }
        Long id = room.get().getId();
        room.ifPresent(roomRepository::delete);
        return id;
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()){
            Room getRoom = room.get();
            return new RoomResponse(getRoom.getId(), getRoom.getRoomType(),getRoom.getRoomPrice(),getRoom.getBookings(),getRoom.getPhotoUrls());
        }
        return null;
    }

    @Override
    public RoomResponse updateRoom(Long roomId, String roomType, BigDecimal roomPrice, List<MultipartFile> photos) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        List<String> photoUrls = null;
        if (optionalRoom.isPresent()) {
            Room savedRoom = optionalRoom.get();
            savedRoom.setRoomPrice(roomPrice);
            savedRoom.setRoomType(roomType);
            deletePhotoRoom(savedRoom);
            photoUrls = new ArrayList<>();
            if(photos!=null){
                for (MultipartFile photo : photos) {
                    photoUrls.add(s3Service.uploadFile(photo));
                }
            }
            savedRoom.setPhotoUrls(photoUrls);
            roomRepository.save(savedRoom);
        }
        return new RoomResponse(roomId, roomType, roomPrice, photoUrls);
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        List<Room> availableRooms = roomRepository.findAvailableRoomsByDateAndType(checkInDate,checkOutDate,roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room:availableRooms){
            roomResponses.add(getRoomResponse(room));
        }
        return roomResponses;
    }


    private void deletePhotoRoom(Room room){
        List<String> photoUrls = room.getPhotoUrls();
        for(String photo:photoUrls){
            s3Service.deleteFileByUrl(photo);
        }
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookedRooms = bookingService.getAllBookingsByRoomId(room.getId());
        if(bookedRooms == null){
            return null;
        }
        List<BookingResponse> bookingResponses = bookedRooms
                .stream()
                .map(r ->
                        new BookingResponse(
                                r.getBookingId(),
                                r.getCheckInDate(),
                                r.getCheckOutDate(),
                                r.getBookingConfirmationCode())).toList();
        List<String> photoUrls = room.getPhotoUrls();
        return new RoomResponse(
                room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                bookedRooms,
                photoUrls
        );
    }

    private List<String> getRoomPhotoByRoomId(Long id) throws SQLException {
        // Retrieve the room by its ID
        Optional<Room> room = roomRepository.findById(id);

        // Check if the room with the given ID exists
        if (room.isEmpty()) {
            // If not found, throw a ResourceNotFoundException
            throw new ResourceNotFoundException("Sorry, room not found");
        }

        return room.get().getPhotoUrls();
    }

}
