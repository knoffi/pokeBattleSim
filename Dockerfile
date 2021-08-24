FROM openjdk:11 as packager
# note that we use .dockerignore to ignore most of the frontend and more
COPY . /usr/src/app
COPY ./web/build/ /usr/src/app/src/main/resources/static
WORKDIR /usr/src/app
RUN ./mvnw clean install

EXPOSE 8080
CMD ["/usr/local/openjdk-11/bin/java", "-jar", "./target/demo-0.0.1-SNAPSHOT.jar"]

# debug via
# docker run -itp 8080:8080 poke-battle-sim /bin/bash

# second stage: For smaller images and security (minimize attack surface by not installing anything unnecessary)
FROM adoptopenjdk/openjdk11:alpine

WORKDIR /usr/src/app
COPY --from=packager /usr/src/app/target .

# dont env var change name. will be used by google-cloud-translate library
ENV GOOGLE_APPLICATION_CREDENTIALS=/usr/src/app/mnt/secrets/google-translate-api-key.json

EXPOSE 8080
CMD ["java", "-jar", "./demo-0.0.1-SNAPSHOT.jar" ]
