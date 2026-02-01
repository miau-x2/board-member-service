package com.example.board.member.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class BoardMemberServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardMemberServiceApplication.class, args);
    }

}
