# ── Stage 1: Build ─────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build fat JAR with BuildKit cache mount (persists downloaded dependencies across retries)
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package -DskipTests

# ── Stage 2: Runtime ───────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /workspace/target/*.jar app.jar

RUN chown appuser:appgroup app.jar
USER 100:101

EXPOSE 8080

# JVM tuning for low-memory containers (important on 8 GB host)
ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseContainerSupport", \
  "-jar", "app.jar"]
