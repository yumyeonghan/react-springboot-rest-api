package com.programmers.library.repository.reserve;

import com.programmers.library.domain.Reserve;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReserveRepository {
    List<Reserve> findAll();

    Reserve insert(Reserve reserve);

    Reserve update(Reserve reserve);

    Optional<Reserve> findById(UUID reserveId);

    void deleteById(UUID reserveId);
}
