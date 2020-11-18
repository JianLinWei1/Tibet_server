FROM openjdk:8-jdk-alpine
ADD api-gateway-1.0.1.jar api-gateway-1.0.1.jar
VOLUME /tmp
EXPOSE 8001
ENTRYPOINT ["java","-jar","/api-gateway-1.0.1.jar" ,"--spring.profiles.active=pro"]