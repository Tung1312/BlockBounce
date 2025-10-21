package com.birb_birb.blockbounce.utils;

import javafx.geometry.Point2D;

/**
 * Utility class for ball physics calculations.
 * Contains methods for normalizing angles and calculating velocities.
 */
public class BallPhysics {

    // Giới hạn góc để tránh bóng rơi quá dốc (75 độ)
    private static final double MAX_VERTICAL_ANGLE = 75.0;

    // Góc tối thiểu để tránh bóng bay quá ngang (15 độ)
    private static final double MIN_VERTICAL_ANGLE = 15.0;

    // Tốc độ tối đa cho phép
    private static final double MAX_SPEED_MULTIPLIER = 1.2;

    private BallPhysics() {
        // Prevent instantiation
    }

    /**
     * Tính góc từ vận tốc hiện tại
     * @param velocityX vận tốc theo trục X
     * @param velocityY vận tốc theo trục Y
     * @return góc tính bằng độ (-180 đến 180)
     */
    public static double calculateAngle(double velocityX, double velocityY) {
        return Math.toDegrees(Math.atan2(velocityY, velocityX));
    }

    /**
     * Chuẩn hóa góc để tránh bóng rơi quá dốc hoặc bay quá ngang
     * @param angle góc hiện tại (độ)
     * @return góc đã được điều chỉnh
     */
    public static double normalizeAngle(double angle) {
        double normalizedAngle = angle;

        // Xử lý góc trong phạm vi -180 đến 180
        while (normalizedAngle > 180) normalizedAngle -= 360;
        while (normalizedAngle < -180) normalizedAngle += 360;

        double absAngle = Math.abs(normalizedAngle);

        // Xử lý góc 0° (hoàn toàn ngang)
        if (absAngle < 0.01) {
            return MIN_VERTICAL_ANGLE; // Mặc định trả về góc dương
        }

        // Nếu góc quá dốc (> 75° và < 105°), giới hạn về 75°
        if (absAngle > MAX_VERTICAL_ANGLE && absAngle < 180 - MAX_VERTICAL_ANGLE) {
            normalizedAngle = Math.signum(normalizedAngle) * MAX_VERTICAL_ANGLE;
        }

        // Nếu góc quá ngang (< 15° hoặc > 165°), đẩy về góc tối thiểu
        if (absAngle < MIN_VERTICAL_ANGLE && absAngle > 0.01) {
            normalizedAngle = Math.signum(normalizedAngle) * MIN_VERTICAL_ANGLE;
        }

        // Xử lý góc gần 180° (bay ngang về phía sau)
        if (absAngle > 180 - MIN_VERTICAL_ANGLE) {
            normalizedAngle = Math.signum(normalizedAngle) * (180 - MIN_VERTICAL_ANGLE);
        }

        return normalizedAngle;
    }

    /**
     * Tính vận tốc từ góc và tốc độ
     * @param angle góc (độ)
     * @param speed tốc độ
     * @return Point2D chứa (velocityX, velocityY)
     */
    public static Point2D calculateVelocity(double angle, double speed) {
        double radians = Math.toRadians(angle);
        return new Point2D(
            Math.cos(radians) * speed,
            Math.sin(radians) * speed
        );
    }

    /**
     * Chuẩn hóa vận tốc để đảm bảo góc hợp lý và tốc độ không vượt quá giới hạn
     * @param velocity vận tốc hiện tại
     * @param baseSpeed tốc độ cơ bản
     * @return vận tốc đã được điều chỉnh
     */
    public static Point2D normalizeVelocity(Point2D velocity, double baseSpeed) {
        // Tính góc hiện tại
        double angle = calculateAngle(velocity.getX(), velocity.getY());

        // Chuẩn hóa góc
        double normalizedAngle = normalizeAngle(angle);

        // Tính tốc độ hiện tại
        double currentSpeed = velocity.magnitude();

        // Giới hạn tốc độ tối đa
        double targetSpeed = Math.min(currentSpeed, baseSpeed * MAX_SPEED_MULTIPLIER);

        // Tạo vận tốc mới với góc đã chuẩn hóa
        return calculateVelocity(normalizedAngle, targetSpeed);
    }

    /**
     * Tính vận tốc bật lại từ paddle dựa trên vị trí va chạm
     * @param ballCenterX vị trí X tâm bóng
     * @param paddleCenterX vị trí X tâm paddle
     * @param paddleWidth độ rộng paddle
     * @param baseSpeed tốc độ cơ bản
     * @return vận tốc mới sau khi bật
     */
    public static Point2D calculatePaddleBounce(double ballCenterX, double paddleCenterX,
                                                 double paddleWidth, double baseSpeed) {
        // Tính độ lệch so với tâm paddle (-1 đến 1)
        double hitOffset = (ballCenterX - paddleCenterX) / (paddleWidth / 2);

        // Giới hạn hitOffset trong khoảng [-0.75, 0.75]
        hitOffset = Math.max(-0.75, Math.min(0.75, hitOffset));

        // Tính góc bật (±60 độ tương ứng ±π/3)
        double bounceAngle = hitOffset * Math.PI / 3;

        // Tạo vận tốc: X theo hướng offset, Y luôn hướng lên
        double newVelX = baseSpeed * Math.sin(bounceAngle);
        double newVelY = -baseSpeed * Math.cos(bounceAngle);

        // Đảm bảo vận tốc Y luôn hướng lên và đủ mạnh (tối thiểu -2.0)
        newVelY = Math.min(newVelY, -2.0);

        return new Point2D(newVelX, newVelY);
    }

    /**
     * Kiểm tra góc có hợp lệ hay không
     * @param angle góc (độ)
     * @return true nếu góc trong giới hạn cho phép
     */
    public static boolean isAngleValid(double angle) {
        double absAngle = Math.abs(angle);

        // Góc hợp lệ nếu nằm trong khoảng [15°, 75°] hoặc [105°, 165°]
        return (absAngle >= MIN_VERTICAL_ANGLE && absAngle <= MAX_VERTICAL_ANGLE) ||
               (absAngle >= 180 - MAX_VERTICAL_ANGLE && absAngle <= 180 - MIN_VERTICAL_ANGLE);
    }

    /**
     * Lấy giới hạn góc dọc tối đa
     */
    public static double getMaxVerticalAngle() {
        return MAX_VERTICAL_ANGLE;
    }

    /**
     * Lấy giới hạn góc dọc tối thiểu
     */
    public static double getMinVerticalAngle() {
        return MIN_VERTICAL_ANGLE;
    }
}
