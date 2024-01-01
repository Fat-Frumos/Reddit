FROM debian:bullseye-slim

WORKDIR /opt/jboss

RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    curl \
    && rm -rf /var/lib/apt/lists/*

RUN curl -L https://github.com/keycloak/keycloak/releases/download/23.0.1/keycloak-23.0.1.tar.gz | tar zx

RUN mv /opt/jboss/keycloak-23.0.1 /opt/jboss/keycloak
RUN ls -la /opt/jboss/keycloak/bin

ENV KEYCLOAK_USER=admin
ENV KEYCLOAK_PASSWORD=admin

ENV DB_VENDOR=postgres
ENV DB_ADDR=jdbc:postgresql://oregon-postgres.render.com:5432
ENV DB_DATABASE=scenario
ENV KC_DB_USERNAME=dev
ENV KC_DB_PASSWORD=PuUQvcwdWJUohyaZmwCOSlLbSiKSpgP9

EXPOSE 8080

CMD ["/opt/jboss/keycloak/bin/kc.sh", "start-dev"]

#CMD ["/opt/jboss/keycloak/bin/kc.sh", "start-dev", "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile", "-Dkeycloak.migration.file=/opt/jboss/keycloak/standalone/configuration/keycloak-export.json", "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING", "-Dkeycloak.migration.realmName=myrealm", "-Dkeycloak.migration.usersExportStrategy=REALM_FILE", "-Dkeycloak.migration.usersPerRealm=1", "-Dkeycloak.migration.adminUsers=admin", "-Dkeycloak.migration.exported-users=/opt/jboss/keycloak/exported-users.json", "-Dkeycloak.migration.exported-realm=/opt/jboss/keycloak/exported-realm.json", "-Dkeycloak.profile.feature.upload_scripts=enabled", "-Dkeycloak.database.url=jdbc:h2:./standalone/data/keycloak;DB_CLOSE_ON_EXIT=FALSE"]