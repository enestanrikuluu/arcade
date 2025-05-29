# Knowledge Siege

A Java game created for COMP 132: Advanced Programming Spring 2025 project.

## Overview

"Knowledge Siege" is an educational Java game where players navigate through three increasingly challenging levels, facing off against academic adversaries: Section Leaders (SLs), Teaching Assistants (TAs), and Professors. These adversaries challenge the player by shooting either questions or valuable information. The player's goal is to collect information to earn points while avoiding questions that cause points reduction.

## Game Features

- Three progressively challenging levels with different enemy types
- User authentication system with account creation
- Profile picture customization
- Real-time gameplay with various enemy behaviors
- Educational content related to Java and OOP concepts
- Score tracking and high score table
- Game logging for player actions

## How to Run

1. Compile the Java files
2. Run the main class: `java siege.KnowledgeSiege`

## Game Controls

- **Left Arrow**: Move player left
- **Right Arrow**: Move player right
- **H Key**: Toggle player visibility

## Requirements

- Java JDK 8 or higher
- Swing and AWT libraries (included in standard JDK)

## Game Structure

The game is structured using Model-View-Controller (MVC) pattern:

- **Model**: Contains the game logic, objects, and state
- **View**: Contains the UI components and rendering
- **Controller**: Handles user input and coordinates between Model and View

## File Structure

- **src/siege/model/**: Game objects and logic
- **src/siege/view/**: UI components and screens
- **src/siege/controller/**: Game controllers and input handlers
- **src/siege/io/**: File and logging utilities
- **src/siege/util/**: Utility classes
- **src/resources/**: Game resources (images, data files)

## Project Implementation

This project demonstrates core concepts of Object-Oriented Programming including:

- Inheritance and polymorphism
- Encapsulation and information hiding
- Interfaces and abstract classes
- Exception handling
- File I/O
- GUI development with Swing

## Credits

Created by [Your Name] for COMP 132: Advanced Programming at Koç University.
