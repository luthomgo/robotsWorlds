#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Move the script to /usr/local/bin if it's not already there
SCRIPT_NAME=$(basename "$0")
TARGET_DIR="/usr/local/bin"
TARGET_PATH="$TARGET_DIR/$SCRIPT_NAME"

if [[ "$0" != "$TARGET_PATH" ]]; then
    echo "Moving script to $TARGET_DIR..."
    sudo mv "$0" "$TARGET_PATH"
    sudo chmod +x "$TARGET_PATH"
    echo "Script moved to $TARGET_PATH. Run the script again from any directory using: $SCRIPT_NAME"
    exit 0
fi

# Check if Maven is installed
if command_exists mvn; then
    echo "Maven is already installed"
else
    # Update the package list and install Maven
    sudo apt update
    sudo apt install -y maven
fi

# Navigate to the project directory
cd /home/wtc/Documents/java/java_work/r/cpt10_robot_worlds || { echo "Directory not found"; exit 1; }

# Clean the Maven project
mvn clean

# Check if the jar file already exists
JAR_FILE="target/CPT_10_Robot_Worlds-1.0-SNAPSHOT-jar-with-dependencies.jar"
if [[ -f "$JAR_FILE" ]]; then
    echo "Jar file already exists. Skipping packaging."
else
    # Package the project and skip tests
    mvn package -DskipTests

    # Verify if the JAR file was created successfully
    if [[ ! -f "$JAR_FILE" ]]; then
        echo "Packaging failed. Exiting."
        exit 1
    fi
fi

# Navigate to the target directory
cd target || { echo "Target directory not found"; exit 1; }

# Run the jar file with arguments
java -jar CPT_10_Robot_Worlds-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"

# Clear the terminal and print the welcome message
clear
echo "Welcome to Robot Worlds"
