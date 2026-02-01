package com.example.board.member.service.config.datasource;

import java.util.Optional;

public interface DataSourceContextHolder {
    Optional<DataSourceType> get();
}
