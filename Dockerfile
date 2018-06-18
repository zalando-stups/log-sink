ARG BASE_IMAGE_VERSION=latest
FROM registry.opensource.zalan.do/stups/openjdk:${BASE_IMAGE_VERSION}

MAINTAINER Zalando SE

CMD ["/bin/bash", "-c", "java $JAVA_OPTS $(java-dynamic-memory-opts) -jar /log-sink.jar"]

COPY target/log-sink.jar /
