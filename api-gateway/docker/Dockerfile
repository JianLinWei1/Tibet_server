FROM openjdk:8-jdk-alpine
ADD api-gateway-1.0.1.jar app.jar
VOLUME /tmp
EXPOSE 8001
#ENV JAVA_OPTS="-Xms1024m -Xmx4096m"
ENTRYPOINT java   -jar  app.jar  --spring.profiles.active=pro