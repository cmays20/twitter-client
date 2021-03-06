FROM openjdk:11-jre

ENV APP_JAR_NAME twitter-client

ENV APP_HOME /opt/${APP_JAR_NAME}
ENV APP_JAR ${APP_JAR_NAME}.jar

ADD target/*.jar ${APP_HOME}/${APP_JAR}
ADD docker-entrypoint.sh /docker-entrypoint.sh

RUN sh -c 'touch ${APP_HOME}/${APP_JAR}' && \
        chmod a+x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]

EXPOSE 8080 9090