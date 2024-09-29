FROM openjdk:17.0
# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file from the host into the container
COPY target/Journail-0.0.1-SNAPSHOT.jar /app/journail.jar

# Expose the port that Spring Boot runs on (default is 8080)
EXPOSE 8080

# Define the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/journail.jar"]
