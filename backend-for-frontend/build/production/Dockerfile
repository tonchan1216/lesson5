# Arguments
ARG APP_PATH="/usr/local/lesson5"
ARG MODULE_NAME="lesson5-bff-0.0.1-SNAPSHOT"

# Build Image
FROM adoptopenjdk/maven-openjdk11:latest AS builder
LABEL  maintainer "cleartone1216"
ARG APP_PATH

RUN apt-get update && apt-get install -y git
RUN git clone https://github.com/tonchan1216/lesson5.git ${APP_PATH}
RUN mvn install -f ${APP_PATH}/common/pom.xml
RUN mvn package -Dmaven.test.skip=true -f ${APP_PATH}/backend-for-frontend/pom.xml

# Deploy Image
FROM adoptopenjdk/openjdk11:alpine-slim
ARG APP_PATH
ARG MODULE_NAME

RUN apk --update add tzdata && \
    cp /usr/share/zoneinfo/Asia/Tokyo /etc/localtime && \
    apk del tzdata && \
    rm -rf /var/cache/apk/* && \
    mkdir -p /opt/app/
COPY --from=builder ${APP_PATH}/backend-for-frontend/target/${MODULE_NAME}.jar /opt/app/

EXPOSE 8080

CMD java -jar -Dspring.profiles.active=production /opt/app/${MODULE_NAME}.jar
