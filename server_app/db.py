import pymongo

mylist = [{'_id': 0, 'plant': 'Apple', 'disease': 'Apple___Apple_scab', 'treatment': 'Treatment Apple__Apple_scab', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 1, 'plant': 'Apple', 'disease': 'Apple___Black_rot', 'treatment': 'Treatment Apple___Black_rot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 2, 'plant': 'Apple', 'disease': 'Apple___Cedar_apple_rust', 'treatment': 'Treatment Apple___Cedar_apple_rust', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 3, 'plant': 'Apple', 'disease': 'Apple___healthy', 'treatment': 'Treatment Apple___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 4, 'plant': 'Blueberry', 'disease': 'Blueberry___healthy', 'treatment': 'Treatment Blueberry___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 5, 'plant': 'Cherry', 'disease': 'Cherry_(including_sour)___Powdery_mildew', 'treatment': 'Treatment Cherry_(including_sour)___Powdery_mildew', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 6, 'plant': 'Cherry', 'disease': 'Cherry_(including_sour)___healthy', 'treatment': 'Treatment Cherry_(including_sour)___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 7, 'plant': 'Corn', 'disease': 'Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot', 'treatment': 'Treatment Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 8, 'plant': 'Corn', 'disease': 'Corn_(maize)___Common_rust_', 'treatment': 'Treatment Corn_(maize)___Common_rust_', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 9, 'plant': 'Corn', 'disease': 'Corn_(maize)___Northern_Leaf_Blight', 'treatment': 'Treatment Corn_(maize)___Northern_Leaf_Blight', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 10, 'plant': 'Corn', 'disease': 'Corn_(maize)___healthy', 'treatment': 'Treatment Corn_(maize)___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 11, 'plant': 'Grape', 'disease': 'Grape___Black_rot', 'treatment': 'Treatment Grape___Black_rot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 12, 'plant': 'Grape', 'disease': 'Grape___Esca_(Black_Measles)', 'treatment': 'Treatment Grape___Esca_(Black_Measles)', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 13, 'plant': 'Grape', 'disease': 'Grape___Leaf_blight_(Isariopsis_Leaf_Spot)', 'treatment': 'Treatment Grape___Leaf_blight_(Isariopsis_Leaf_Spot)', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 14, 'plant': 'Grape', 'disease': 'Grape___healthy', 'treatment': 'Treatment Grape___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 15, 'plant': 'Orange', 'disease': 'Orange___Haunglongbing_(Citrus_greening)', 'treatment': 'Treatment Orange___Haunglongbing_(Citrus_greening)', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 16, 'plant': 'Peach', 'disease': 'Peach___Bacterial_spot', 'treatment': 'Treatment Peach___Bacterial_spot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 17, 'plant': 'Peach', 'disease': 'Peach___healthy', 'treatment': 'Treatment Peach___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 18, 'plant': 'Pepper', 'disease': 'Pepper,_bell___Bacterial_spot', 'treatment': 'Treatment Pepper,_bell___Bacterial_spot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 19, 'plant': 'Pepper', 'disease': 'Pepper,_bell___healthy', 'treatment': 'Treatment Pepper,_bell___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 20, 'plant': 'Potato', 'disease': 'Potato___Early_blight', 'treatment': 'Treatment Potato___Early_blight', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 21, 'plant': 'Potato', 'disease': 'Potato___Late_blight', 'treatment': 'Treatment Potato___Late_blight', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 22, 'plant': 'Potato', 'disease': 'Potato___healthy', 'treatment': 'Treatment Potato___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 23, 'plant': 'Raspberry', 'disease': 'Raspberry___healthy', 'treatment': 'Treatment Raspberry___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 24, 'plant': 'Soybean', 'disease': 'Soybean___healthy', 'treatment': 'Treatment Soybean___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 25, 'plant': 'Squash', 'disease': 'Squash___Powdery_mildew', 'treatment': 'Treatment Squash___Powdery_mildew', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 26, 'plant': 'Strawberry', 'disease': 'Strawberry___Leaf_scorch', 'treatment': 'Treatment Strawberry___Leaf_scorch', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 27, 'plant': 'Strawberry', 'disease': 'Strawberry___healthy', 'treatment': 'Treatment Strawberry___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 28, 'plant': 'Tomato', 'disease': 'Tomato___Bacterial_spot', 'treatment': 'Treatment Tomato___Bacterial_spot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 29, 'plant': 'Tomato', 'disease': 'Tomato___Early_blight', 'treatment': 'Treatment Tomato___Early_blight', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 30, 'plant': 'Tomato', 'disease': 'Tomato___Late_blight', 'treatment': 'Treatment Tomato___Late_blight', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 31, 'plant': 'Tomato', 'disease': 'Tomato___Leaf_Mold', 'treatment': 'Treatment Tomato___Leaf_Mold', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 32, 'plant': 'Tomato', 'disease': 'Tomato___Septoria_leaf_spot', 'treatment': 'Treatment Tomato___Septoria_leaf_spot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 33, 'plant': 'Tomato', 'disease': 'Tomato___Spider_mites Two-spotted_spider_mite', 'treatment': 'Treatment Tomato___Spider_mites Two-spotted_spider_mite', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 34, 'plant': 'Tomato', 'disease': 'Tomato___Target_Spot', 'treatment': 'Treatment Tomato___Target_Spot', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 35, 'plant': 'Tomato', 'disease': 'Tomato___Tomato_Yellow_Leaf_Curl_Virus', 'treatment': 'Treatment Tomato___Tomato_Yellow_Leaf_Curl_Virus', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 36, 'plant': 'Tomato', 'disease': 'Tomato___Tomato_mosaic_virus', 'treatment': 'Treatment Tomato___Tomato_mosaic_virus', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'},
{'_id': 37, 'plant': 'Tomato', 'disease': 'Tomato___healthy', 'treatment': 'Treatment Tomato___healthy', 'Optimal conditions': 'Temp < 27, Wind < 25, No rain, Hour 6-18'}]


def init_db():
    myclient = pymongo.MongoClient("mongodb://localhost:27017")
    mydb = myclient["mydatabase"]
    mycol = mydb["plants"]
    return mycol


def insert_plants(mycol, mylist):
    mycol.insert_many(mylist)


def get_treatment(mycol, plant, disease):
    print(plant)
    print(disease)
    x = mycol.find_one({"plant": plant, "disease": disease})
    return x['treatment']

if __name__ == '__main__':
    my_col = init_db()
    entry = get_treatment(my_col, 'Apple', 'Apple__Apple_scab')
    print(entry['treatment'])

