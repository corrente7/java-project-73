FROM gradle:8.2-jdk17

WORKDIR /

COPY / .

RUN gradle installDist

CMD ./build/install/app/bin/app