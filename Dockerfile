FROM openjdk:8-jdk-alpine

ENV APP_NAME=word2pdf \
	TIMEZONE="Asia/Shanghai"

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN apk update
RUN apk add tzdata
RUN apk add libreoffice
RUN apk add fontconfig

COPY simsun.ttc /usr/share/fonts/chinese/simsun.ttf
RUN chmod -R 755 /usr/share/fonts/chinese
RUN fc-cache -fv

ADD ./word2pdf/target/*.jar /opt/${APP_NAME}/

COPY entrypoint.sh /sbin/entrypoint.sh
RUN chmod 755 /sbin/entrypoint.sh

EXPOSE 1993

ENTRYPOINT ["/sbin/entrypoint.sh"]
