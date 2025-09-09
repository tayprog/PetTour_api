#!/usr/bin/env bash
set -e  # faz o script parar em caso de erro

echo ">> Configurando Java 17..."
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH
java -version

echo ">> Garantindo permissão para o Maven Wrapper..."
chmod +x ./mvnw

echo ">> Iniciando build com Maven..."
./mvnw clean install -DskipTests

echo ">> Build finalizado com sucesso!"
