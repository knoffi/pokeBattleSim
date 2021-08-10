FROM openjdk:11
# note that we use .dockerignore to ignore frontend and some other stuff
COPY . /usr/src/myapp
COPY ./web/build/ /usr/src/myapp/src/main/resources/static
WORKDIR /usr/src/myapp
RUN ./mvnw clean install
EXPOSE 8080
CMD ["/usr/local/openjdk-11/bin/java", "-jar", "./target/demo-0.0.1-SNAPSHOT.jar"]

# TODO cleanup and optimization, including building frontend and optimizing for production

# docker run -itp 8080:8080 poke-battle-sim /bin/bash
# java -jar ./target/demo-0.0.1-SNAPSHOT.jar

# docker build -t poke-battle-sim .
# docker run -p 8080:8080 poke-battle-sim java -jar ./target/demo-0.0.1-SNAPSHOT.jar
# docker run -p 8080:8080 poke-battle-sim

# second change, add only our minimal "JRE" distribution and our app
# FROM debian:stretch-slim

# ENV JAVA_HOME=/opt/java-minimal
# ENV PATH="$PATH:$JAVA_HOME/bin"

# COPY --from=packager "$JAVA_HOME" "$JAVA_HOME"
# COPY "build/libs/spring-boot-demo.jar" "/app.jar"

# EXPOSE 8080
# CMD [ "-jar", "/app.jar" ]
# ENTRYPOINT [ "java" ]
