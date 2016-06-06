FROM registry.opensource.zalan.do/stups/openjdk:8-24

MAINTAINER Zalando SE

CMD java $(java-dynamic-memory-opts 40) $(appdynamics-agent) -jar /log-sink.jar

COPY target/log-sink.jar /
COPY target/scm-source.json /
