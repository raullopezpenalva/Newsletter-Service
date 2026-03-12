!# /bib/bash
# This script is used to start the application in development mode using Docker Compose.
# It will build the Docker images and start the containers defined in the docker-compose.yml file.

# Build the Docker images
echo "Building Docker images..."
docker compose build --no-cache

# Start the containers
echo "Starting Docker containers..."
docker compose up -d

# logs docker compose logs -f
echo "Entering to Docker Compose logs..."
docker compose logs -f testnewsletter_service

# To stop the containers, you can use:
echo "Stopping Docker containers..."
docker compose down