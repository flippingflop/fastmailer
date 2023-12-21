# Builder stage
FROM openjdk:17-slim AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
# Convert line endings to Unix style and make it executable
RUN sed -i 's/\r$//' gradlew && chmod +x ./gradlew

# Copy only the files necessary for downloading dependencies
COPY build.gradle .
COPY settings.gradle .
# Download dependencies - separate layer
RUN ./gradlew --no-daemon dependencies

# Copy the source code - this layer will be rebuilt when source changes
COPY src src
# Build the application
RUN ./gradlew build -x test

# Deploy stage
FROM openjdk:17-slim AS deploy
WORKDIR /app
# Replace 'your-app-name.jar' with the name of your executable JAR file
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
