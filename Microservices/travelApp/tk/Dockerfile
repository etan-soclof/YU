FROM python:3

RUN apt-get update && apt-get -y install netcat && apt-get clean && apt-get update && apt-get install -y vim

WORKDIR /tmp

RUN  /usr/bin/curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl \
     && chmod +x ./kubectl  \
     &&  mv ./kubectl /usr/local/bin/kubectl

COPY sender.sh ./

WORKDIR /app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY csv-receiver.py ./
COPY csv-reader.sh ./
COPY csv-reader2.sh ./
RUN chmod 755 ./reader.sh
RUN chmod 755 ./csv-reader2.sh