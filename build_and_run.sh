#!/bin/bash

echo "Running Maven clean..."
mvn clean

echo "Running Maven package..."
mvn package

if [ $? -ne 0 ]; then
  echo "Maven package failed. Press Enter to exit."
  read -p ""
fi

echo "Building Docker images with Docker Compose..."
docker-compose build

if [ $? -ne 0 ]; then
  echo "Docker Compose build failed. Press Enter to exit."
  read -p ""
fi

echo "Starting services with Docker Compose..."
docker-compose up -d

# Added this command to keep the window open
read -p "Press Enter to exit"