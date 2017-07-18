FROM csdiregistry.azurecr.io/robertalab-base:16.04

RUN mkdir -p /data/project/robertalab
COPY . /data/project/robertalab/
WORKDIR /data/project/robertalab
RUN cp -rf OpenRobertaServer/dbBase OpenRobertaServer/db-2.2.0

EXPOSE 1999
ENTRYPOINT ["./ora.sh", "--start-from-git"]