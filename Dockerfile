FROM 42.159.111.31/stemproj/robertaserverbase:1

RUN mkdir -p /data/project/robertalab
COPY . /data/project/robertalab/

RUN cd /data/project/robertalab/OpenRobertaParent \
    && mvn clean install -DskipTests
WORKDIR /data/project/robertalab

EXPOSE 80
ENTRYPOINT ["./ora.sh", "--start-from-git"]