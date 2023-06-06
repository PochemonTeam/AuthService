# Stage 1: Build and install the DTO project into local maven repository
FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /PochemonLib

# Copy your DTO project
COPY ./PochemonLib/pom.xml ./pom.xml
COPY ./PochemonLib/src ./src

RUN mvn clean install

WORKDIR /RPCLib

# Copy your DTO project
COPY ./RPCLib/pom.xml ./pom.xml
COPY ./RPCLib/src ./src

# Build the DTO project and install it into the local maven repository
RUN mvn clean install

WORKDIR /AuthService

# Copy your main project
COPY ./AuthService/pom.xml ./pom.xml
COPY ./AuthService/src ./src

# Build the main project with the dependencies
RUN mvn clean package -DskipTests

# Stage 3: Copy the artifact and run
FROM openjdk:17-jdk-slim
WORKDIR /AuthService

COPY --from=builder /AuthService/target/auth-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java","-jar","app.jar"]