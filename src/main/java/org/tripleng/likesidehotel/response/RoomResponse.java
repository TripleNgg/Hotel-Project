package org.tripleng.likesidehotel.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.tripleng.likesidehotel.model.BookedRoom;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private Boolean isBooked = false;
    private List<BookedRoom> bookings = new ArrayList<>();
    private List<String> photoUrls;

    public RoomResponse(Long id,String roomType,BigDecimal roomPrice){
        this.id = id;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
    }

    public RoomResponse(Long id,String roomType,BigDecimal roomPrice,List<BookedRoom> bookings, List<String> photoUrls){
        this.id = id;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
        this.photoUrls = photoUrls;
        this.bookings = bookings;
    }

    public RoomResponse(Long roomId, String roomType, BigDecimal roomPrice, List<String> photoUrls) {
        this.id=roomId;
        this.roomType=roomType;
        this.roomPrice=roomPrice;
        this.photoUrls=photoUrls;
    }
}
