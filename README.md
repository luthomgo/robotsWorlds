# Robot Worlds

**Robot Worlds is a client-server application for controlling robots in a virtual world. The server manages the world state and client interactions, while the client allows users to control robots and navigate the world.**

## Features

**Server-Client Architecture:** Uses a client-server architecture for managing the world and controlling robots.

**World Configuration:** Loads and saves world configurations from a file.

**Obstacle Generation:** Generates obstacles such as lakes, mountains, and pits within the world.

**Robot Control:** Allows users to launch and control robots in the world.

**Command Handling:** Handles commands such as moving robots, shooting, and repairing shields.

# Installation:
1. Download the zip from git
2. Navigate into your download directory using:

`cd Downloads`

3. Extract the folder by using:

`unzip cpt10_robot_worlds-main.zip
`
4. Navigate into your Robot Worlds directory using:

`cd cpt10_robot_worlds-main/
`
5. Make the script executable by typing in:

`chmod +x RobotWorlds.sh
`
6. Execute the script by running:

`/RobotWorlds.sh`

7. To run the server type in RobotWorlds with no arguments:

`for Example: RobotWorlds
`
8. To run the client type in RobotWorlds ip port, make sure you pass the IP address and the port.

`for Example: RobotWorlds 127.0.0.10 5055
`
## Client Usage
To run the client, use the following command with the IP address and port number of the specific server you want to connect to as arguments:

`RobotWorlds 10.200.109.255 5058
`
Note that to connect to our Robot Worlds you have to use the 5058 port. The IP adress depends on who in your party wants to run the server.


Once the command is executed, you will be connected to the RobotWorlds server. If the server has not been started, you will receive a message saying the server is not connected.

Connecting and Launching Your Robot
After the server is connected, you will be prompted to enter your name and then to launch your robot in the world.
When launching the robot, you can choose one of three types:


Sniper: Has 5 shots, 1 shield, and 10 visibility.

Tank: Has 1 shot, 5 shields, and 5 visibility.

Brad1: Has 4 shots, 2 shields, and 5 visibility.

Normy: Has 3 shots, 3 shields, and 5 visibility.

To launch a robot, specify "launch" followed by the type. For example, to launch a sniper, use:

`launch sniper
`

If no type is provided, it will launch a "Normy" by default.

### Available Commands

**After launching, you can use the following commands:**


* **Help:** Displays how to run commands and what they each do.

* **Look:** Shows the obstacles based on your visibility.

* **Forward [steps]:** Moves forward based on the number of steps given. If there's an obstacle in the way, you will be obstructed.

* **Back [steps]:** Moves back based on the number of steps given. If there's an obstacle in the way, you will be obstructed.

* **Fire:** Shoots another robot. The bullet distance and direction are based on your visibility and current direction.

* **Turn [left/right]:** Changes the robot's direction based on whether you pass left or right with it.

* **State:** Shows the state of the robot.

* **Reload:** Sets the amount of shots back to its initial state.

* **Repair:** Sets the amount of shields back to its initial state.

* **Exit:** Disconnects from the server.

# Server Usage Guide

To run the server, type RobotWorlds with no arguments in your terminal. For example:

`RobotWorlds`

You will then be prompted to configure the world.

### Configuring the World

By configuring the world, you set the following:

* **Top Left and Bottom Right Positions:** Setting the world's movement boundaries.

* **Maximum Amount of Shots:** The maximum amount of shots the players have.

* **Visibility:** The furthest steps ahead a player can see.

* **Maximum Amount of Shields:** The maximum amount of shields the player has.

* **Reload Time:** The time it takes to reload shots.

* **Repair Time:** The time it takes to repair shields.

If you have already configured the world, it will prompt you to either use the old configuration or configure the world again.

Connecting Clients and Viewing World Properties
After configuring the world, clients will be able to connect to the server.

You can then enter commands that will display properties of the world. The commands are:

* **dump:** Displays the world attributes, obstacles, and robots.
* **robots:** Displays the robots currently connected to the world.
* **quit:** Closes the server and disconnects all players connected to it.
* **Help:** Displays how to run commands and what they each do.
