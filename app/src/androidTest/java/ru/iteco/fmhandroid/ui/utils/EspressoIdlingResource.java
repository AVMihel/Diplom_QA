package ru.iteco.fmhandroid.ui.utils;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";
    private static CountingIdlingResource countingIdlingResource =
            new CountingIdlingResource(RESOURCE);

    public static void increment() {
        countingIdlingResource.increment();
    }

    public static void decrement() {
        if (!countingIdlingResource.isIdleNow()) {
            countingIdlingResource.decrement();
        }
    }

    public static IdlingResource getIdlingResource() {
        return countingIdlingResource;
    }

    public static void registerIdlingResource() {
        androidx.test.espresso.IdlingRegistry.getInstance()
                .register(countingIdlingResource);
    }

    public static void unregisterIdlingResource() {
        androidx.test.espresso.IdlingRegistry.getInstance()
                .unregister(countingIdlingResource);
    }
}