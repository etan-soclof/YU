from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route('/', methods = ['POST'])
def update_text():
    content=request.json
    return content

if __name__ == '__main__':
    app.run(host= '0.0.0.0',debug=True)