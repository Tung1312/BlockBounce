![](src/main/resources/assets/block_bounce.png)

- **Java 17 trở lên** - [Tải OpenJDK](https://adoptium.net/)
- **Git** - [Tải Git](https://git-scm.com/downloads)

### 1. Clone Repository

```bash
git clone https://github.com/your-username/BlockBounce.git
cd BlockBounce
```

### 2. Build

```cmd
.\gradlew build
```

### 3. Run

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

## Công nghệ sử dụng

- **Java 17+** - Ngôn ngữ lập trình
- **FXGL 17.3** - Framework game engine
- **JavaFX** - Framework giao diện và đồ họa
- **Gradle** - Công cụ tự động build

### Các task Gradle có sẵn

- `./gradlew build` - Compile và build dự án
- `./gradlew run` - Chạy game
- `./gradlew test` - Chạy unit tests
- `./gradlew clean` - Xóa các file build
- `./gradlew tasks` - Xem tất cả tasks có sẵn