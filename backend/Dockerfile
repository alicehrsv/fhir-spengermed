FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY spring-boot/pom.xml ./
RUN mvn -B -X -e dependency:go-offline
COPY spring-boot/src ./src
RUN mvn -B -Dmaven.test.skip=true package
FROM eclipse-temurin:21-jre-jammy
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xms256m","-Xmx1024m","-jar","/app/app.jar"]