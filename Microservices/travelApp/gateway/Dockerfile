FROM python:3

RUN apt-get update && apt-get -y install netcat && apt-get clean

WORKDIR /app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
COPY --from=lachlanevenson/k8s-kubectl:v1.10.3 /usr/local/bin/kubectl /usr/local/bin/kubectl

COPY config.yml ./
COPY run.sh ./
COPY gateway.py ./

RUN chmod +x ./run.sh

CMD ["./run.sh"]
