FROM  frolvlad/alpine-java:jdk8-slim
ADD main-service-1.0.1.jar app.jar
VOLUME /tmp
EXPOSE 8002
ENV JAVA_OPTS="-Xms1024m -Xmx4096m"
ENTRYPOINT java $JAVA_OPTS   -jar  app.jar  --spring.profiles.active=pro