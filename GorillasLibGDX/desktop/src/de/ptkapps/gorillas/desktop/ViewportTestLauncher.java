package de.ptkapps.gorillas.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.ptkapps.gorillas.ViewportTest;

public class ViewportTestLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        // smallest display
        // config.height = 240;
        // config.width = 320;

        // smallest display normal screen size
        // config.height = 320;
        // config.width = 480;

        // smallest display large screen size
        config.height = 480;
        config.width = 800;

        // smallest display xlarge screen size
        // config.height = 600;
        // config.width = 1024;

        // smallest display xxlarge screen size
        // config.height = 1000;
        // config.width = 1537;

        // config.height = 344;
        // config.width = 705;
        // config.height = 300;
        // config.width = 480;
        // config.height = 1000;
        // config.width = 1900;
        // config.height = 480;
        // config.width = 800;
        // config.height = 600;
        // config.width = 1024;
        // config.height = 768;
        // config.width = 1280;
        // config.height = 379;
        // config.width = 640;

        // config.height = (int) (config.height - config.height * 1 / 10f);

        new LwjglApplication(new ViewportTest(), config);
    }
}
