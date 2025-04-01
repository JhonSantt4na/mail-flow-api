FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# 1. Copiar Maven Wrapper primeiro
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# 2. Garantir permissões e download de dependências
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline

# 3. Copiar código fonte
COPY src/ src/

# 4. Build da aplicação
RUN ./mvnw clean install -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/mailflow.jar"]