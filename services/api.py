from extractor import pipeline
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Initialize CORS

@app.route('/extract_entities', methods=['POST'])
def main():
    text = request.get_data(as_text=True)
    result = pipeline(text)
    return jsonify(result)

if __name__ == '__main__':
    app.run()
