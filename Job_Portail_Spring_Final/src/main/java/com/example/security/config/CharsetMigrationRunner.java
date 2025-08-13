package com.example.security.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CharsetMigrationRunner {

    private final JdbcTemplate jdbcTemplate;

    public CharsetMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void updateCharset() {
        String[] tables = {
            "books",
            "book_files",
            "book_categories"
        };

        for (String table : tables) {
            try {
                String sql = "ALTER TABLE `" + table + "` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
                jdbcTemplate.execute(sql);
                System.out.println("Charset conversion executed for table: " + table);
            } catch (Exception e) {
                System.err.println("Failed to execute charset conversion for table " + table + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
