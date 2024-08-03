Cloud-Based File Storage

This application allows multiple users to store and retrieve their files in a Docker container. The latest image is pulled from Docker Hub each time the application is run, ensuring users have access to the most up-to-date version of their data.

Prerequisites

Before you begin, ensure you have the following software installed on your machine:

Docker
Java
JavaFX
Maven
Google Cloud SDK (Optional): If you intend to interact with Google Cloud, such as pushing Docker images to Google Container Registry, install the Google Cloud SDK.

Installation

Clone the Repository

Build the Project:
mvn clean package

Ensure that the Docker daemon is running.

Running the Application
The application needs to be run in two separate terminals:

Terminal 1: Run the Docker setup script to pull the latest Docker image and start the container.
./app_run.sh

This script will:
Pull the latest Docker image from Docker Hub.
Start a Docker container named file-storage using the latest image.
Mount a Docker volume for persistent storage of user files.

Terminal 2: Run the JavaFX application using Maven.
mvn clean javafx:run
This command will:

Clean any previous builds.
When you close the application, the app_exit.sh script will be automatically executed to handle any necessary cleanup tasks, such as pushing changes back to Docker Hub.

Contributions are welcome! Feel free to submit a pull request or open an issue on GitHub.

Contact

For access to docker hub repository and any questions, contact razataiab25.11@gmail.com.
