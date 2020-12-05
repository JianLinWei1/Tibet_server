FROM  java:8
ADD main-service-1.0.1.jar app.jar
VOLUME /tmp
EXPOSE 8002
ENTRYPOINT ["java","-jar","/app.jar" ,"--spring.profiles.active=pro"]