package org.example;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirstCardTest {

    @Test
    public void firstCardIsCorrect() throws IOException {
        Path directory = Paths.get("imgs_marked");
        List<File> imgs = new ArrayList<>();
        Files.list(directory)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".png"))
                .forEach(path -> {
                    imgs.add(path.toFile());
                });
        assertTrue(imgs.size() > 0, "Empty image set");
        List<Boolean> testReuslts = imgs.stream().map(this::testFile).toList();
        int falses = 0;
        for (Boolean testReuslt : testReuslts) {
            if (!testReuslt) {
                falses++;
            }
        }
        assertTrue(falses == 0, "There are '" + falses + "' tests with wrong answer and '"
                + (testReuslts.size() - falses) + "' with right answer");
    }

    public Boolean testFile(File file) {
        String name = file.getName();
        System.out.println("File name: " + name);
        int end = 2;
        if (name.startsWith("1")) {
            end = 3;
        }
        String firstCard = name.substring(0, end);
        System.out.println("first card name:" + firstCard);
        String recognizedName = "";
        try {
            recognizedName = Main.recognizeText(file).substring(0, end);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false, "Failed with testing file " + name);
        }
        boolean equals = recognizedName.equals(firstCard);
        if (!equals) {
            System.out.println("Expected: " + firstCard + ", but got: " + recognizedName + " for file: " + name);
        }
        return equals;
    }
}