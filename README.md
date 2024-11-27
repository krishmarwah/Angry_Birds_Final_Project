# Angry Birds

Overview
This project is a Java-based recreation of the popular Angry Birds game, developed as part of the CSE 201 (Advanced Programming) course. The game incorporates realistic physics, interactive gameplay, and a graphical user interface (GUI) built using LibGDX.

Features
Physics Simulation: Accurate projectile motion and collision mechanics using LibGDX physics engine.
Static and Dynamic Elements:
Static structures (e.g., towers and blocks).
Dynamic entities (e.g., birds and destructible objects).
Interactive Gameplay: Drag-and-release mechanics for launching birds.
Material Properties: Different block types with unique durability (wood, stone, glass, etc.).
Score Tracking: Points awarded based on destruction and successful completion of levels.
Progressive Levels: Increasing difficulty with new challenges in higher levels.
Technologies Used
Java: Core programming language.
LibGDX: Game development framework for graphics, physics, and user interaction.
OOP Principles: Clean code structure with modular and reusable components.
Git: Version control for project collaboration and iteration.
Prerequisites
Before running the project, ensure you have the following installed:

Java Development Kit (JDK): Version 8 or later.
LibGDX: Download and set up using LibGDX Setup Tool.
IDE: IntelliJ IDEA, Eclipse, or any preferred IDE for Java development.
Installation
Clone the repository:

bash
Copy code
git clone https://github.com/krishmarwah/Angry_Birds_Final_Project
cd angry-birds-clone
Import the project into your IDE.

Set up LibGDX dependencies:

Ensure gdx.jar, gdx-backend-lwjgl.jar, and other LibGDX libraries are included in the classpath.
Run the project:

bash
Copy code
./gradlew desktop:run
or just run the Launchr Application


Gameplay Instructions
Objective: Use the slingshot to launch birds and knock down all pigs to complete the level.
Controls:
Drag the bird backwards to aim.
Release to launch the bird.

Scoring:
Destroy blocks and pigs to earn points.
The fewer birds used, the higher your score.

Winning the Game:
Clear all levels by defeating all pigs.

File Structure
src/: Contains Java source files.
Angry.Birds: Game logic and core mechanics,Physics and collision handling
java/test-Junit Testing
assets/: Game assets such as images, sounds, and fonts.
build.gradle: Build configuration for LibGDX and Java dependencies.

Following are the few Design patterns,we have inculcated in our code:-
1. Observer Pattern
Usage: Collision Handling
Example:
The setupCollisionListener() method uses a listener (ContactListener) to observe interactions (collisions) between game objects (birds, pigs, ground, etc.).
When a collision occurs, the system reacts appropriately (e.g., updates the score, spawns a new bird, or destroys an object).
2. Factory Method Pattern
Usage: Bird Creation
Example:
The createBird() method acts as a factory for creating birds based on their type (e.g., "RedAngryBird", "BlueAngryBird").
It abstracts the bird creation logic, including properties like density and restitution, allowing easy addition of new bird types.
3. State Pattern
Usage: Game Pausing and Resuming
Example:
The isPaused flag represents the game's state.
Methods like pauseGame() and resumeGame() change the game's behavior based on this state (e.g., stopping animations or timers when paused).
4. Strategy Pattern
Usage: Material Collision Handling
Example:
Classes like WoodMaterial, GlassMaterial, and ArmoredPig implement their specific collision handling logic (e.g., handleCollision()).
This allows flexibility and extensibility when introducing new materials with unique behaviors.
5. Queue (Behavioral Pattern)
Usage: Bird Queue Management
Example:
The birdQueue uses a queue structure to manage birds in a predefined sequence.
This ensures that birds are spawned in order and their turn ends before the next bird is activated.
6. Singleton Pattern
Usage: World Instance Management
Example:
The World object from Box2D acts as a singleton within the game, providing a single physics environment shared by all objects.
7. Builder Pattern
Usage: Level Creation
Example:
The createLevel() method builds the game level incrementally, placing wood blocks, glass blocks, and pigs in specific configurations.
This method encapsulates the logic for constructing complex level structures.
8. Command Pattern
Usage: Game Screen Transitions
Example:
Methods like goToWinPage() and goToLosePage() encapsulate actions to transition to different screens (WinPage and LosePage).
The commands (setScreen()) are abstracted, ensuring centralized control over screen navigation.
9. Template Method Pattern
Usage: Material Collision Behavior
Example:
The handleCollision() method in WoodMaterial and GlassMaterial follows a similar structure but is overridden with material-specific behavior.
10. Composite Pattern
Usage: Block and Pig Groups
Example:
The woodBlocks, glassBlocks, and armoredPigs lists collectively represent groups of game elements, enabling unified operations (e.g., iterating through them to check for collisions or apply game logic).




Thank You!
Efforts by:
Akshat Verma(2023065)
Krish Marwah(2023288)

