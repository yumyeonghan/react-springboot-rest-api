package com.programmers.library.repository.sql.builder;

import java.util.Objects;

public class SelectSqlBuilder {
    private StringBuilder sqlBuilder;

    public SelectSqlBuilder() {
        sqlBuilder = new StringBuilder();
    }

    public SelectSqlBuilder select(String columns) {
        sqlBuilder.append("SELECT ").append(columns);
        return this;
    }

    public SelectSqlBuilder from(String tableName) {
        sqlBuilder.append(" FROM ").append(tableName);
        return this;
    }

    public SelectSqlBuilder where(String condition) {
        if (Objects.isNull(condition) || condition.isBlank()) {
            return this;
        }
        sqlBuilder.append(" WHERE ").append(condition);
        return this;
    }

    public SelectSqlBuilder and(String condition) {
        if (Objects.isNull(condition) || condition.isBlank()) {
            return this;
        }
        sqlBuilder.append(" AND ").append(condition);
        return this;
    }

    public SelectSqlBuilder or(String condition) {
        if (Objects.isNull(condition) || condition.isBlank()) {
            return this;
        }
        sqlBuilder.append(" OR ").append(condition);
        return this;
    }

    public SelectSqlBuilder orderBy(String column, String order) {
        if (Objects.isNull(column) || column.isBlank()) {
            return this;
        }
        sqlBuilder.append(" ORDER BY ").append(column).append(" ").append(order);
        return this;
    }

    public SelectSqlBuilder limit(int limit) {
        if (limit <= 0) {
            return this;
        }
        sqlBuilder.append(" LIMIT ").append(limit);
        return this;
    }

    public SelectSqlBuilder offset(int offset) {
        if (offset < 0) {
            return this;
        }
        sqlBuilder.append(" OFFSET ").append(offset);
        return this;
    }

    public String build() {
        return sqlBuilder.toString();
    }
}
