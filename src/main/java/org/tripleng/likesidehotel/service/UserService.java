package org.tripleng.likesidehotel.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.tripleng.likesidehotel.model.User;
import org.tripleng.likesidehotel.response.BookingResponse;

import java.util.List;

public interface UserService {
    String registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);

    List<BookingResponse> getBookingsByEmail(String email);

    String confirmToken(String confirmationToken);
//    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
