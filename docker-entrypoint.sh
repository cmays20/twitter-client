#!/bin/bash

set -e

if [ "$ENABLE_JMX" = "true" ]; then
    JMX_OPTS = "-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=9090 -Dcom.sun.management.jmxremote.rmi.port=9090 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=$(hostname -i)"
fi

if [ -n "$USER" ]; then
    USER_OPTS1 = "su -c"
    USER_OPTS2 = "${USER} -- "
fi

if [ -z "$1" -o  "${1:0:1}" = '-' ]; then
    CMD = java ${JAVA_OPTS} ${JMX_OPTS} -Djava.security.egd=file:/dev/./urandom -jar ${APP_HOME}/${APP_JAR}
    exec ${USER_OPTS1} ${CMD} ${USER_OPTS2} "$@"
fi

exec "$@"