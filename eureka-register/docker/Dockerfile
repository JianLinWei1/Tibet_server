FROM openjdk:8-jdk-alpine
ADD eureka-register-1.0.1.jar app.jar
VOLUME /tmp
EXPOSE 8000
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENTRYPOINT java $JAVA_OPTS   -jar  app.jar  --spring.profiles.active=pro