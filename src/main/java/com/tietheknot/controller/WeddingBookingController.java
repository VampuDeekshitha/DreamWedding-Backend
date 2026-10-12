
package com.tietheknot.controller;

import com.tietheknot.entity.WeddingBooking;
import com.tietheknot.repository.WeddingBookingRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173",
    "http://localhost:5174",
    "http://localhost:5175"
})
public class WeddingBookingController {

    private final WeddingBookingRepository bookingRepository;

    public WeddingBookingController(
            WeddingBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping
    public WeddingBooking createBooking(
            @RequestBody WeddingBooking booking) {
        if (booking == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid booking data: Request body is missing."
            );
        }
        if (booking.getWeddingType() == null || booking.getWeddingType().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Wedding type is required to confirm a booking."
            );
        }
        if (booking.getDestination() == null || booking.getDestination().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Destination is required to confirm a booking."
            );
        }
        if (booking.getVenue() == null || booking.getVenue().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Venue is required to confirm a booking."
            );
        }

        booking.setWeddingType(booking.getWeddingType().trim());
        booking.setDestination(booking.getDestination().trim());
        booking.setVenue(booking.getVenue().trim());
        booking.setBookingStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    @GetMapping
    public List<WeddingBooking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/count")
    public Map<String, Long> getBookingsCount() {
        return Map.of("count", bookingRepository.count());
    }

    @GetMapping("/{id}")
    public WeddingBooking getBookingById(
            @PathVariable("id") Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Wedding booking #" + id + " not found"
                ));
    }
}