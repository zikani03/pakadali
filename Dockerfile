FROM openjdk:14-alpine
COPY build/libs/pakadali-*-all.jar pakadali.jar
EXPOSE 8080
#CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "dialoguss-flow.jar"]
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar pakadali.jar
