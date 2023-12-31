FROM openjdk:17-jdk

WORKDIR /opt/jboss

RUN curl -L https://github.com/keycloak/keycloak/releases/download/23.0.1/keycloak-23.0.1.tar.gz | tar zx

RUN mv /opt/jboss/keycloak-23.0.1 /opt/jboss/keycloak
RUN ls -la /opt/jboss/keycloak

ENV KEYCLOAK_USER=admin
ENV KEYCLOAK_PASSWORD=admin

EXPOSE 8080

CMD ["/opt/jboss/keycloak/bin/kc.sh", "start-dev"]
