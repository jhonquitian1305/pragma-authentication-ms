#FROM eclipse-temurin:21-jdk-alpine
#VOLUME /tmp
#COPY *.jar authentication.jar
#ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
## Replace with a non-root user to avoid running the container with excessive privileges
#USER appuser
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar authentication.jar" ]

FROM gradle:jdk21-alpine AS build
WORKDIR /app

COPY --chown=gradle:gradle build.gradle settings.gradle gradlew gradlew.bat gradle/ ./

COPY --chown=gradle:gradle . .

RUN ./gradlew clean build -x validateStructure -x test --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/applications/app-service/build/libs/authentication.jar authentication.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "authentication.jar"]