
- **JDK 21** - [Download](https://release-assets.githubusercontent.com/github-production-release-asset/602574963/021cb48d-b0ea-4eb5-985c-10df3f4e28ad?sp=r&sv=2018-11-09&sr=b&spr=https&se=2025-10-02T03%3A54%3A42Z&rscd=attachment%3B+filename%3DOpenJDK21U-jdk_x64_windows_hotspot_21.0.8_9.msi&rsct=application%2Foctet-stream&skoid=96c2d410-5711-43a1-aedd-ab1947aa7ab0&sktid=398a6654-997b-47e9-b12b-9515b896b4de&skt=2025-10-02T02%3A54%3A23Z&ske=2025-10-02T03%3A54%3A42Z&sks=b&skv=2018-11-09&sig=f1yKqhnGCUb63MULWwld9ykgE%2BTjJGP5XFlhPEGeMsM%3D&jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmVsZWFzZS1hc3NldHMuZ2l0aHVidXNlcmNvbnRlbnQuY29tIiwia2V5Ijoia2V5MSIsImV4cCI6MTc1OTM3NDYwMSwibmJmIjoxNzU5Mzc0MzAxLCJwYXRoIjoicmVsZWFzZWFzc2V0cHJvZHVjdGlvbi5ibG9iLmNvcmUud2luZG93cy5uZXQifQ.PvqE8jusAdc6zFC5SpQwhKEEEGUcr3g9qWfl2t3bMFM&response-content-disposition=attachment%3B%20filename%3DOpenJDK21U-jdk_x64_windows_hotspot_21.0.8_9.msi&response-content-type=application%2Foctet-stream)
- **FXGL 21.1** - [Github](https://github.com/AlmasB/FXGL) - Based on [JavaFX](https://openjfx.io/)

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

## Cấu trúc dự án

```
BlockBounce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/birb_birb/blockbounce/
│   │   │       └── Main.java              # Điểm khởi động chính của ứng dụng
│   │   └── resources/
│   │       └── assets/                    # Thư mục tài nguyên game
│   │           ├── textures/              # Hình ảnh và sprites
│   │           ├── sounds/                # Hiệu ứng âm thanh
│   │           ├── music/                 # Nhạc nền
│   │           ├── fonts/                 # Font chữ tùy chỉnh
│   │           ├── ui/                    # Phần tử giao diện người dùng
│   │           └── data/                  # File dữ liệu game
│   └── test/
│       └── java/                          # Unit tests
├── gradle/
├── build.gradle                           # Cấu hình build
├── settings.gradle                        # Cài đặt dự án
├── .gitignore
└── README.md
```

### Các task Gradle

- `./gradlew build` - Compile và build dự án
- `./gradlew run` - Chạy game
- `./gradlew test` - Chạy unit tests
- `./gradlew clean` - Xóa các file build
- `./gradlew tasks` - Xem tất cả tasks có sẵn