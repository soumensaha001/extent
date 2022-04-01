FROM amazoncorretto:11-alpine-jdk
MAINTAINER Soumen
COPY target/klov-0.0.1-SNAPSHOT.jar klov-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/klov-0.0.1-SNAPSHOT.jar"]