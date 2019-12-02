from plant_disease_recognition import *
import db

import base64
import json

from flask import Flask, request, Response

app = Flask(__name__)
checkpoint_plants_path = '../checkpoints/test/plants_checkpoint_e16.pth'
checkpoint_disease_path = '../checkpoints/test/disease_checkpoint_e16.pth'

model_plants, classes_plants = load_checkpoint_plants(checkpoint_plants_path)
model_disease, classes_disease = load_checkpoint_diseases(checkpoint_disease_path)


@app.route('/api/test', methods=['POST'])
def test():
    print('here')
    imgdata = base64.b64decode(request.values.get('image'))
    print('receive')
    image_name = "image.jpeg"
    with open(image_name, 'wb') as f:
        f.write(imgdata)

    object_class = None
    plant, acc_plant, disease, acc_disease = predict(image_name, model_plants, classes_plants, model_disease, classes_disease)
    print("Plant: %s | Acc: %.3f" % (plant, acc_plant))
    print("Disease: %s | Acc: %.3f" % (disease, acc_disease))
    col = db.init_db()
    treatment = db.get_treatment(col, plant, disease)
    print(treatment)


    res = json.dumps([{'phrase': object_class, 'plant': plant, 'disease': disease, 'treatment': treatment, 'success': True }])
    print('send')
    return Response(res,
                    status=200,
                    mimetype="application/json"
                    )

app.run(host='0.0.0.0')