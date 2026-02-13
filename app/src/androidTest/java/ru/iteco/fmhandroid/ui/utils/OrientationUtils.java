package ru.iteco.fmhandroid.ui.utils;

import static ru.iteco.fmhandroid.ui.utils.WaitUtils.waitForMillis;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

public class OrientationUtils {

    private static final int DEFAULT_ORIENTATION_DELAY = 2000;
    private static UiDevice uiDevice;

    private static synchronized UiDevice getDevice() {
        if (uiDevice == null) {
            uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        }
        return uiDevice;
    }

    // Поворот экрана в ландшафтную ориентацию
    public static void rotateToLandscape() {
        rotateToLandscape(DEFAULT_ORIENTATION_DELAY);
    }

    // Поворот экрана в ландшафтную ориентацию с задержкой
    public static void rotateToLandscape(long delayMillis) {
        try {
            getDevice().setOrientationLeft();
            waitForMillis(delayMillis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rotate to landscape: " + e.getMessage(), e);
        }
    }

    // Поворот экрана в портретную ориентацию
    public static void rotateToPortrait() {
        rotateToPortrait(DEFAULT_ORIENTATION_DELAY);
    }

    // Поворот экрана в портретную ориентацию с задержкой
    public static void rotateToPortrait(long delayMillis) {
        try {
            getDevice().setOrientationNatural();
            waitForMillis(delayMillis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rotate to portrait: " + e.getMessage(), e);
        }
    }
}
