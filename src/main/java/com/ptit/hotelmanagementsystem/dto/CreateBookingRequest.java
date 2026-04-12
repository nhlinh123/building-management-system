package com.ptit.hotelmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    private Long hotelId;
    private Long roomId;
    private Long userId;
    private Date checkInDate;
    private Date checkOutDate;
    private String status;
    private Double totalPrice;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
}
