import torch
from transformers import AutoTokenizer, AutoModelForTokenClassification


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

def extract_entities(text):
    inputs = tokenizer.encode(text, return_tensors="pt")
    outputs = model(inputs)[0]
    predictions = torch.argmax(outputs, dim=2)
    tokens = tokenizer.convert_ids_to_tokens(inputs[0])
    entities = []
    current_entity = ""
    current_label = None

    for token, label_idx in zip(tokens, predictions[0].tolist()):
        label = model.config.id2label[label_idx]

        if label.startswith("B-"):
            if current_entity:
                entities.append((current_entity, current_label))
            current_entity = token
            current_label = label.split("-")[1]
        elif label.startswith("I-"):
            if current_entity and current_label == label.split("-")[1]:
                current_entity += " " + token
        else:
            if current_entity:
                entities.append((current_entity, current_label))
            current_entity = ""
            current_label = None

    if current_entity:
        entities.append((current_entity, current_label))

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
    tmp = [entity for entity in tmp if not (entity in seen or seen.add(entity))]
    result = [item for item in tmp if len(item) >= 3]
    result = [item.replace(" ' ", "'") for item in result]
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
