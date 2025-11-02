<p align="center">
  <img src="src/main/resources/docs/block_bounce.png" alt="Installation" />
</p>


<p align="center">
  <img src="src/main/resources/docs/installation.png" alt="Installation" />
</p>

### [Download JDK 21](https://release-assets.githubusercontent.com/github-production-release-asset/602574963/021cb48d-b0ea-4eb5-985c-10df3f4e28ad?sp=r&sv=2018-11-09&sr=b&spr=https&se=2025-10-02T09%3A30%3A15Z&rscd=attachment%3B+filename%3DOpenJDK21U-jdk_x64_windows_hotspot_21.0.8_9.msi&rsct=application%2Foctet-stream&skoid=96c2d410-5711-43a1-aedd-ab1947aa7ab0&sktid=398a6654-997b-47e9-b12b-9515b896b4de&skt=2025-10-02T08%3A30%3A14Z&ske=2025-10-02T09%3A30%3A15Z&sks=b&skv=2018-11-09&sig=DB%2BYxJyE6YvQlMZOSMhpcjiqkeRJA3Nf5gIj9EQYXII%3D&jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmVsZWFzZS1hc3NldHMuZ2l0aHVidXNlcmNvbnRlbnQuY29tIiwia2V5Ijoia2V5MSIsImV4cCI6MTc1OTM5NTMyOCwibmJmIjoxNzU5Mzk1MDI4LCJwYXRoIjoicmVsZWFzZWFzc2V0cHJvZHVjdGlvbi5ibG9iLmNvcmUud2luZG93cy5uZXQifQ.TLf0c_498Q9C6XqQVyGeVd5dIUcu3_uvS0MQXB9P590&response-content-disposition=attachment%3B%20filename%3DOpenJDK21U-jdk_x64_windows_hotspot_21.0.8_9.msi&response-content-type=application%2Foctet-stream)

```bash
// Clone dự án này về máy
git clone https://github.com/your-username/BlockBounce.git

// Di chuyển đến thư mục chứa dự án vừa clone
cd BlockBounce

// Lệnh để build game
.\gradlew build

// Lệnh để run game
.\gradlew run
```

<p align="center">
  <img src="src/main/resources/docs/howtoplay.png" alt="How to play" />
</p>

- **Di chuyển:**\
![left.png](src/main/resources/docs/buttons/left.png)![right.png](src/main/resources/docs/buttons/right.png)
hoặc
![a.png](src/main/resources/docs/buttons/a.png)![d.png](src/main/resources/docs/buttons/d.png)
- **Phóng bóng:**\
![space.png](src/main/resources/docs/buttons/space.png)
- **Lưu game:**\
![s.png](src/main/resources/docs/buttons/s.png) (Score Mode only)


- **Các elements trong game:**

| Texture                                                                                   | Element              | Mô tả                                                         |
|-------------------------------------------------------------------------------------------|----------------------|---------------------------------------------------------------|
| <img src="src/main/resources/assets/textures/entities/ball.png" width="28" />             | Bóng tuyết (Ball)    | Bóng tuyết dùng để phá gạch. Bật lại khi chạm tường/pít tông. |
| <img src="src/main/resources/assets/textures/entities/paddle.png" width="56" />           | Pít tông (Paddle)    | Vật thể người chơi điều khiển sang trái/phải để đỡ bóng.      |
| <img src="src/main/resources/assets/textures/entities/brick.png" width="28" />            | Khối gỗ (Brick)      | Khối gạch là mục tiêu chính cần phá để nhận điểm hoặc qua màn. |
| <img src="src/main/resources/assets/textures/entities/powerup/multiply.png" width="28" /> | Vật phẩm Double Ball | Nhân đôi số lượng bóng trên màn hình.                       |
| <img src="src/main/resources/assets/textures/entities/powerup/speed.png" width="28" />    | Vật phẩm Fast Ball   | Tăng tốc độ của bóng trong 8 giây.                            |
| <img src="src/main/resources/assets/textures/entities/powerup/life.png" width="28" />     | Vật phẩm Extra Life  | Tăng thêm 1 mạng cho người chơi.                              |
| <img src="src/main/resources/assets/textures/entities/powerup/double.png" width="28" />   | Vật phẩm Double Points | Nhân đôi điểm nhận được trong một thời gian ngắn.            |
| <img src="src/main/resources/assets/textures/entities/powerup/shrink.png" width="28" />   | Vật phẩm Small Paddle | Thu nhỏ pít tông 40% trong 10 giây.                           |

## I. Story Mode
Chế độ chơi theo tiến trình đặt trước, bao gồm 8 màn chơi khác biệt.
Người chơi bắt đầu ở màn đầu tiên và mở khóa các màn chơi tiếp theo, đồng thời được giới thiệu thêm các cơ chế của 
![story_mode_menu_docs.png](src/main/resources/docs/story_mode_menu_docs.png)
![story_mode_game_docs.png](src/main/resources/docs/story_mode_game_docs.png)

## II. Score Mode
Chế độ chơi endless tính điểm, bản đồ sẽ được tái tạo khi người chơi phá hết khối gạch.
Chế độ có bảng xếp hạng (local) để so sánh điểm các màn chơi của những người chơi khác nhau.
![score_mode_menu_docs.png](src/main/resources/docs/score_mode_menu_docs.png)
![score_mode_game_docs.png](src/main/resources/docs/score_mode_game_docs.png)
Người chơi có thể lưu lại màn chơi của mình sao đó load lại để tiếp tục chơi tiếp ở lần tiếp theo.

## III. Versus Mode
Chế độ chơi 2 người để bạn có thể cùng chơi game với 1 người khác trên cùng 1 màn hình.
![versus_mode_game_docs.png](src/main/resources/docs/versus_mode_game_docs.png)
- **Người chơi 1 (bên trái):**\
![a.png](src/main/resources/docs/buttons/a.png)
![d.png](src/main/resources/docs/buttons/d.png)
![space.png](src/main/resources/docs/buttons/space.png)
- **Người chơi 2 (bên phải)**\
![left.png](src/main/resources/docs/buttons/left.png)
![right.png](src/main/resources/docs/buttons/right.png)
![enter.png](src/main/resources/docs/buttons/enter.png)

<p align="center">
  <img src="src/main/resources/docs/projectstructure.png" alt="Project structure" />
</p>

<details>
<summary>Cấu trúc thư mục</summary>

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

</details>

<details>
<summary>Sơ đồ lớp</summary>

![java.png](src/main/resources/docs/java.png)
</details>