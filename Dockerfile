FROM openjdk:17-jdk-slim-buster as builder

WORKDIR /app

# build.Add only files that depend on gradle,
#Idle gradle build to cache dependent files
COPY *.gradle gradle.* gradlew /app/
COPY gradle/ /app/gradle/
RUN ./gradlew build -x test --parallel --continue > /dev/null 2>&1 || true

#Incorporate and build code changes after the following lines
COPY . /app
RUN ./gradlew build -x test --parallel

FROM openjdk:11-jdk-slim-buster

COPY --from=builder /app/build/libs/recipe.jar /app/recipe.jar
CMD ["java", "-jar", "/app/recipe.jar"]