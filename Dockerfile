FROM azul/zulu-openjdk-alpine:11.0.20

RUN mkdir /app

COPY app.jar /app/app.jar

WORKDIR /app

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
