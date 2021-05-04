import pika
from flask import Flask

app = Flask(__name__)

connection = pika.BlockingConnection(pika.ConnectionParameters('rabbit'))
channel = connection.channel()

channel.basic_qos(prefetch_count=1)

channel.queue_declare(queue='queue')

@app.route('/')
def add():
    channel.basic_publish(exchange='', routing_key='queue', body='Hello World!')
    return "One task added to queue\n"

@app.route('/get')
def get():
    res = channel.queue_declare(
        queue="queue",
        durable=True,
        exclusive=False,
        auto_delete=False,
        passive=True
    )
    return "Number of tasks remaining: " + str(res.method.message_count) + "\n"

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)