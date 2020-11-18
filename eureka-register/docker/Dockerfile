FROM openjdk:8-jdk-alpine
ADD eureka-register-1.0.1.jar eureka-register-1.0.1.jar
VOLUME /tmp
EXPOSE 8000
ENTRYPOINT ["java","-jar","/eureka-register-1.0.1.jar" ,"--spring.profiles.active=pro"]