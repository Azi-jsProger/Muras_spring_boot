# Используем официально поддерживаемый образ OpenJDK 17 на базе Debian slim
FROM eclipse-temurin:17-jdk-focal

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven git && rm -rf /var/lib/apt/lists/*

# Создаём рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости (кэшируем для ускорения сборки)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем весь проект
COPY src ./src

# Собираем jar без тестов
RUN mvn clean package -DskipTests -B

# Экспонируем порт, Render передаст свой через переменную PORT
ENV PORT=8080
EXPOSE $PORT

# Команда запуска Spring Boot приложения
CMD ["sh", "-c", "java -jar target/murasAI-0.0.1-SNAPSHOT.jar --server.port=$PORT"]