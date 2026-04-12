package com.ptit.hotelmanagementsystem.controller;

import com.ptit.hotelmanagementsystem.dto.CreateBookingRequest;
import com.ptit.hotelmanagementsystem.dto.BookingDto;
import com.ptit.hotelmanagementsystem.dto.UpdateBookingRequest;
import com.ptit.hotelmanagementsystem.dto.BaseResponseModel;
import com.ptit.hotelmanagementsystem.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking Management")
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BaseResponseModel<BookingDto>> createBooking(@RequestBody CreateBookingRequest request, Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        BookingDto createdBooking = bookingService.createBooking(request, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseModel.created(createdBooking, "Booking created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseModel<BookingDto>> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(bookingDto -> ResponseEntity.ok(BaseResponseModel.success(bookingDto)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponseModel.notFound("Booking not found")));
    }

    @GetMapping
    public ResponseEntity<BaseResponseModel<List<BookingDto>>> getAllBookings() {
        List<BookingDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(BaseResponseModel.success(bookings));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<BaseResponseModel<List<BookingDto>>> getBookingsByRoomId(@PathVariable Long roomId) {
        List<BookingDto> bookings = bookingService.getBookingsByRoomId(roomId);
        return ResponseEntity.ok(BaseResponseModel.success(bookings));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponseModel<List<BookingDto>>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingDto> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(BaseResponseModel.success(bookings));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<BaseResponseModel<List<BookingDto>>> getBookingsByStatus(@PathVariable String status) {
        List<BookingDto> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(BaseResponseModel.success(bookings));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseModel<BookingDto>> updateBooking(@PathVariable Long id, @RequestBody UpdateBookingRequest request) {
        return bookingService.updateBooking(id, request)
                .map(bookingDto -> ResponseEntity.ok(BaseResponseModel.success(bookingDto, "Booking updated successfully")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(BaseResponseModel.notFound("Booking not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseModel<String>> deleteBooking(@PathVariable Long id) {
        if (bookingService.deleteBooking(id)) {
            return ResponseEntity.ok(BaseResponseModel.success("Booking deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponseModel.notFound("Booking not found"));
    }
}
