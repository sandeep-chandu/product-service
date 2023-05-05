FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/product-service-1.0-SNAPSHOT.jar product-service-1.0.jar
ENTRYPOINT ["java","-jar","/cake-manager-1.0.jar"]