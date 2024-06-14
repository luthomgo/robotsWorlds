#!/bin/bash

command_exists() {
    command -v "$1" >/dev/null 2>&1
}

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

if command_exists mvn; then
    echo "Maven is already installed"
else

    sudo apt update
    sudo apt install -y maven
fi

cd cpt10_robot_worlds-main/ || { echo "Directory not found"; exit 1; }

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

cd target || { echo "Target directory not found"; exit 1; }

clear
echo "Welcome to Robot Worlds"
java -jar CPT_10_Robot_Worlds-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"