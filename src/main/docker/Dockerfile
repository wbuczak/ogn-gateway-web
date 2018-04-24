FROM anapsix/alpine-java

VOLUME /tmp

ADD @project.build.finalName@.jar /

EXPOSE @ogn.gateway.web.port@

#ENTRYPOINT ["java","-Xmx128m", "-Djava.security.egd=file:/dev/./urandom","-jar","/@project.build.finalName@.jar"]

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /@project.build.finalName@.jar"].