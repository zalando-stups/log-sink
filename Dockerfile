FROM registry.opensource.zalan.do/stups/openjdk:8u66-b17-1-12

MAINTAINER Zalando SE

CMD java $(java-dynamic-memory-opts 50) $(appdynamics-agent) -jar /log-sink.jar

COPY target/log-sink.jar /
COPY target/scm-source.json /
