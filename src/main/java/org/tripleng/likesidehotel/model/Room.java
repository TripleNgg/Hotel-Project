package org.tripleng.likesidehotel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private Boolean isBooked = false;
    @OneToMany(mappedBy = "room",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BookedRoom> bookings = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "room_photo_urls", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_urls",columnDefinition = "TEXT")
    private List<String> photoUrls = new ArrayList<>();

    public void addBooking(BookedRoom room){
        if(room == null){
            this.bookings = new ArrayList<>();
        }else {
            this.bookings.add(room);
            room.setRoom(this);
            this.isBooked = true;
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            String bookingCode = RandomStringUtils.random(10, characters);
            room.setBookingConfirmationCode(bookingCode);
        }

    }
}
