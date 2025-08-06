# Use openjdk image with Java 17 as the base image for building
#FROM openjdk:17-jdk-slim AS MAVEN_BUILD
From eclipse-temurin:17-jdk-jammy AS MAVEN_BUILD
WORKDIR /build/
# Install Maven
# Define Maven version and URL

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*
    
#https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
#https://archive.apache.org/dist/maven/binaries/apache-maven-3.0-alpha-2-bin.tar.gz
COPY . .

RUN cd phcnnode && mvn clean install
RUN cd lemonpayredis && mvn clean install
# RUN cd ekedc && mvn clean install
# RUN cd ikedc && mvn clean install
RUN cd eedc && mvn clean install
#RUN cd jedc && mvn clean install
#RUN cd kaedc && mvn clean install

#RUN cd aedc && mvn clean install

#RUN mvn -B -f phcnnode/pom.xml  install -DskipTests
#RUN mvn -B clean install


RUN cd /build && mvn clean package
#RUN mvn -B clean package -DskipTests
#COPY /build/target/*.jar ./vasgate-electricity-service.jar



ENTRYPOINT ["java", "-jar", "/build/target/lemonpay-electricity-0.0.1-SNAPSHOT.jar"]