package de.ptkapps.gorillas.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Packer {

    private static final String ASSET_DIR = "../assets/";

    public static void main(String[] args) {
        pack("images");
    }

    private static void pack(String folderName) {

        String input = ASSET_DIR + folderName;
        String output = ASSET_DIR + folderName + "/atlas";
        String packFileName = "items";

        TexturePacker.process(input, output, packFileName);
    }
}
