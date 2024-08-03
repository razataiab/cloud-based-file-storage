#!/bin/bash

# Variables
IMAGE="razataiab2511/ubuntu-file-storage"  # Docker Hub image name
CONTAINER_NAME="file-storage"  # Container name
NEW_TAG="latest"  # Tag for the new image, use 'latest' to override

# Stop the running container
docker stop $CONTAINER_NAME

# Commit the current state of the container as a new image
docker commit $CONTAINER_NAME $IMAGE:$NEW_TAG

# Push the newly created image to Docker Hub
docker push $IMAGE:$NEW_TAG

# Remove the stopped container
docker rm $CONTAINER_NAME

echo "Docker image has been updated and pushed to Docker Hub."
