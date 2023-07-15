package com.programmers.library.repository.seat;

import com.programmers.library.domain.Category;
import com.programmers.library.domain.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findAll();

    Seat insert(Seat seat);

    Seat update(Seat seat);

    Optional<Seat> findById(Long seatId);

    void deleteById(Long seatId);

    List<Seat> findAllByCategory(Category category);
}
