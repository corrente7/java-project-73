FROM node:20.6.1 AS frontend

WORKDIR /frontend

COPY frontend/package*.json .

RUN npm ci

COPY frontend /frontend

RUN npm run build

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.3

RUN apt-get update && apt-get install -yq make unzip

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle

RUN mv gradle-${GRADLE_VERSION} ${GRADLE_HOME}

ENV PATH=$PATH:$GRADLE_HOME/bin

COPY ./ .

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M"
EXPOSE 8080

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar