FROM openjdk:8-jdk-alpine
RUN echo "http://mirrors.aliyun.com/alpine/v3.6/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.6/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && apk add ttf-dejavu \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
COPY /lib/libJsyunew3_64.so  /usr/bin
ADD main-service-1.0.1.jar app.jar
VOLUME /tmp
EXPOSE 8002
#ENV JAVA_OPTS="-Xms1024m -Xmx4096m"
ENTRYPOINT java    -jar  app.jar  --spring.profiles.active=pro