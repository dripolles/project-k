FROM openjdk:8

ADD build/libs/project-k.jar /project-k/project-k.jar
ADD static /project-k/static

WORKDIR /project-k

CMD ["java", "-jar", "project-k.jar"]

