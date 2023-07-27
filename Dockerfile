FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY target/demo-0.0.1-SNAPSHOT.jar picpay.jar
ENTRYPOINT ["java","-jar","/picpay.jar"]