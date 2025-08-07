FROM openjdk:21-jdk-slim

# JAR 파일 복사 (Spring Boot 빌드 후 생성된 JAR 파일)
COPY ./build/libs/qeat-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar","app.jar"]
