package com.programmers.library.repository.seat;

import com.programmers.library.domain.Category;
import com.programmers.library.domain.Seat;
import com.programmers.library.domain.SeatStatus;
import com.programmers.library.repository.sql.builder.SelectSqlBuilder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcSeatRepository implements SeatRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcSeatRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Seat> findAll() {
        String sql = new SelectSqlBuilder()
                .select("*")
                .from("seat")
                .build();
        return namedParameterJdbcTemplate.query(sql, getSeatRowMapper());
    }

    @Override
    public Seat insert(Seat seat) {
        return null;
    }

    @Override
    public Seat update(Seat seat) {
        return null;
    }

    @Override
    public Optional<Seat> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Seat> findAllByCategory(Category category) {
        return null;
    }

    private RowMapper<Seat> getSeatRowMapper() {
        return (rs, rowNum) -> {
            Long seatId = rs.getLong("seat_id");
            SeatStatus seatStatus = SeatStatus.valueOf(rs.getString("seat_status").toUpperCase());
            Category category = Category.valueOf(rs.getString("category").toUpperCase());
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("last_login_at").toLocalDateTime();
            return new Seat(createdAt, seatId, category, seatStatus, updatedAt);
        };
    }
}
