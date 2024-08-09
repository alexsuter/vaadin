FROM container-registry.oracle.com/graalvm/native-image:21
COPY target/unified-chat-tutorial app
RUN ls -lisa /app
ENTRYPOINT ["/app/app"]

EXPOSE 8080
#COPY target/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
