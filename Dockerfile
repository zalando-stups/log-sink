FROM zalando/openjdk:8u45-b14-6

COPY target/logsink.jar /logsink.jar

COPY scm-source.json /scm-source.json

EXPOSE 8080

CMD java -jar /logsink.jar