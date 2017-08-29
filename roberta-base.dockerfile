FROM ubuntu:16.04

RUN apt-get update \
    && apt-get install -y openjdk-8-jdk \
    && apt-get install libusb-0.1-4 \
    && apt-get install -y gcc-avr binutils-avr gdb-avr avr-libc avrdude \
    && apt-get install -y nbc \
    && apt-get install -y gcc-arm-none-eabi srecord libssl-dev
