import cv2
import pandas as pd
from ultralytics import YOLO
import cvzone
from tracker import*  # Ensure the Tracker class is defined correctly in tracker.py
import numpy as np
import urllib.request
import requests
import threading
import time  # Import the time module for delay

# URLs of the ESP32-CAM streams
urls = ['http://172.20.10.2/cam-hi.jpg', 'http://172.20.10.3/cam-hi.jpg']

# Firebase API base URL

firebase_base_url = "https://esp32cam-da86d-default-rtdb.asia-southeast1.firebasedatabase.app"

# Load the YOLO model
model = YOLO('yolov8s.pt')

# Open the 'coco.txt' file containing class names and read its content
with open("coco.txt", "r") as my_file:
    class_list = my_file.read().split("\n")  # Split the content by newline to get a list of class names

# Initialize counters and trackers
tracker = Tracker()

# Function to send detection data to Firebase
def send_to_firebase(firebase_base_url, firebase_path, data):
    """Sends data to Firebase Realtime Database using PUT."""
    firebase_url = f"{firebase_base_url}/{firebase_path}.json"
    response = requests.put(firebase_url, json=data)
    if response.status_code == 200:
        print(f"Data updated in Firebase successfully: {data}")
    else:
        print(f"Failed to update data in Firebase: {response.status_code}, {response.text}")

# Function to process a single camera stream
def process_camera(camera_index):
    url = urls[camera_index]
    firebase_path = f"persimpangan{camera_index + 1}"
    car_count = 0  # Counter for cars detected

    while True:
        try:
            # Fetch the image from the ESP32-CAM
            img_resp = urllib.request.urlopen(url)

        
            imgnp = np.array(bytearray(img_resp.read()), dtype=np.uint8)
            frame = cv2.imdecode(imgnp, -1)

            if frame is None or frame.size == 0:
                print(f"Invalid image from camera {camera_index + 1}")
                continue

            frame = cv2.resize(frame, (640, 360))  # Resize to a smaller frame size

            # Predict objects in the frame using YOLO model
            results = model.predict(frame)
            detections = results[0].boxes.data
            px = pd.DataFrame(detections).astype("float")

            # Initialize list to store bounding boxes for cars
            cars = []

            # Iterate over detection results and categorize them
            for index, row in px.iterrows():
                x1 = int(row[0])
                y1 = int(row[1])
                x2 = int(row[2])
                y2 = int(row[3])
                d = int(row[5])
                c = class_list[
                    d]
                if 'car' in c:
                    cars.append([x1, y1, x2, y2])

            # Update tracker for cars
            cars_boxes = tracker.update(cars)

            # Annotate the frame and count cars
            for bbox in cars_boxes:
                cv2.rectangle(frame, (bbox[0], bbox[1]), (bbox[2], bbox[3]), (255, 0, 255), 2)
                cvzone.putTextRect(frame, f'{bbox[4]}', (bbox[0], bbox[1]), 1, 1)
                car_count += 1

            # Display the smaller frame
            cv2.imshow(f"Camera {camera_index + 1}", frame)

            # Send car count data to Firebase
            data = {
                "car_count": car_count,
                "timestamp": time.time()
            }
            send_to_firebase(firebase_base_url, firebase_path, data)

            # Reset car count after sending
            car_count = 0

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

            time.sleep(10)  # Delay before next frame

        except Exception as e:
            print(f"Error processing camera {camera_index + 1}: {e}")

# Start threads for each camera
threads = []
for i in range(len(urls)):
    thread = threading.Thread(target=process_camera, args=(i,))
    threads.append(thread)
    thread.start()

# Wait for all threads to complete
for thread in threads:
    thread.join()

cv2.destroyAllWindows()
