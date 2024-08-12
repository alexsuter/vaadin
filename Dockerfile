FROM container-registry.oracle.com/graalvm/native-image:21 AS build

# install maven
ARG MAVEN_VERSION=3.9.8
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
 && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
 && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
 && rm -f /tmp/apache-maven.tar.gz \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# build native app
COPY . /build
RUN mvn -f /build/pom.xml -Pproduction -Pnative native:compile

# create runtime container
FROM scratch
COPY --from=build /build/target/product-listing-service /service
ENTRYPOINT ["/service"]
EXPOSE 8080
