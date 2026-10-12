package com.tietheknot.repository;

import com.tietheknot.entity.WeddingBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeddingBookingRepository extends JpaRepository<WeddingBooking, Long> {
}
