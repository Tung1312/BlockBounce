- **JDK 21** - [Download](https://release-assets.githubusercontent.com/github-production-release-asset/602574963/021cb48d-b0ea-4eb5-985c-10df3f4e28ad?sp=r&sv=2018-11-09&sr=b&spr=https&se=2025-10-02T09%3A30%3A15Z&rscd=attachment%3B+filename%3DOpenJDK21U-jdk_x64_windows_hotspot_21.0.8_9.msi&rsct=application%2Foctet-stream&skoid=96c2d410-5711-43a1-aedd-ab1947aa7ab0&sktid=398a6654-997b-47e9-b12b-9515b896b4de&skt=2025-10-02T08%3A30%3A14Z&ske=2025-10-02T09%3A30%3A15Z&sks=b&skv=2018-11-09&sig=DB%2BYxJyE6YvQlMZOSMhpcjiqkeRJA3Nf5gIj9EQYXII%3D&jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmVsZWFzZS1hc3NldHMuZ2l0aHVidXNlcmNvbnRlbnQuY29tIiwia2V5Ijoia2V5MSIsImV4cCI6MTc1OTM5NTMyOCwibmJmIjoxNzU5Mzk1MDI4LCJwYXRoIjoicmVsZWFzZWFzc2V0cHJvZHVjdGlvbi5ibG9iLmNvcmUud2luZG93cy5uZXQifQ.TLf0c_498Q9C6XqQVyGeVd5dIUcu3_uvS0MQXB9P590&response-content-disposition=attachment%3B%20filename%3DOpenJDK21U-jdk_x64_windows_hotspot_21.0.8_9.msi&response-content-type=application%2Foctet-stream)
- **FXGL Framework** - [Github](https://github.com/AlmasB/FXGL) - Based on [JavaFX](https://openjfx.io/)

**1. Clone Repository**

```bash
git clone https://github.com/your-username/BlockBounce.git
cd BlockBounce
```

**2. Build**

```cmd
.\gradlew build
```

**3. Run**

```cmd
.\gradlew run
```

## Project structure

```
BlockBounce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/birb_birb/blockbounce/
│   │   │       ├── constants/
│   │   │       │   ├── EntityType.java
│   │   │       │   ├── GameConstants.java
│   │   │       │   └── GameMode.java
│   │   │       ├── core/
│   │   │       │   ├── gamemode/
│   │   │       │   │   ├── score/
│   │   │       │   │   │   └── ScoreModeGame.java
│   │   │       │   │   ├── story/
│   │   │       │   │   │   └── StoryModeGame.java
│   │   │       │   │   └── versus/
│   │   │       │   │       ├── Playfield.java
│   │   │       │   │       ├── PlayfieldFactory.java
│   │   │       │   │       └── VersusModeGame.java
│   │   │       │   ├── BlockBounceApp.java
│   │   │       │   ├── GameFactory.java
│   │   │       │   └── GameManager.java
│   │   │       ├── entities/
│   │   │       │   ├── BallComponent.java
│   │   │       │   ├── BrickComponent.java
│   │   │       │   ├── PaddleComponent.java
│   │   │       │   ├── PowerUpComponent.java
│   │   │       │   └── WallComponent.java
│   │   │       ├── ui/
│   │   │       │   ├── menus/
│   │   │       │   │   ├── MainMenu.java
│   │   │       │   │   ├── ScoreModeMenu.java
│   │   │       │   │   ├── StoryModeMenu.java
│   │   │       │   │   └── VersusModeMenu.java
│   │   │       │   ├── GameplayButtons.java
│   │   │       │   └── HighScoreInput.java
│   │   │       └── utils/
│   │   │           ├── ButtonManager.java
│   │   │           ├── CursorManager.java
│   │   │           ├── FontManager.java
│   │   │           ├── MenuManager.java
│   │   │           ├── SoundManager.java
│   │   │           ├── TextureManager.java
│   │   │           ├── physics/
│   │   │           │   └── BallPhysics.java
│   │   │           ├── saveload/
│   │   │           │   ├── SaveData.java
│   │   │           │   ├── SaveGameManager.java
│   │   │           │   └── StateCapture.java
│   │   │           ├── tracker/
│   │   │           │   └── KeyTracker.java
│   │   │           └── highscore/
│   │   │               ├── HighScore.java
│   │   │               └── HighScoreManager.java
│   │   └── resources/
│   │       └── assets/
│   └── test/
│       └── java/
├── build.gradle
├── settings.gradle
└── gradlew.bat
```

### Available Gradle tasks

- `gradlew.bat build` - Compile and build the project
- `gradlew.bat run` - Run the game
- `gradlew.bat test` - Run unit tests
- `gradlew.bat clean` - Remove build files
- `gradlew.bat tasks` - List all available tasks
