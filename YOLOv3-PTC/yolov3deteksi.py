import numpy as np
import cv2
import time

# Configuration for YOLO
confidenceThreshold = 0.5
NMSThreshold = 0.3

# Paths to YOLO configuration and weights files
modelConfiguration = 'cfg/yolov3.cfg'
modelWeights = 'yolov3.weights'

# Labels for the object classes (from coco.names)
labelsPath = 'coco.names'
labels = open(labelsPath).read().strip().split('\n')

# Set random colors for each class label
np.random.seed(10)
COLORS = np.random.randint(0, 255, size=(len(labels), 3), dtype="uint8")

# Load the YOLO network
net = cv2.dnn.readNetFromDarknet(modelConfiguration, modelWeights)

# Get YOLO output layers
outputLayer = net.getLayerNames()
outputLayer = [outputLayer[i - 1] for i in net.getUnconnectedOutLayers()]

# Video input and output
video = cv2.VideoCapture('tf.mp4')  # Change the input file name if needed
writer = None
(W, H) = (None, None)

# Initialize video writer for saving the result
fourcc = cv2.VideoWriter_fourcc(*'mp4v')
writer = cv2.VideoWriter('hasildeteksi.mp4', fourcc, 30, (640, 480), True)

start_time = time.time()
count = 0

# Define the classes you want to detect
target_classes = ["car", "bus", "truck"]

# Initialize the total car count
total_car_count = 0  # Variable to track the total car count across all frames

while True:
    (ret, frame) = video.read()
    if not ret:
        break

    # Check elapsed time and stop processing after 30 seconds
    elapsed_time = time.time() - start_time
    if elapsed_time > 40:
        print("Stopping video processing after 30 seconds.")
        break

    if W is None or H is None:
        (H, W) = frame.shape[:2]

    # Prepare the frame for YOLO detection
    blob = cv2.dnn.blobFromImage(frame, 1 / 255.0, (416, 416), swapRB=True, crop=False)
    net.setInput(blob)
    layersOutputs = net.forward(outputLayer)

    boxes = []
    confidences = []
    classIDs = []

    # Process the detections
    for output in layersOutputs:
        for detection in output:
            scores = detection[5:]
            classID = np.argmax(scores)
            confidence = scores[classID]
            if confidence > confidenceThreshold and labels[classID] in target_classes:
                box = detection[0:4] * np.array([W, H, W, H])
                (centerX, centerY, width, height) = box.astype('int')
                x = int(centerX - (width / 2))
                y = int(centerY - (height / 2))

                boxes.append([x, y, int(width), int(height)])
                confidences.append(float(confidence))
                classIDs.append(classID)

    # Apply Non-Maximum Suppression (NMS)
    detectionNMS = cv2.dnn.NMSBoxes(boxes, confidences, confidenceThreshold, NMSThreshold)

    # Initialize counts for this frame
    car_count = 0
    bus_count = 0
    truck_count = 0

    if len(detectionNMS) > 0:
        for i in detectionNMS.flatten():
            (x, y) = (boxes[i][0], boxes[i][1])
            (w, h) = (boxes[i][2], boxes[i][3])

            # Draw bounding box and label on the frame
            color = [int(c) for c in COLORS[classIDs[i]]]
            cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)
            text = '{}: {:.4f}'.format(labels[classIDs[i]], confidences[i])
            cv2.putText(frame, text, (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

            # Count cars, buses, and trucks
            if labels[classIDs[i]] == "car":
                car_count += 1
            elif labels[classIDs[i]] == "bus":
                bus_count += 1
            elif labels[classIDs[i]] == "truck":
                truck_count += 1

    # Output vehicle counts to the console
    print(f"Frame {count + 1}: Cars: {car_count}, Buses: {bus_count}, Trucks: {truck_count}")

    # Add the current frame's car count to the total car count
    total_car_count += car_count  # Update total car count

    # Write the frame with detections to the output video
    if writer is not None:
        writer.write(frame)
        print(f"Writing frame {count + 1}")
        count += 1

    # Display the frame with detections in a window
    cv2.imshow("YOLO Object Detection", frame)

    # Exit when 'Esc' is pressed
    if cv2.waitKey(1) & 0xFF == 27:
        break

# Print the total car count after processing all frames
print(f"Total cars detected: {total_car_count}")

# Release the video capture and writer objects
writer.release()
video.release()
cv2.destroyAllWindows()
