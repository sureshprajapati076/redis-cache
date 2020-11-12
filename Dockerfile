FROM openjdk:8-jre-alpine
ADD /target/*.jar /app.jar
EXPOSE 2020
ENV SPRING_PROFILES_ACTIVE="", JAVA_OPTS=""
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]