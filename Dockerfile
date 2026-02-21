# Используем OpenJDK 17
FROM openjdk:17-jdk-slim

# Рабочая директория
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости (кэшируем для ускорения)
COPY pom.xml .
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

# Копируем весь проект
COPY src ./src

# Собираем jar
RUN mvn clean package -DskipTests

# Указываем команду запуска
CMD ["java", "-jar", "target/murasAI-0.0.1-SNAPSHOT.jar"]

# Экспонируем порт (Render передаст через PORT)
EXPOSE 8080