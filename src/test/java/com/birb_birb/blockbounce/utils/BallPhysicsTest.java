package com.birb_birb.blockbounce.utils;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BallPhysics class
 */
@DisplayName("Ball Physics Tests")
class BallPhysicsTest {

    private static final double DELTA = 0.1; // Độ chính xác cho so sánh số thực

    @Test
    @DisplayName("Tính góc từ vận tốc - góc 45 độ")
    void testCalculateAngle45Degrees() {
        double angle = BallPhysics.calculateAngle(1, 1);
        assertEquals(45.0, angle, DELTA);
    }

    @Test
    @DisplayName("Tính góc từ vận tốc - góc 0 độ (ngang phải)")
    void testCalculateAngle0Degrees() {
        double angle = BallPhysics.calculateAngle(1, 0);
        assertEquals(0.0, angle, DELTA);
    }

    @Test
    @DisplayName("Tính góc từ vận tốc - góc 90 độ (xuống dưới)")
    void testCalculateAngle90Degrees() {
        double angle = BallPhysics.calculateAngle(0, 1);
        assertEquals(90.0, angle, DELTA);
    }

    @Test
    @DisplayName("Tính góc từ vận tốc - góc -90 độ (lên trên)")
    void testCalculateAngleNegative90Degrees() {
        double angle = BallPhysics.calculateAngle(0, -1);
        assertEquals(-90.0, angle, DELTA);
    }

    @Test
    @DisplayName("Chuẩn hóa góc - góc quá dốc (80°) phải giảm về 75°")
    void testNormalizeAngleTooSteep() {
        double angle = BallPhysics.normalizeAngle(80);
        assertEquals(75.0, angle, DELTA);
    }

    @Test
    @DisplayName("Chuẩn hóa góc - góc quá ngang (5°) phải tăng lên 15°")
    void testNormalizeAngleTooFlat() {
        double angle = BallPhysics.normalizeAngle(5);
        assertEquals(15.0, angle, DELTA);
    }

    @Test
    @DisplayName("Chuẩn hóa góc - góc bình thường (45°) không đổi")
    void testNormalizeAngleNormal() {
        double angle = BallPhysics.normalizeAngle(45);
        assertEquals(45.0, angle, DELTA);
    }

    @Test
    @DisplayName("Chuẩn hóa góc - góc âm quá dốc (-80°) phải giảm về -75°")
    void testNormalizeAngleNegativeTooSteep() {
        double angle = BallPhysics.normalizeAngle(-80);
        assertEquals(-75.0, angle, DELTA);
    }

    @Test
    @DisplayName("Chuẩn hóa góc - góc âm quá ngang (-5°) phải tăng lên -15°")
    void testNormalizeAngleNegativeTooFlat() {
        double angle = BallPhysics.normalizeAngle(-5);
        assertEquals(-15.0, angle, DELTA);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 15",      // 0° -> 15°
        "10, 15",     // 10° -> 15°
        "45, 45",     // 45° không đổi
        "75, 75",     // 75° không đổi
        "80, 75",     // 80° -> 75°
        "90, 75",     // 90° -> 75°
        "-5, -15",    // -5° -> -15°
        "-45, -45",   // -45° không đổi
        "-80, -75"    // -80° -> -75°
    })
    @DisplayName("Chuẩn hóa góc - nhiều trường hợp")
    void testNormalizeAngleMultipleCases(double input, double expected) {
        double result = BallPhysics.normalizeAngle(input);
        assertEquals(expected, result, DELTA);
    }

    @Test
    @DisplayName("Tính vận tốc từ góc 0° và tốc độ 5")
    void testCalculateVelocityHorizontal() {
        Point2D velocity = BallPhysics.calculateVelocity(0, 5);
        assertEquals(5.0, velocity.getX(), DELTA);
        assertEquals(0.0, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Tính vận tốc từ góc 90° và tốc độ 5")
    void testCalculateVelocityVertical() {
        Point2D velocity = BallPhysics.calculateVelocity(90, 5);
        assertEquals(0.0, velocity.getX(), DELTA);
        assertEquals(5.0, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Tính vận tốc từ góc 45° và tốc độ 5")
    void testCalculateVelocity45Degrees() {
        Point2D velocity = BallPhysics.calculateVelocity(45, 5);
        assertEquals(3.535, velocity.getX(), DELTA);
        assertEquals(3.535, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Chuẩn hóa vận tốc - vận tốc quá cao phải giảm")
    void testNormalizeVelocityTooFast() {
        Point2D velocity = new Point2D(10, 10); // Tốc độ ~14.14
        Point2D normalized = BallPhysics.normalizeVelocity(velocity, 3.0);

        // Tốc độ sau chuẩn hóa không được vượt quá 3.0 * 1.2 = 3.6
        double speed = normalized.magnitude();
        assertTrue(speed <= 3.6);
    }

    @Test
    @DisplayName("Chuẩn hóa vận tốc - góc được điều chỉnh nếu không hợp lệ")
    void testNormalizeVelocityInvalidAngle() {
        Point2D velocity = new Point2D(10, 0.5); // Góc ~2.86° (quá ngang)
        Point2D normalized = BallPhysics.normalizeVelocity(velocity, 5.0);

        // Góc mới phải >= 15° (với tolerance nhỏ cho sai số làm tròn)
        double angle = BallPhysics.calculateAngle(normalized.getX(), normalized.getY());
        assertTrue(Math.abs(angle) >= 14.9, "Angle was: " + angle);
    }

    @Test
    @DisplayName("Tính bật paddle - va chạm ở giữa paddle")
    void testPaddleBounceCenterHit() {
        Point2D velocity = BallPhysics.calculatePaddleBounce(400, 400, 80, 3.0);

        // Va chạm ở giữa -> vận tốc X = 0, Y hướng lên
        assertEquals(0.0, velocity.getX(), DELTA);
        assertTrue(velocity.getY() < 0); // Hướng lên
        assertEquals(-3.0, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Tính bật paddle - va chạm bên trái")
    void testPaddleBounceLeftSide() {
        Point2D velocity = BallPhysics.calculatePaddleBounce(370, 400, 80, 3.0);

        // Va chạm bên trái -> vận tốc X âm (bay sang trái)
        assertTrue(velocity.getX() < 0);
        assertTrue(velocity.getY() < 0); // Hướng lên
    }

    @Test
    @DisplayName("Tính bật paddle - va chạm bên phải")
    void testPaddleBounceRightSide() {
        Point2D velocity = BallPhysics.calculatePaddleBounce(430, 400, 80, 3.0);

        // Va chạm bên phải -> vận tốc X dương (bay sang phải)
        assertTrue(velocity.getX() > 0);
        assertTrue(velocity.getY() < 0); // Hướng lên
    }

    @Test
    @DisplayName("Tính bật paddle - đảm bảo vận tốc Y tối thiểu -2.0")
    void testPaddleBounceMinimumYVelocity() {
        // Va chạm cực biên paddle
        Point2D velocity = BallPhysics.calculatePaddleBounce(440, 400, 80, 3.0);

        // Vận tốc Y phải <= -2.0 để tránh bóng bay chậm
        assertTrue(velocity.getY() <= -2.0);
    }

    @Test
    @DisplayName("Kiểm tra góc hợp lệ - góc 45° hợp lệ")
    void testIsAngleValid45Degrees() {
        assertTrue(BallPhysics.isAngleValid(45));
    }

    @Test
    @DisplayName("Kiểm tra góc hợp lệ - góc 5° không hợp lệ")
    void testIsAngleInvalid5Degrees() {
        assertFalse(BallPhysics.isAngleValid(5));
    }

    @Test
    @DisplayName("Kiểm tra góc hợp lệ - góc 85° không hợp lệ")
    void testIsAngleInvalid85Degrees() {
        assertFalse(BallPhysics.isAngleValid(85));
    }

    @Test
    @DisplayName("Lấy giới hạn góc tối đa")
    void testGetMaxVerticalAngle() {
        assertEquals(75.0, BallPhysics.getMaxVerticalAngle(), DELTA);
    }

    @Test
    @DisplayName("Lấy giới hạn góc tối thiểu")
    void testGetMinVerticalAngle() {
        assertEquals(15.0, BallPhysics.getMinVerticalAngle(), DELTA);
    }

    @Test
    @DisplayName("Độ lớn vận tốc được bảo toàn sau chuẩn hóa góc")
    void testVelocityMagnitudePreserved() {
        double speed = 5.0;
        Point2D velocity = BallPhysics.calculateVelocity(30, speed);

        assertEquals(speed, velocity.magnitude(), DELTA);
    }

    @Test
    @DisplayName("Xử lý góc 180° (bay ngược lại)")
    void testNormalizeAngle180Degrees() {
        double angle = BallPhysics.normalizeAngle(180);
        // Góc 180° phải được điều chỉnh về 165° (180 - 15)
        assertEquals(165.0, angle, DELTA);
    }

    @Test
    @DisplayName("Xử lý góc -180° (bay ngược lại)")
    void testNormalizeAngleNegative180Degrees() {
        double angle = BallPhysics.normalizeAngle(-180);
        assertEquals(-165.0, angle, DELTA);
    }
}
