FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /opt/app

COPY *.jar /opt/app/app.jar


ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
