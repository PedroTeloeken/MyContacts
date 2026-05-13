# Etapa de build
FROM maven:3.9.9-eclipse-temurin-22 AS build

WORKDIR /app

# Arquivos do Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw

# Baixa dependências
RUN ./mvnw dependency:go-offline

# Código-fonte
COPY src src

# Build da aplicação
RUN ./mvnw clean package -DskipTests

# Imagem final
FROM eclipse-temurin:22-jre

WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]