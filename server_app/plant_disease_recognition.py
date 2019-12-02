import torch
import numpy as np
import matplotlib.pyplot as plt
import torch
import time
import numpy as np
from torch import nn, optim
import torch.nn.functional as F
from torchvision import datasets, transforms, models
import torchvision
from collections import OrderedDict
from torch.autograd import Variable
from PIL import Image
from torch.optim import lr_scheduler
import copy
import json
import os
from os.path import exists
from collections import OrderedDict
from ShuffleNetV2 import ShuffleNetV2


def load_checkpoint_diseases(filepath):
    checkpoint = torch.load(filepath)
    model = models.resnet152()

    # Our input_size matches the in_features of pretrained model
    input_size = 2048
    output_size = 38

    classifier = nn.Sequential(OrderedDict([
        ('fc1', nn.Linear(2048, 512)),
        ('relu', nn.ReLU()),
        # ('dropout1', nn.Dropout(p=0.2)),
        ('fc2', nn.Linear(512, 38)),
        ('output', nn.LogSoftmax(dim=1))
    ]))

    # Replacing the pretrained model classifier with our classifier
    model.fc = classifier
    model.load_state_dict(checkpoint['state_dict'])
    idx_to_class = {v: k for k, v in checkpoint['class_to_idx'].items()}
    return model, idx_to_class


def load_checkpoint_plants(filepath):
    class_to_idx_plants = {'Apple': 0, 'Blueberry': 1, 'Cherry': 2, 'Corn': 3, 'Grape': 4, 'Orange': 5, 'Peach': 6, 'Pepper,': 7, 'Potato': 8, 'Raspberry': 9, 'Soybean': 10, 'Squash': 11, 'Strawberry': 12, 'Tomato': 13}
    idx_to_class = {v: k for k, v in class_to_idx_plants.items()}
    checkpoint = torch.load(filepath)
    resnet_model_plants = models.resnet152()
    layer2 = nn.Sequential(*list(resnet_model_plants.children())[:-4])
    layer2.add_module('Shuffle Block1', ShuffleNetV2(32, 32, 512, model_scale=2.0))
    layer2.add_module('fc', nn.Sequential(OrderedDict([
        ('fc1', nn.Linear(2048, 512)),
        ('relu', nn.ReLU()),
        ('dropout', nn.Dropout(p=0.2)),
        ('fc2', nn.Linear(512, 14)),
        ('output', nn.LogSoftmax(dim=1))
    ])))

    model_plants = layer2
    model_plants.load_state_dict(checkpoint['state_dict'])
    return model_plants, idx_to_class



def process_image(image):
    ''' Scales, crops, and normalizes a PIL image for a PyTorch model,
        returns an Numpy array
    '''

    # Process a PIL image for use in a PyTorch model
    size = 256, 256
    image.thumbnail(size, Image.ANTIALIAS)
    image = image.crop((128 - 112, 128 - 112, 128 + 112, 128 + 112))
    npImage = np.array(image)
    npImage = npImage / 255.

    imgA = npImage[:, :, 0]
    imgB = npImage[:, :, 1]
    imgC = npImage[:, :, 2]

    imgA = (imgA - 0.485) / (0.229)
    imgB = (imgB - 0.456) / (0.224)
    imgC = (imgC - 0.406) / (0.225)

    npImage[:, :, 0] = imgA
    npImage[:, :, 1] = imgB
    npImage[:, :, 2] = imgC

    npImage = np.transpose(npImage, (2, 0, 1))

    return npImage

def predict(image_path, model_plants, classes_plants, model_disease, classes_disease, topk=5):
    ''' Predict the class (or classes) of an image using a trained deep learning model.
    '''

    # Implement the code to predict the class from an image file
    image = torch.FloatTensor([process_image(Image.open(image_path))])

    model_plants.eval()
    output_plants = model_plants.forward(Variable(image))
    pobabilities_plants = torch.exp(output_plants).data.numpy()[0]
    top_idx_plants = np.argsort(pobabilities_plants)[-topk:][::-1]
    top_class_plants = [classes_plants[x] for x in top_idx_plants]
    print(top_class_plants)
    top_probability_plants = [pobabilities_plants[t] for t in top_idx_plants]
    print(top_probability_plants)

    model_disease.eval()
    output_diseases = model_disease.forward(Variable(image))
    pobabilities_diseases = torch.exp(output_diseases).data.numpy()[0]
    top_idx_diseases = np.argsort(pobabilities_diseases)[-topk:][::-1]
    top_class_diseases = [classes_disease[x] for x in top_idx_diseases]
    print(top_class_diseases)
    top_probability_diseases = [pobabilities_diseases[t] for t in top_idx_diseases]
    print(top_probability_diseases)

    return top_class_plants[0], top_probability_plants[0], top_class_diseases[0], top_probability_diseases[0]
    # return "Plant: %s | p= %.3f ; Disease: %s | p= %.3f" % (top_class_plants[0], top_probability_plants[0], top_class_diseases[0], top_probability_diseases[0])


def predict_tensor(image_tensor, model_plants, model_disease, topk=5):
    ''' Predict the class (or classes) of an image using a trained deep learning model.
    '''

    # # Implement the code to predict the class from an image file
    # image = torch.FloatTensor([process_image(Image.open(image_path))])

    model_plants.eval()
    output_plants = model_plants.forward(Variable(image_tensor))
    # pobabilities_plants = torch.exp(output_plants).data.numpy()[0]
    # top_idx_plants = np.argsort(pobabilities_plants)[-topk:][::-1]
    # top_class_plants = [classes_plants[x] for x in top_idx_plants]
    # print(top_class_plants)
    # top_probability_plants = [pobabilities_plants[t] for t in top_idx_plants]
    # print(top_probability_plants)

    model_disease.eval()
    output_diseases = model_disease.forward(Variable(image_tensor))
    pobabilities_diseases = torch.exp(output_diseases).data.numpy()[0]
    top_idx_diseases = np.argsort(pobabilities_diseases)[-topk:][::-1]
    # top_class_diseases = [classes_disease[x] for x in top_idx_diseases]
    # print(top_class_diseases)
    # top_probability_diseases = [pobabilities_diseases[t] for t in top_idx_diseases]
    # print(top_probability_diseases)

    return output_plants, top_idx_diseases


