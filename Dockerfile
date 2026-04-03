# ========== FASE 1: BUILD ==========
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de build primero
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN apk add --no-cache dos2unix \
    && dos2unix ./gradlew \
    && chmod +x ./gradlew

# Descargar dependencias
RUN ./gradlew dependencies --no-daemon

# Ahora copiar el resto del código
COPY . .

# Build
RUN ./gradlew clean bootJar --no-daemon

# ========== FASE 2: RUNTIME ==========
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Puerto
EXPOSE 8086

# Run
ENTRYPOINT ["java", "-jar", "/app/app.jar"]