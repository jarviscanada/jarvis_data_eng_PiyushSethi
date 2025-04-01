FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml /app/
COPY src /app/src
# Build the application using Maven, skip tests, downloads and stores all dependencies into the target/dependency/
RUN mvn clean package -DskipTests dependency:copy-dependencies
ENTRYPOINT ["java", "-cp",  "target/classes:target/dependency/*", "ca.jrvs.apps.jdbc.Main"]