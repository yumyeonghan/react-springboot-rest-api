package com.programmers.library.repository.seat;

import com.programmers.library.domain.seat.Category;
import com.programmers.library.domain.seat.Seat;
import com.programmers.library.domain.seat.SeatStatus;
import com.programmers.library.repository.sql.builder.DeleteSqlBuilder;
import com.programmers.library.repository.sql.builder.InsertSqlBuilder;
import com.programmers.library.repository.sql.builder.SelectSqlBuilder;
import com.programmers.library.repository.sql.builder.UpdateSqlBuilder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcSeatRepository implements SeatRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcSeatRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Seat> findAll() {
        String sql = new SelectSqlBuilder()
                .select("*")
                .from("seats")
                .build();
        return namedParameterJdbcTemplate.query(sql, getSeatRowMapper());
    }

    @Override
    public Seat insert(Seat seat) {
        String sql = new InsertSqlBuilder()
                .insertInto("seats")
                .columns("category, seat_status, created_at, updated_at")
                .values(":category, :seatStatus, :createdAt, :updatedAt")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("category", seat.getCategory().name())
                .addValue("seatStatus", seat.getSeatStatus().name())
                .addValue("createdAt", seat.getCreatedAt())
                .addValue("updatedAt", seat.getUpdatedAt());
        namedParameterJdbcTemplate.update(sql, paramMap);
        return seat;
    }

    @Override
    public Seat update(Seat seat) {
        String sql = new UpdateSqlBuilder()
                .update("seats")
                .set("category = :category, seat_status = :seatStatus, updated_at = :updatedAt")
                .where("seat_id = :seatId")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("seatId", seat.getSeatId())
                .addValue("category", seat.getCategory().name())
                .addValue("seatStatus", seat.getSeatStatus().name())
                .addValue("updatedAt", seat.getUpdatedAt());
        namedParameterJdbcTemplate.update(sql, paramMap);
        return seat;
    }

    @Override
    public Optional<Seat> findById(Long seatId) {
        String sql = new SelectSqlBuilder()
                .select("*")
                .from("seats")
                .where("seat_id = :seatId")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("seatId", seatId);
        try {
            Seat seat = namedParameterJdbcTemplate.queryForObject(sql, paramMap, getSeatRowMapper());
            return Optional.of(seat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long seatId) {
        String sql = new DeleteSqlBuilder()
                .deleteFrom("seats")
                .where("seat_id = :seatId")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("seatId", seatId);
        namedParameterJdbcTemplate.update(sql, paramMap);
    }

    @Override
    public List<Seat> findAllByCategory(Category category) {
        String sql = new SelectSqlBuilder()
                .select("*")
                .from("seats")
                .where("category = :category")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("category", category.name());
        return namedParameterJdbcTemplate.query(sql, paramMap, getSeatRowMapper());
    }

    @Override
    public List<Seat> findAllByPage(int offset, int pageSize) {
        String sql = new SelectSqlBuilder()
                .select("*")
                .from("seats")
                .orderBy("seat_id", "ASC")
                .limit(pageSize)
                .offset(offset)
                .build();
        return namedParameterJdbcTemplate.query(sql, getSeatRowMapper());
    }

    @Override
    public int getCount() {
        String sql = new SelectSqlBuilder()
                .select("COUNT(*)")
                .from("seats")
                .build();
        return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    private RowMapper<Seat> getSeatRowMapper() {
        return (rs, rowNum) -> {
            Long seatId = rs.getLong("seat_id");
            SeatStatus seatStatus = SeatStatus.valueOf(rs.getString("seat_status").toUpperCase());
            Category category = Category.valueOf(rs.getString("category"));
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
            return new Seat(createdAt, seatId, category, seatStatus, updatedAt);
        };
    }
}
