# #!/bin/bash

# # Variables
IMAGE="razataiab2511/ubuntu-file-storage"
VOLUME_NAME="docker_storage_volume"  # Docker volume name
CONTAINER_NAME="file-storage"  # Set your specific container name here

# # Pull the latest image from Docker Hub
docker pull $IMAGE

# # Stop and remove any existing container with the same name
docker stop $CONTAINER_NAME 2>/dev/null
docker rm $CONTAINER_NAME 2>/dev/null

# # Create a Docker volume if it doesn't exist
docker volume create $VOLUME_NAME

# # Run the Docker container with a volume
docker run -it -v $VOLUME_NAME:/home/razataiab/storage --name $CONTAINER_NAME $IMAGE
