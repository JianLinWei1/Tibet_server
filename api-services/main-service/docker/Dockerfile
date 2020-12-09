FROM  java:8
ADD main-service-1.0.1.jar app.jar
VOLUME /tmp
EXPOSE 8002
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENTRYPOINT java $JAVA_OPTS   -jar  app.jar  --spring.profiles.active=pro