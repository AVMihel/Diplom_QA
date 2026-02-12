package ru.iteco.fmhandroid.ui.utils;

import static ru.iteco.fmhandroid.ui.utils.WaitUtils.waitForMillis;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import io.qameta.allure.kotlin.Allure;

public class OrientationUtils {

    private static final int DEFAULT_ORIENTATION_DELAY = 2000;
    private static UiDevice uiDevice;

    private static synchronized UiDevice getDevice() {
        if (uiDevice == null) {
            uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        }
        return uiDevice;
    }

    public static void rotateToLandscape() {
        Allure.step("Поворот экрана в ландшафтную ориентацию");
        rotateToLandscape(DEFAULT_ORIENTATION_DELAY);
    }

    public static void rotateToLandscape(long delayMillis) {
        Allure.step("Поворот экрана в ландшафтную ориентацию (задержка: " + delayMillis + " мс)");
        try {
            getDevice().setOrientationLeft();
            waitForMillis(delayMillis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rotate to landscape: " + e.getMessage(), e);
        }
    }

    public static void rotateToPortrait() {
        Allure.step("Поворот экрана в портретную ориентацию");
        rotateToPortrait(DEFAULT_ORIENTATION_DELAY);
    }

    public static void rotateToPortrait(long delayMillis) {
        Allure.step("Поворот экрана в портретную ориентацию (задержка: " + delayMillis + " мс)");
        try {
            getDevice().setOrientationNatural();
            waitForMillis(delayMillis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to rotate to portrait: " + e.getMessage(), e);
        }
    }
}
