from extractor import pipeline
from extractor import pipelineN
from extractor import pipeline, get_age, get_gender
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Initialize CORS

@app.route('/extract_entities', methods=['POST'])
def main():
    text = request.get_data(as_text=True)
    result = pipeline(text)
    return jsonify(result)

@app.route('/extract_age', methods=['POST'])
def extract_age():
    text = request.get_data(as_text=True)
    result = get_age(text)
    return jsonify(result)

@app.route('/extract_gender', methods=['POST'])
def extract_gender():
    text = request.get_data(as_text=True)
    result = get_gender(text)
    return jsonify(result)

@app.route('/extract_entities', methods=['PUT'])
def main1():
    text = request.get_data(as_text=True)
    result = pipelineN(text)
    return jsonify({'diseases': result[0], 'negatives': result[1]})

if __name__ == '__main__':
    app.run()
