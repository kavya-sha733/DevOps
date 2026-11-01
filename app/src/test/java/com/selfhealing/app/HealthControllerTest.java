package com.selfhealing.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthControllerTest {

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:health_ok_db;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "APP_BREAK=false",
        "APP_VERSION=1.0.0",
        "APP_NAME=self-healing-app"
    })
    @DisplayName("Healthy Application State")
    class HealthyTests {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("GET /health returns 200 OK and status UP")
        void healthReturnsUp() throws Exception {
            mockMvc.perform(get("/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.message").value("Application is healthy"));
        }

        @Test
        @DisplayName("GET / returns 200 OK with app name and version")
        void rootReturnsAppInfo() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.app").value("self-healing-app"))
                    .andExpect(jsonPath("$.version").value("1.0.0"));
        }
    }

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    @TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:health_broken_db;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "APP_BREAK=true"
    })
    @DisplayName("Broken Application State (Self-Healing Trigger)")
    class BrokenTests {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("GET /health returns 503 SERVICE UNAVAILABLE and status DOWN when APP_BREAK=true")
        void healthReturnsDownWhenAppBreakIsTrue() throws Exception {
            mockMvc.perform(get("/health"))
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath("$.status").value("DOWN"))
                    .andExpect(jsonPath("$.message").value("Simulated failure via APP_BREAK=true"));
        }
    }
}

