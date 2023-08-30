FROM openjdk:17

RUN mkdir /eatout-app

COPY target/ /eatout-app

WORKDIR /eatout-app

CMD java -jar FoodDeliverySystem-0.0.1-SNAPSHOT.jar --spring.config.name=application.properties