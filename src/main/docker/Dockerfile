FROM anapsix/alpine-java

VOLUME /tmp

ADD @project.build.finalName@.jar /

EXPOSE @ogn.gateway.web.port@

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/@project.build.finalName@.jar"]