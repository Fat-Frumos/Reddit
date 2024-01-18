[![Build with Maven](https://github.com/Fat-Frumos/Reddit/actions/workflows/REST-API.yml/badge.svg)](https://github.com/Fat-Frumos/Reddit/actions/workflows/REST-API.yml)

### Commands:

Clean, install, and generate report

`mvn clean install site -P test`

Test report

`mvn surefire-report:report`

Show dependency tree

`mvn dependency:tree`

Build the project with Maven Tool

`mvn -B package --file pom.xml`

Build the project with Maven Tool without Tests

`mvn clean install -e -DskipTests`

Build the project with Maven Tool with Tests

`mvn clean install -X`

`mvn verify -e`

`mvn clean package -DskipTests`
