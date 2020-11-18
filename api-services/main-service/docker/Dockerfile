FROM openjdk:8-jdk-alpine
ADD auth-service-1.0.1.jar auth-service-1.0.1.jar
VOLUME /tmp
EXPOSE 8002
ENTRYPOINT ["java","-jar","/auth-service-1.0.1.jar" ,"--spring.profiles.active=pro"]