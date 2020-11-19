FROM openjdk:8-jdk-alpine
ADD main-service-1.0.1.jar main-service-1.0.1.jar
VOLUME /tmp
EXPOSE 8002
ENTRYPOINT ["java","-jar","/main-service-1.0.1.jar" ,"--spring.profiles.active=pro"]