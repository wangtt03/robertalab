FROM csdiregistry.azurecr.io/robertalab-base:16.04

RUN mkdir -p /data/project/robertalab
COPY . /data/project/robertalab/
WORKDIR /data/project/robertalab
RUN ./ora.sh --createemptydb OpenRobertaServer/db-2.2.0/openroberta-db

EXPOSE 1999
ENTRYPOINT ["./ora.sh", "--start-from-git"]