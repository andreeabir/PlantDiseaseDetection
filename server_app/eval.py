from plant_disease_recognition import *
from torchvision import datasets, transforms, models
from tqdm import trange
import torch

checkpoint_plants_path = '../checkpoints/test/plants_checkpoint_e16.pth'
checkpoint_disease_path = '../checkpoints/test/disease_checkpoint_e16.pth'

model_plants, classes_plants = load_checkpoint_plants(checkpoint_plants_path)
model_disease, classes_disease = load_checkpoint_diseases(checkpoint_disease_path)

data_dir = 'D:/Facultate/IV/Licenta/Resurse/datasets/PlantVillage-Dataset-master_my/raw/color/val'

data_transforms = {
    'test': transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
}

batch_size = 8
image_dataset = datasets.ImageFolder(data_dir, data_transforms['test'])
dataloader= torch.utils.data.DataLoader(image_dataset, batch_size=batch_size,
                                             shuffle=True, num_workers=0)
dataset_size = len(image_dataset)
print(dataset_size)
class_names = image_dataset.classes
print(class_names)
print(dataloader.dataset.class_to_idx)

# image_path = '../../Resurse/datasets/PlantVillage-Dataset-master_my/raw/color/test/Apple___Apple_scab/e3509da1-332c-4e43-977a-c57978ff6a38___FREC_Scab 3219.JPG'
# image = torch.FloatTensor([process_image(Image.open(image_path))])
# out_plant, out_disease = predict_tensor(image, model_plants, model_disease)
# print(out_plant, out_disease)

class_plants_dataset = {0: 0, 1: 0, 2: 0, 3: 0, 4: 1, 5: 2, 6: 2, 7: 3, 8: 3, 9: 3, 10: 3, 11: 4, 12: 4, 13: 4, 14: 4, 15: 5, 16: 6, 17: 6, 18: 7, 19: 7, 20: 8, 21: 8, 22: 8, 23: 9, 24: 10, 25: 11, 26: 12, 27: 12, 28: 13, 29: 13, 30: 13, 31: 13, 32: 13, 33: 13, 34: 13, 35: 13, 36: 13, 37: 13}
class_plants_to_disease = {0: [0, 1, 2, 3], 1: [4], 2:[5, 6], 3:[8, 9, 10], 4:[11, 12, 13, 14], 5:[15], 6:[16, 17], 7:[18,19],
                           8: [20, 21, 22], 9:[23], 10:[24], 11:[25], 12:[26, 27], 13:[28, 29, 30, 31, 32, 33, 34, 35, 36, 37]}
# determin ce labels din deasese coresp cu label plant
# iau label d care are prob cea mai mare
def eval():
    model_plants.cuda()
    model_disease.cuda()
    model_plants.eval()
    model_disease.eval()

    acc_plants = 0.0
    acc_disease = 0.0
    nr_batch = 0
    batches = int(len(dataloader.dataset) / batch_size)
    print(batches)
    with trange(batches) as t:
        for inputs, labels in dataloader:
            inputs, labels = inputs.cuda(), labels.cuda()

            labels_plants = []
            for l in labels:
                labels_plants.append(class_plants_dataset[l.item()])
            labels_plants = torch.tensor(np.array(labels_plants)).cuda()


            outputs_plants = model_plants(inputs)
            _, preds_plants = torch.max(outputs_plants, 1)

            dict_diseases = {}
            i = 0
            for l in preds_plants:
                l_d = class_plants_to_disease[l.item()]
                dict_diseases[i] = l_d
                i += 1

            # print(torch.sum(preds_plants.data == labels_plants.data.long()))
            outputs_disease = model_disease(inputs)
            pobabilities_diseases = torch.exp(outputs_disease.cpu()).data.numpy()[0]

            preds_disease = []
            for i in dict_diseases:
                diseases = dict_diseases[i]
                pobabilities_diseases = torch.exp(outputs_disease.cpu()).data.numpy()[i]
                max = 0
                disease_out = 0
                for d in diseases:
                    if pobabilities_diseases[d] >= max:
                        max = pobabilities_diseases[d]
                        disease_out = d
                # print(i, disease_out)
                preds_disease.append(disease_out)
            # preds_disease = torch.LongTensor(preds_disease).cuda()

            _, preds_disease = torch.max(outputs_disease, 1)

            acc_plants += torch.sum(preds_plants.data == labels_plants.data.long())
            acc_disease += torch.sum(preds_disease == labels.data)

            nr_batch += 1

            epoch_acc_plants = acc_plants.double() / (nr_batch * inputs.size(0))
            epoch_acc_disease = acc_disease.double() / (nr_batch * inputs.size(0))
            t.set_postfix(acc_plants=epoch_acc_plants, acc_disease=epoch_acc_disease.item())
            t.update()
    print('acc_plants', acc_plants.double() / dataset_size)
    print('acc_disease', acc_disease.double() / dataset_size)

eval()


