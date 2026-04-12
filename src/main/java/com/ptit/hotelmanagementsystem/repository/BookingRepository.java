package com.ptit.hotelmanagementsystem.repository;

import com.ptit.hotelmanagementsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomId(Long roomId);
    List<Booking> findByHotelId(Long hotelId);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStatus(String status);
}
