package org.tripleng.likesidehotel.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.tripleng.likesidehotel.service.RoomService;
import org.tripleng.likesidehotel.service.S3Service;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class RoomServiceImplTest {

    private final RoomService roomService;
    private final S3Service s3Service;

    @Test
    public void testDeleteImage(){
        String filename = "039a4440-2b46-4ccb-b4aa-eeaeac1623a0.jpg";
        s3Service.deleteFile(filename);
    }

}