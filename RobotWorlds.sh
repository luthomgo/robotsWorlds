#!/bin/bash

command_exists() {
    command -v "$1" >/dev/null 2>&1
}

SCRIPT_NAME="RobotWorlds"
TARGET_DIR="/usr/local/bin"
TARGET_PATH="$TARGET_DIR/$SCRIPT_NAME"

if [[ "$0" != "$TARGET_PATH" ]]; then
    echo "Moving script to $TARGET_DIR..."
    sudo mv "$0" "$TARGET_PATH"
    sudo chmod +x "$TARGET_PATH"
    echo "Script moved to $TARGET_PATH. Run the script again from any directory using: $SCRIPT_NAME"
    exit 0
fi

if command_exists mvn; then
    echo "Maven is already installed"
else
    sudo apt update
    sudo apt install -y maven
fi

DOWNLOADS_DIR=$(xdg-user-dir DOWNLOAD)

if [[ -d "$DOWNLOADS_DIR" ]]; then
    cd "$DOWNLOADS_DIR" || { echo "Failed to navigate to Downloads directory"; exit 1; }
else
    echo "Downloads directory not found"; exit 1;
fi

cd cpt10_robot_worlds-main || { echo "Project directory not found"; exit 1; }

mvn clean

JAR_FILE="target/CPT_10_Robot_Worlds-1.0-SNAPSHOT-jar-with-dependencies.jar"
if [[ -f "$JAR_FILE" ]]; then
    echo "Jar file already exists. Skipping packaging."
else
    mvn package -DskipTests
    if [[ ! -f "$JAR_FILE" ]]; then
        echo "Packaging failed. Exiting."
        exit 1
    fi
fi

clear
echo "Welcome to Robot Worlds"
cd target || { echo "Target directory not found"; exit 1; }
java -jar CPT_10_Robot_Worlds-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"