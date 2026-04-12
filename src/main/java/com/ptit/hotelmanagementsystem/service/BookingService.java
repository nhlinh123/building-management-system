package com.ptit.hotelmanagementsystem.service;

import com.ptit.hotelmanagementsystem.dto.BookingDto;
import com.ptit.hotelmanagementsystem.dto.CreateBookingRequest;
import com.ptit.hotelmanagementsystem.dto.UpdateBookingRequest;
import com.ptit.hotelmanagementsystem.model.Booking;
import com.ptit.hotelmanagementsystem.model.User;
import com.ptit.hotelmanagementsystem.repository.BookingRepository;
import com.ptit.hotelmanagementsystem.repository.UserRepository;
import com.ptit.hotelmanagementsystem.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public BookingDto createBooking(CreateBookingRequest request, String username) {
        // If userId not provided in request, try to infer from authenticated username
        Long userId = request.getUserId();
        if (userId == null && username != null) {
            userId = userRepository.findByUsername(username).map(User::getId).orElse(null);
        }

        Booking booking = Booking.builder()
                .hotelId(request.getHotelId())
                .roomId(request.getRoomId())
                .userId(userId)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .status(request.getStatus() != null ? request.getStatus() : "PENDING")
                .totalPrice(request.getTotalPrice() != null ? request.getTotalPrice() : 0.0)
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .createdAt(new java.util.Date())
                .updatedAt(new java.util.Date())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        // If booking status is PENDING (or other statuses as required), mark the room as unavailable
        if (savedBooking.getRoomId() != null && "PENDING".equalsIgnoreCase(savedBooking.getStatus())) {
            // Lazy update: only change if room is currently available
            try {
                roomRepository.findById(savedBooking.getRoomId()).ifPresent(room -> {
                    if (room.isAvailable()) {
                        room.setAvailable(false);
                        roomRepository.save(room);
                    }
                });
            } catch (Exception e) {
                // Log and continue; booking was created successfully
                System.out.println("Failed to update room availability: " + e.getMessage());
            }
        }

        return mapToDto(savedBooking);
    }

    public Optional<BookingDto> getBookingById(Long id) {
        return bookingRepository.findById(id).map(this::mapToDto);
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<BookingDto> updateBooking(Long id, UpdateBookingRequest request) {
        return bookingRepository.findById(id).map(booking -> {
            if (request.getHotelId() != null) booking.setHotelId(request.getHotelId());
            if (request.getRoomId() != null) booking.setRoomId(request.getRoomId());
            if (request.getUserId() != null) booking.setUserId(request.getUserId());
            if (request.getCheckInDate() != null) booking.setCheckInDate(request.getCheckInDate());
            if (request.getCheckOutDate() != null) booking.setCheckOutDate(request.getCheckOutDate());
            if (request.getStatus() != null) booking.setStatus(request.getStatus());
            if (request.getTotalPrice() != null) booking.setTotalPrice(request.getTotalPrice());
            if (request.getGuestName() != null) booking.setGuestName(request.getGuestName());
            if (request.getGuestEmail() != null) booking.setGuestEmail(request.getGuestEmail());
            if (request.getGuestPhone() != null) booking.setGuestPhone(request.getGuestPhone());
            booking.setUpdatedAt(new java.util.Date());
            
            Booking updatedBooking = bookingRepository.save(booking);
            return mapToDto(updatedBooking);
        });
    }

    public boolean deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .hotelId(booking.getHotelId())
                .roomId(booking.getRoomId())
                .userId(booking.getUserId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .guestName(booking.getGuestName())
                .guestEmail(booking.getGuestEmail())
                .guestPhone(booking.getGuestPhone())
                .build();
    }
}
