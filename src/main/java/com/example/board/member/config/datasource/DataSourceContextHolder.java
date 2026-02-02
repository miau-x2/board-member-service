package com.example.board.member.config.datasource;

import java.util.Optional;

public interface DataSourceContextHolder {
    Optional<DataSourceType> get();
}
