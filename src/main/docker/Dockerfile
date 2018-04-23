FROM anapsix/alpine-java

VOLUME /tmp

ADD @project.build.finalName@.jar /

EXPOSE @ogn.gateway.web.port@

ENTRYPOINT ["java","-Xmx32m", "-Djava.security.egd=file:/dev/./urandom","-jar","/@project.build.finalName@.jar"]