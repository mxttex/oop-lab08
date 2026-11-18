package it.unibo.mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.unibo.mvc.api.DrawNumber;
import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.controller.DrawNumberControllerImpl;
import it.unibo.mvc.model.DrawNumberImpl;

/**
 * Application entry-point.
 */
public final class LaunchApp {

    private LaunchApp() {
    }

    /**
     * Runs the application.
     *
     * @throws ClassNotFoundException    if the fetches class does not exist
     * @throws NoSuchMethodException     if the 0-ary constructor do not exist
     * @throws InvocationTargetException if the constructor throws exceptions
     * @throws InstantiationException    if the constructor throws exceptions
     * @throws IllegalAccessException    in case of reflection issues
     * @throws IllegalArgumentException  in case of reflection issues
     */
    public static void main() throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException {
        final var model = new DrawNumberImpl();
        final DrawNumberController app = new DrawNumberControllerImpl(model);

        // loading views via reflection
        for (final var viewType : List.of("StandardOutput", "Swing")) {
            final var clazz = Class.forName("it.unibo.mvc.view.DrawNumber" + viewType + "View");
            final var newView = clazz.getConstructor().newInstance();
            app.addView((DrawNumberView) newView);

            //per l'esercizio, chiedeva tre GUI e Consoles
            // for (int i = 0; i < 3; i++) {
            //     final var newView = clazz.getConstructor().newInstance();
            //     if (newView instanceof DrawNumberView) {
            //         app.addView((DrawNumberView) newView);
            //     } else {
            //         throw new IllegalStateException(newView.getClass() + " is not a subclass of " + DrawNumber.class);
            //     }
            // }
        }
    }
}
