package com.programmers.library.repository.reserve;

import com.programmers.library.domain.Reserve;
import com.programmers.library.domain.ReserveStatus;
import com.programmers.library.domain.Seat;
import com.programmers.library.domain.StudentId;
import com.programmers.library.repository.seat.JdbcSeatRepository;
import com.programmers.library.repository.sql.builder.DeleteSqlBuilder;
import com.programmers.library.repository.sql.builder.InsertSqlBuilder;
import com.programmers.library.repository.sql.builder.SelectSqlBuilder;
import com.programmers.library.repository.sql.builder.UpdateSqlBuilder;
import com.programmers.library.util.UUIDMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JdbcReserveRepository implements ReserveRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcSeatRepository jdbcSeatRepository;

    public JdbcReserveRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcSeatRepository jdbcSeatRepository) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcSeatRepository = jdbcSeatRepository;
    }

    @Override
    public List<Reserve> findAll() {
        String sql = new SelectSqlBuilder()
                .select("*")
                .from("reserves")
                .build();
        return namedParameterJdbcTemplate.query(sql, getReserveRowMapper());
    }

    @Override
    public Reserve insert(Reserve reserve) {
        String sql = new InsertSqlBuilder()
                .insertInto("reserves")
                .columns("reserve_id, student_id, student_name, seat_id, reserve_status, created_at, updated_at")
                .values(":reserveId, :studentId, :studentName, :seatId, :reserveStatus, :createdAt, :updatedAt")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("reserveId", UUIDMapper.toBytes(reserve.getReserveId()))
                .addValue("studentId", reserve.getStudentId().studentId())
                .addValue("studentName", reserve.getStudentName())
                .addValue("seatId", Optional.ofNullable(reserve.getSeat()).map(Seat::getSeatId).orElse(null))
                .addValue("reserveStatus", reserve.getReserveStatus().name())
                .addValue("createdAt", reserve.getCreatedAt())
                .addValue("updatedAt", reserve.getUpdatedAt());
        namedParameterJdbcTemplate.update(sql, paramMap);
        return reserve;
    }

    @Override
    public Reserve update(Reserve reserve) {
        String sql = new UpdateSqlBuilder()
                .update("reserves")
                .set("seat_id = :seatId, reserve_status = :reserveStatus, updated_at = :updatedAt")
                .where("reserve_id = :reserveId")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("reserveId", reserve.getReserveId())
                .addValue("seatId", Optional.ofNullable(reserve.getSeat()).map(Seat::getSeatId).orElse(null))
                .addValue("reserveStatus", reserve.getReserveStatus().name())
                .addValue("updatedAt", reserve.getUpdatedAt());
        namedParameterJdbcTemplate.update(sql, paramMap);
        return reserve;
    }

    @Override
    public Optional<Reserve> findById(UUID reserveId) {
        String sql = new SelectSqlBuilder()
                .select("*")
                .from("reserves")
                .where("reserve_id = :reserveId")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("reserveId", reserveId);
        try {
            Reserve reserve = namedParameterJdbcTemplate.queryForObject(sql, paramMap, getReserveRowMapper());
            return Optional.of(reserve);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(UUID reserveId) {
        String sql = new DeleteSqlBuilder()
                .deleteFrom("reserves")
                .where("reserve_id = :reserveId")
                .build();
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("reserveId", reserveId);
        namedParameterJdbcTemplate.update(sql, paramMap);
    }

    private RowMapper<Reserve> getReserveRowMapper() {
        return (rs, rowNum) -> {
            UUID reserveId = UUIDMapper.toUUID(rs.getBytes("reserve_id"));
            StudentId studentId = new StudentId(rs.getString("student_id"));
            String studentName = rs.getString("student_name");
            Seat seat = jdbcSeatRepository.findById(Long.valueOf(rs.getString("seat_id"))).orElse(null);
            ReserveStatus reserveStatus = ReserveStatus.valueOf(rs.getString("reserve_status"));
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
            return new Reserve(reserveId, studentId, studentName, seat, reserveStatus, createdAt, updatedAt);
        };
    }
}
