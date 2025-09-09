# Usando Java 17
FROM eclipse-temurin:17-jdk-alpine AS builder

# Define diretório de trabalho
WORKDIR /app

# Copia arquivos de configuração Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Baixa dependências do Maven (para cache)
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copia o código da aplicação
COPY src src

# Compila a aplicação
RUN ./mvnw clean package -DskipTests

# --------------------
# Segunda imagem (menor)
# --------------------
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia apenas o JAR gerado
COPY --from=builder /app/target/*.jar app.jar

# Define o comando de start
ENTRYPOINT ["java", "-jar", "app.jar"]
