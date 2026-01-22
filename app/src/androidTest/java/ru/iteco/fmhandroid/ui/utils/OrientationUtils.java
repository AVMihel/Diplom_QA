package ru.iteco.fmhandroid.ui.utils;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

public class OrientationUtils {

    private static UiDevice uiDevice;

    private static UiDevice getDevice() {
        if (uiDevice == null) {
            uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        }
        return uiDevice;
    }

    // Поворот в ландшафтную ориентацию
    public static void rotateToLandscape() {
        try {
            getDevice().setOrientationLeft();
            WaitUtils.waitForMillis(2000);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rotate to landscape: " + e.getMessage(), e);
        }
    }

    // Поворот в портретную ориентацию
    public static void rotateToPortrait() {
        try {
            getDevice().setOrientationNatural();
            WaitUtils.waitForMillis(2000);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rotate to portrait: " + e.getMessage(), e);
        }
    }

    // Сброс ориентации в дефолтную (для очистки)
    public static void resetOrientation() {
        rotateToPortrait();
    }
}
