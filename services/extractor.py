import torch
import re
from transformers import AutoTokenizer, AutoModelForTokenClassification
from flask import jsonify

tokenizer = AutoTokenizer.from_pretrained("alvaroalon2/biobert_diseases_ner")
model = AutoModelForTokenClassification.from_pretrained("alvaroalon2/biobert_diseases_ner")

def pipeline(text):
    
    texts = []
    copied_text = '' + text
    while len(text) > 512:
        # search for last whitespace before 2039 characters
        index = text.rfind(".", 0, 512)
        if index == -1:
            index = text.rfind(" ", 0, 512)
        if index == 0 or index == -1:
            index = 512
        texts.append(text[:index])
        text = text[index:]
    else:
        texts.append(text)

    extracted_entities = []
    for text in texts:
        print('text: ', text)
        extracted_entities.extend(extract_entities(text))
    cleaned_entities = clean(extracted_entities)
    return removeNegatives(copied_text, cleaned_entities)

def pipelineN(text):
    
    texts = []
    copied_text = '' + text
    while len(text) > 512:
        # search for last whitespace before 2039 characters
        index = text.rfind(".", 0, 512)
        if index == -1:
            index = text.rfind(" ", 0, 512)
        if index == 0 or index == -1:
            index = 512
        texts.append(text[:index])
        text = text[index:]
    else:
        texts.append(text)

    extracted_entities = []
    for text in texts:
        print('text: ', text)
        extracted_entities.extend(extract_entities(text))
    cleaned_entities = clean(extracted_entities)
    return removeNegatives(copied_text, cleaned_entities), getNegatives(copied_text, cleaned_entities)

def extract_entities(text):
    inputs = tokenizer.encode(text, return_tensors="pt")
    outputs = model(inputs)[0]
    predictions = torch.argmax(outputs, dim=2)
    tokens = tokenizer.convert_ids_to_tokens(inputs[0])

    entities = []
       
    preds = predictions[0].tolist()
    prev = False

    for i in range(len(tokens)):
        label = "DISEASE"

        if preds[i] == 0:
            entities.append((tokens[i], label))
            prev = True
        elif (preds[i] == 1 or tokens[i].startswith("##")) and prev:
            entities[len(entities)-1] = (entities[len(entities)-1][0] + " " + tokens[i], label)
        else:
            prev = False
            continue

    return entities

def clean(extracted_entities):
    result = []

    print(extracted_entities)

    for entity, label in extracted_entities:
        entity = entity.replace(" ##", "")
        result.append(entity)

    tmp = []

    for i in range(len(result)):
        if result[i].startswith("##"):
            if len(tmp) == 0:
                continue
            tmp[len(tmp)-1] = tmp[len(tmp)-1] + result[i].replace("##","").replace(" ##","")
        elif any(token in result[i] for token in ["[SEP]", "[CLS]", "[PAD]", "[UNK]", "[MASK]"]):
            continue
        else:
            tmp.append(result[i])

    # Remove duplicates while preserving the order
    seen = set()
    tmp = [entity for entity in tmp if not (entity.lower() in seen or seen.add(entity.lower()))]
    result = [item for item in tmp if len(item) >= 3]
    result = [item.replace(" ' ", "'") for item in result]
    result = [item.replace(" ,", ",") for item in result]
    result = [item.replace("( ", "(") for item in result]
    result = [item.replace(" )", ")") for item in result]
    result = [item.replace(" / ", "/") for item in result]
    return result

def removeNegatives(text, extracted_entities):
    result = []
    for disease in extracted_entities:
        if ("not have " + disease).lower() in text.lower():
            continue
        if ("not " + disease).lower() in text.lower():
            continue
        if ("doesn't have " + disease).lower() in text.lower():
            continue
        if ("hasn't " + disease).lower() in text.lower():
            continue
        if ("has not " + disease).lower() in text.lower():
            continue
        if ("no history of " + disease).lower() in text.lower():
            continue
        if ("no " + disease).lower() in text.lower():
            continue
        if ("no evidence of " + disease).lower() in text.lower():
            continue
        if ("no recent history of " + disease).lower() in text.lower():
            continue
        if ("negative for " + disease).lower() in text.lower():
            continue
        if ("negative " + disease).lower() in text.lower():
            continue
        result.append(disease)

    return result


def getNegatives(text, extracted_entities):
    result = []
    for disease in extracted_entities:
        if ("not have " + disease).lower() in text.lower():
            result.append("not have " + disease)
            continue
        if ("not " + disease).lower() in text.lower():
            result.append("not " + disease)
            continue
        if ("doesn't have " + disease).lower() in text.lower():
            result.append("doesn't have " + disease)
            continue
        if ("hasn't " + disease).lower() in text.lower():
            result.append("hasn't " + disease)
            continue
        if ("has not " + disease).lower() in text.lower():
            result.append("has not " + disease)
            continue
        if ("no history of " + disease).lower() in text.lower():
            result.append("no history of " + disease)
            continue
        if ("no " + disease).lower() in text.lower():
            result.append("no " + disease)
            continue
        if ("no evidence of " + disease).lower() in text.lower():
            result.append("no evidence of " + disease)
            continue
        if ("no recent history of " + disease).lower() in text.lower():
            result.append("no recent history of " + disease)
            continue
        if ("negative for " + disease).lower() in text.lower():
            result.append("negative for " + disease)
            continue
        if ("negative " + disease).lower() in text.lower():
            result.append("negative " + disease)
            continue

    return result


def get_age(admission_note):
    age = re.findall(r'\d{1,3}', admission_note)
    
    if len(age) > 0:
        return int(age[0])
    else:
        return -1
    
def get_gender(admission_note):
    maleTerms = ['male', 'man', 'gentleman', 'boy', 'm']
    femaleTerms = ['female', 'woman', 'lady', 'girl', 'f']
    allTerms = maleTerms + femaleTerms
    
    # Match any female or male term
    sex = re.findall(r'\b(?:%s)\b' % '|'.join(allTerms), admission_note[:100], flags=re.IGNORECASE)
        
    # Find sex like 48M get only the M
    sex.extend(re.findall(r'\d{1,3}\s?(%s)' % '|'.join(allTerms), admission_note[:100], flags=re.IGNORECASE))
        
    if len(sex) > 0:
        sex = sex[0].lower()
        return 'm' if sex in maleTerms else 'f'
    else:
        return 'u'
